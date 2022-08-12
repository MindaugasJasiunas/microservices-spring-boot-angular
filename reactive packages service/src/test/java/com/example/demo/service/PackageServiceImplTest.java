package com.example.demo.service;

import com.example.demo.util.CustomPageImpl;
import com.example.demo.domain.*;
import com.example.demo.domain.Package;
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

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PackageServiceImplTest {
    @Mock
    PackageRepository packageRepository;
    @Mock
    SequenceGeneratorService sequenceService;
    @Mock
    SenderServiceImpl senderService;
    @Mock
    ReceiverServiceImpl receiverService;

    @InjectMocks
    PackageServiceImpl service;// = new PackageServiceImpl(packageRepository, senderService, receiverService, sequenceService);

    UUID uuid = UUID.randomUUID();

    Address address = new Address("Temp str", null, null, "5", "1", "City", "State", "Country");
    Sender sender = new Sender("1", "John", "Doe", "temp company", "00000", "mail", address);
    Receiver receiver = new Receiver("1", "Jane", "Doe", "temp comp", "00000", "mail", address);
    Package pkg = new Package("1", uuid, UUID.randomUUID().toString().replaceAll("-", ""), PackageState.NEW, LocalDateTime.now(), LocalDateTime.now(), 1L, 5L, "pkg desc", false, false, "1", "1", sender, receiver);

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
        Mockito.when(receiverService.findReceiverById(any(String.class))).thenReturn(Mono.just(receiver));
        Mockito.when(senderService.findSenderById(any(String.class))).thenReturn(Mono.just(sender));
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

        Assertions.assertNotNull(pkgReturned.getSender());
        Assertions.assertNotNull(pkgReturned.getReceiver());

        Mockito.verify(packageRepository, Mockito.times(1)).findByTrackingNumber(any(String.class));
        Mockito.verify(senderService, Mockito.times(1)).findSenderById(any(String.class));
        Mockito.verify(receiverService, Mockito.times(1)).findReceiverById(any(String.class));
    }

    @Test
    void findAll() {
        Long numOfElementsPerPage = 5L;
        Long numOfElementsTotal = 9L;
        Long numOfPages = numOfElementsTotal / numOfElementsPerPage + ((numOfElementsTotal % numOfElementsPerPage) > 0 ? 1 : 0);
        Mockito.when(packageRepository.findAllBy(PageRequest.of(0,5))).thenReturn(getPackages());
        Mockito.when(packageRepository.count()).thenReturn(Mono.just(numOfElementsTotal));

        Mockito.when(receiverService.findReceiverById(any(String.class))).thenReturn(Mono.just(receiver));
        Mockito.when(senderService.findSenderById(any(String.class))).thenReturn(Mono.just(sender));

        Mono<CustomPageImpl<Package>> packagePageMono = service.findAll(PageRequest.of(0,5));
        Page<Package> packagePage = packagePageMono.block();

        Assertions.assertEquals(numOfElementsTotal, packagePage.getTotalElements());
        Assertions.assertEquals(numOfElementsPerPage, packagePage.getNumberOfElements());
        Assertions.assertEquals(numOfPages, packagePage.getTotalPages());

        Mockito.verify(packageRepository, Mockito.times(1)).findAllBy(any(Pageable.class));
        Mockito.verify(packageRepository, Mockito.times(1)).count();
        Mockito.verify(receiverService, Mockito.times(5)).findReceiverById(any(String.class));
        Mockito.verify(senderService, Mockito.times(5)).findSenderById(any(String.class));
    }

    @Test
    void createNewPackage() {
        Long id = 5L;
        ArgumentCaptor<Package> packageArgumentCaptor = ArgumentCaptor.forClass(Package.class);
        Mockito.when(sequenceService.generateSequence(any(String.class))).thenReturn(Mono.just(new DatabaseSequence(Package.SEQUENCE_NAME, id)));
        Mockito.when(packageRepository.save(packageArgumentCaptor.capture())).thenReturn(Mono.just(pkg));
        Mockito.when(packageRepository.findByTrackingNumber(any(String.class))).thenReturn(Mono.empty());
        Mockito.when(senderService.saveSenderIfNotAlreadyExists(any(Sender.class))).thenReturn(Mono.just(sender));
        Mockito.when(receiverService.saveReceiverIfNotAlreadyExists(any(Receiver.class))).thenReturn(Mono.just(receiver));

//        service.createNewPackage(pkg).block();

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
        Mockito.verify(senderService, Mockito.times(1)).saveSenderIfNotAlreadyExists(any(Sender.class));
        Mockito.verify(receiverService, Mockito.times(1)).saveReceiverIfNotAlreadyExists(any(Receiver.class));
    }

    private Flux<Package> getPackages(){
        Address address = new Address("Temp str", null, null, "5", "1", "City", "State", "Country");
        Sender sender = new Sender("1", "John", "Doe", "temp company", "00000", "mail", address);
        Receiver receiver = new Receiver("1", "Jane", "Doe", "temp comp", "00000", "mail", address);

        Package pkg = new Package();
        pkg.setSenderId("abc");
        pkg.setReceiverId("abc");
        pkg.setSender(sender);
        pkg.setReceiver(receiver);
        Package pkg2= new Package();
        pkg2.setSenderId("abc");
        pkg2.setReceiverId("abc");
        pkg2.setSender(sender);
        pkg2.setReceiver(receiver);
        Package pkg3= new Package();
        pkg3.setSenderId("abc");
        pkg3.setReceiverId("abc");
        pkg3.setSender(sender);
        pkg3.setReceiver(receiver);
        Package pkg4= new Package();
        pkg4.setSenderId("abc");
        pkg4.setReceiverId("abc");
        pkg4.setSender(sender);
        pkg4.setReceiver(receiver);
        Package pkg5= new Package();
        pkg5.setSenderId("abc");
        pkg5.setReceiverId("abc");
        pkg5.setSender(sender);
        pkg5.setReceiver(receiver);
        return Flux.just(pkg, pkg2, pkg3, pkg4, pkg5);
    }
}