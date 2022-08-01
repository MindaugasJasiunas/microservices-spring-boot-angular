package com.example.demo.service;

import com.example.demo.domain.DatabaseSequence;
import com.example.demo.domain.Package;
import com.example.demo.domain.PackageState;
import com.example.demo.repository.PackageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PackageServiceImplTest {
    @Mock
    PackageRepository packageRepository;
    @Mock
    SequenceGeneratorService sequenceService;
    @InjectMocks
    PackageServiceImpl service;

    UUID uuid = UUID.randomUUID();
    Package pkg = new Package("1",uuid,UUID.randomUUID().toString().replaceAll("-",""), PackageState.NEW, LocalDateTime.now(), LocalDateTime.now());


    @Test
    void findPackageByPublicId() {
        Mockito.when(packageRepository.findByPublicId(any(UUID.class))).thenReturn(Mono.just(pkg));

        Mono<Package> packageMono = service.findPackageByPublicId(uuid);
        Package pkgReturned = packageMono.block();

        Assertions.assertNotNull(pkgReturned);
        Assertions.assertEquals(pkg.getId(), pkgReturned.getId());
        Assertions.assertEquals(pkg.getPublicId(), pkgReturned.getPublicId());
        Assertions.assertEquals(pkg.getTrackingNumber(), pkgReturned.getTrackingNumber());
        Assertions.assertEquals(pkg.getPackageStatus(), pkgReturned.getPackageStatus());
        Assertions.assertEquals(pkg.getCreatedDate(), pkgReturned.getCreatedDate());
        Assertions.assertEquals(pkg.getLastModifiedDate(), pkgReturned.getLastModifiedDate());

        Mockito.verify(packageRepository, Mockito.times(1)).findByPublicId(any(UUID.class));
    }

    @Test
    void findPackageByTrackingNumber() {
        Mockito.mock(PackageRepository.class);
        Mockito.when(packageRepository.findByTrackingNumber(any(String.class))).thenReturn(Mono.just(pkg));
        Mono<Package> packageMono = service.findPackageByTrackingNumber("anyString");

        Package pkgReturned = packageMono.block();
        Assertions.assertNotNull(pkgReturned);
        Assertions.assertEquals(pkg.getId(), pkgReturned.getId());
        Assertions.assertEquals(pkg.getPublicId(), pkgReturned.getPublicId());
        Assertions.assertEquals(pkg.getTrackingNumber(), pkgReturned.getTrackingNumber());
        Assertions.assertEquals(pkg.getPackageStatus(), pkgReturned.getPackageStatus());
        Assertions.assertEquals(pkg.getCreatedDate(), pkgReturned.getCreatedDate());
        Assertions.assertEquals(pkg.getLastModifiedDate(), pkgReturned.getLastModifiedDate());

        Mockito.verify(packageRepository, Mockito.times(1)).findByTrackingNumber(any(String.class));
    }

    @Test
    void findAll() {
        Long numOfElementsPerPage = 5L;
        Long numOfElementsTotal = 9L;
        Long numOfPages = numOfElementsTotal / numOfElementsPerPage + ((numOfElementsTotal % numOfElementsPerPage) > 0 ? 1 : 0);
        Mockito.when(packageRepository.findAllBy(PageRequest.of(0,5))).thenReturn(Flux.just(new Package(), new Package(), new Package(), new Package(), new Package()));
        Mockito.when(packageRepository.count()).thenReturn(Mono.just(numOfElementsTotal));

        Mono<Page<Package>> packagePageMono = service.findAll(PageRequest.of(0,5));
        Page<Package> packagePage = packagePageMono.block();

        Assertions.assertEquals(numOfElementsTotal, packagePage.getTotalElements());
        Assertions.assertEquals(numOfElementsPerPage, packagePage.getNumberOfElements());
        Assertions.assertEquals(numOfPages, packagePage.getTotalPages());

        Mockito.verify(packageRepository, Mockito.times(1)).findAllBy(any(Pageable.class));
        Mockito.verify(packageRepository, Mockito.times(1)).count();
    }

    @Test
    void createNewPackage() {
        Long id = 5L;
        ArgumentCaptor<Package> packageArgumentCaptor = ArgumentCaptor.forClass(Package.class);
        Mockito.when(sequenceService.generateSequence(any(String.class))).thenReturn(Mono.just(new DatabaseSequence(Package.SEQUENCE_NAME, id)));
        Mockito.when(packageRepository.save(packageArgumentCaptor.capture())).thenReturn(Mono.just(pkg));
        Mockito.when(packageRepository.findByTrackingNumber(any(String.class))).thenReturn(Mono.empty());

        service.createNewPackage(pkg).block();

        Assertions.assertNotNull(packageArgumentCaptor.getValue());
        Assertions.assertEquals(String.valueOf(id), packageArgumentCaptor.getValue().getId());
        Assertions.assertEquals(PackageState.NEW, packageArgumentCaptor.getValue().getPackageStatus());
        Assertions.assertNotNull(packageArgumentCaptor.getValue().getPublicId());
        Assertions.assertNotNull(packageArgumentCaptor.getValue().getTrackingNumber());
        Assertions.assertNotNull(packageArgumentCaptor.getValue().getCreatedDate());
        Assertions.assertNotNull(packageArgumentCaptor.getValue().getLastModifiedDate());

        Mockito.verify(packageRepository, Mockito.times(1)).save(any(Package.class));
        Mockito.verify(sequenceService, Mockito.times(1)).generateSequence(any(String.class));
        Mockito.verify(packageRepository, Mockito.times(1)).findByTrackingNumber(any(String.class));
    }
}