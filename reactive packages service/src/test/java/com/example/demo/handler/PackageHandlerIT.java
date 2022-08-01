package com.example.demo.handler;

import com.example.demo.domain.DatabaseSequence;
import com.example.demo.domain.Package;
import com.example.demo.domain.PackageState;
import com.example.demo.repository.DatabaseSequenceRepository;
import com.example.demo.repository.PackageRepository;
import com.example.demo.router.PackageRoutes;
import com.example.demo.service.PackageServiceImpl;
import com.example.demo.service.SequenceGeneratorServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@WebFluxTest
@Import({PackageRoutes.class, PackageHandler.class, PackageServiceImpl.class, SequenceGeneratorServiceImpl.class})
class PackageHandlerIT {
    @Autowired
    private WebTestClient webClient;

    @MockBean
    PackageRepository packageRepository;
    @MockBean
    DatabaseSequenceRepository dbSequenceRepository;


    @Test
    void listPackages_defaultPagingAndSorting() {
        Flux<Package> packages = Flux.fromIterable(getPackages());

        ArgumentCaptor<PageRequest> pageRequestArgumentCaptor = ArgumentCaptor.forClass(PageRequest.class);
        Mockito.when(packageRepository.findAllBy(pageRequestArgumentCaptor.capture())).thenReturn(packages);
        Mockito.when(packageRepository.count()).thenReturn(Mono.just(10L));

        webClient.get().uri("/list")
                .accept(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.totalElements").isEqualTo(10)  // from userRepository.count()
                .jsonPath("$.number").isEqualTo(0)  // default page number - 0
                .jsonPath("$.size").isEqualTo(5)  // default items per page - 5
                .jsonPath("$.numberOfElements").isEqualTo(getPackages().size())
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.content").isArray();

        // check pagination & sorting
        assertNotNull(pageRequestArgumentCaptor.getValue());
        assertEquals(0, pageRequestArgumentCaptor.getValue().getPageNumber());
        assertEquals(5, pageRequestArgumentCaptor.getValue().getPageSize());
        assertTrue(pageRequestArgumentCaptor.getValue().getSort().isSorted());
        assertEquals("lastModifiedDate: ASC", pageRequestArgumentCaptor.getValue().getSort().toString());

        Mockito.verify(packageRepository, Mockito.times(1)).findAllBy(any(PageRequest.class));
        Mockito.verify(packageRepository, Mockito.times(1)).count();
    }

    @Test
    void listPackages_customPagingAndSorting() {
        String sortFieldName = "trackingNumber";
        int page = 1;
        int elementsPerPage = 4;
        long totalElements = 10;
        Flux<Package> packages = Flux.fromIterable(getPackages());

        ArgumentCaptor<PageRequest> pageRequestArgumentCaptor = ArgumentCaptor.forClass(PageRequest.class);
        Mockito.when(packageRepository.findAllBy(pageRequestArgumentCaptor.capture())).thenReturn(packages);
        Mockito.when(packageRepository.count()).thenReturn(Mono.just(totalElements));

        webClient.get().uri("/list?page=1&size=4&sort=trackingNumber")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.totalElements").isEqualTo(totalElements)  // from userRepository.count()
                .jsonPath("$.number").isEqualTo(page)  // default page number - 1
                .jsonPath("$.size").isEqualTo(elementsPerPage)  // default items per page - 4
                .jsonPath("$.numberOfElements").isEqualTo(getPackages().size())
                .jsonPath("$.content").isNotEmpty()
                .jsonPath("$.content").isArray();

        // check pagination & sorting
        assertNotNull(pageRequestArgumentCaptor.getValue());
        assertEquals(page, pageRequestArgumentCaptor.getValue().getPageNumber());
        assertEquals(elementsPerPage, pageRequestArgumentCaptor.getValue().getPageSize());
        assertTrue(pageRequestArgumentCaptor.getValue().getSort().isSorted());
        assertEquals(sortFieldName+": ASC", pageRequestArgumentCaptor.getValue().getSort().toString());

        Mockito.verify(packageRepository, Mockito.times(1)).findAllBy(any(PageRequest.class));
        Mockito.verify(packageRepository, Mockito.times(1)).count();
    }

    @Test
    void trackPackage() {
        Package pkg = getPackages().get(0);
        ArgumentCaptor<String> stringRequestArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(packageRepository.findByTrackingNumber(stringRequestArgumentCaptor.capture())).thenReturn(Mono.just(pkg));

        webClient.get().uri("/tracking/"+pkg.getTrackingNumber())
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody(Package.class)
        .value(packageReturned -> {
            assertNotNull(packageReturned);
            assertEquals(pkg.getPublicId(), packageReturned.getPublicId());
            assertEquals(pkg.getTrackingNumber(), packageReturned.getTrackingNumber());
            assertEquals(pkg.getPackageStatus(), packageReturned.getPackageStatus());
            assertEquals(pkg.getCreatedDate(), packageReturned.getCreatedDate());
            assertEquals(pkg.getLastModifiedDate(), packageReturned.getLastModifiedDate());
        });

        assertNotNull(stringRequestArgumentCaptor.getValue());
        assertEquals(pkg.getTrackingNumber(), stringRequestArgumentCaptor.getValue());

        Mockito.verify(packageRepository, Mockito.times(1)).findByTrackingNumber(any(String.class));
    }

    @Test
    void trackPackage_doesntExist() {
        Package pkg = getPackages().get(0);
        ArgumentCaptor<String> stringRequestArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(packageRepository.findByTrackingNumber(stringRequestArgumentCaptor.capture())).thenReturn(Mono.empty());

        webClient.get().uri("/tracking/"+pkg.getTrackingNumber())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();

        assertNotNull(stringRequestArgumentCaptor.getValue());
        assertEquals(pkg.getTrackingNumber(), stringRequestArgumentCaptor.getValue());

        Mockito.verify(packageRepository, Mockito.times(1)).findByTrackingNumber(any(String.class));
    }


    @Test
    void createPackage() {
        Package pkg = getPackages().get(0);
        DatabaseSequence ds = new DatabaseSequence("packages-sequence", 1L);

        ArgumentCaptor<Package> packageArgumentCaptor = ArgumentCaptor.forClass(Package.class);
        Mockito.when(packageRepository.save(packageArgumentCaptor.capture())).thenReturn(Mono.just(pkg));
        Mockito.when(dbSequenceRepository.findById(any(String.class))).thenReturn(Mono.empty()); // doesnt exist - create new
        ArgumentCaptor<DatabaseSequence> sequenceArgumentCaptor = ArgumentCaptor.forClass(DatabaseSequence.class);
        Mockito.when(dbSequenceRepository.save(sequenceArgumentCaptor.capture())).thenReturn(Mono.just(ds));
        Mockito.when(packageRepository.findByTrackingNumber(any(String.class))).thenReturn(Mono.empty());

        webClient.post().uri("/submitNewPackage")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(pkg))
                .exchange()
                .expectHeader().exists("Location")
                .expectHeader().value("Location", Matchers.containsString("/tracking/"))
                .expectStatus().isCreated();

        assertNotNull(packageArgumentCaptor.getValue());
        assertEquals(String.valueOf(ds.getSeq()), packageArgumentCaptor.getValue().getId());
        assertNotNull(packageArgumentCaptor.getValue().getPublicId());
        assertNotNull(packageArgumentCaptor.getValue().getTrackingNumber());
        assertEquals(PackageState.NEW, packageArgumentCaptor.getValue().getPackageStatus());
        assertTrue(packageArgumentCaptor.getValue().getCreatedDate().isBefore(LocalDateTime.now()));
        assertTrue(packageArgumentCaptor.getValue().getCreatedDate().isAfter(LocalDateTime.now().minusMinutes(1)));
        assertTrue(packageArgumentCaptor.getValue().getLastModifiedDate().isBefore(LocalDateTime.now()));
        assertTrue(packageArgumentCaptor.getValue().getLastModifiedDate().isAfter(LocalDateTime.now().minusMinutes(1)));

        assertNotNull(sequenceArgumentCaptor.getValue());
        assertEquals(1L, sequenceArgumentCaptor.getValue().getSeq());
        assertEquals(Package.SEQUENCE_NAME, sequenceArgumentCaptor.getValue().getId());

        Mockito.verify(packageRepository, Mockito.times(1)).save(any(Package.class));
        Mockito.verify(dbSequenceRepository, Mockito.times(1)).findById(any(String.class));
        Mockito.verify(dbSequenceRepository, Mockito.times(1)).save(any(DatabaseSequence.class));
        Mockito.verify(packageRepository, Mockito.times(1)).findByTrackingNumber(any(String.class));
    }

    private List<Package> getPackages(){
        Package pkg = new Package();
        pkg.setId("1");
        pkg.setPublicId(UUID.randomUUID());
        pkg.setTrackingNumber(UUID.randomUUID().toString().replaceAll("-",""));
        pkg.setPackageStatus(PackageState.NEW);
        pkg.setCreatedDate(LocalDateTime.now());
        pkg.setLastModifiedDate(LocalDateTime.of(2022, Month.JANUARY, 5, 0, 0, 0));

        Package pkg2 = new Package();
        pkg2.setId("2");
        pkg2.setPublicId(UUID.randomUUID());
        pkg2.setTrackingNumber(UUID.randomUUID().toString().replaceAll("-",""));
        pkg2.setPackageStatus(PackageState.NEW);
        pkg2.setCreatedDate(LocalDateTime.now());
        pkg2.setLastModifiedDate(LocalDateTime.of(2022, Month.FEBRUARY, 1, 0, 0, 0));

        Package pkg3 = new Package();
        pkg3.setId("3");
        pkg3.setPublicId(UUID.randomUUID());
        pkg3.setTrackingNumber(UUID.randomUUID().toString().replaceAll("-",""));
        pkg3.setPackageStatus(PackageState.NEW);
        pkg3.setCreatedDate(LocalDateTime.now());
        pkg3.setLastModifiedDate(LocalDateTime.of(2022, Month.MARCH, 1, 0, 0, 0));

        Package pkg4 = new Package();
        pkg4.setId("4");
        pkg4.setPublicId(UUID.randomUUID());
        pkg4.setTrackingNumber(UUID.randomUUID().toString().replaceAll("-",""));
        pkg4.setPackageStatus(PackageState.NEW);
        pkg4.setCreatedDate(LocalDateTime.now());
        pkg4.setLastModifiedDate(LocalDateTime.of(2022, Month.APRIL, 9, 0, 0, 0));

        Package pkg5 = new Package();
        pkg5.setId("5");
        pkg5.setPublicId(UUID.randomUUID());
        pkg5.setTrackingNumber(UUID.randomUUID().toString().replaceAll("-",""));
        pkg5.setPackageStatus(PackageState.NEW);
        pkg5.setCreatedDate(LocalDateTime.now());
        pkg5.setLastModifiedDate(LocalDateTime.of(2022, Month.MAY, 11, 0, 0, 0));

        Package pkg6 = new Package();
        pkg6.setId("6");
        pkg6.setPublicId(UUID.randomUUID());
        pkg6.setTrackingNumber(UUID.randomUUID().toString().replaceAll("-",""));
        pkg6.setPackageStatus(PackageState.NEW);
        pkg6.setCreatedDate(LocalDateTime.now());
        pkg6.setLastModifiedDate(LocalDateTime.of(2022, Month.JUNE, 4, 0, 0, 0));

        List<Package> packageList = Arrays.asList(pkg, pkg2, pkg3, pkg4, pkg5, pkg6);
        return packageList;
    }
}