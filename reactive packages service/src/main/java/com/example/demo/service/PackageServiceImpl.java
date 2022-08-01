package com.example.demo.service;

import com.example.demo.domain.Package;
import com.example.demo.domain.PackageState;
import com.example.demo.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor

@Service
public class PackageServiceImpl implements PackageService{
    private final PackageRepository packageRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public Mono<Package> findPackageByPublicId(UUID publicId) {
        return packageRepository.findByPublicId(publicId);
    }

    @Override
    public Mono<Package> findPackageByTrackingNumber(String trackingNumber) {
        return packageRepository.findByTrackingNumber(trackingNumber);
    }

    @Override
    public Mono<Page<Package>> findAll(PageRequest pageRequest) {
        return packageRepository.findAllBy(pageRequest)
                .collectList()
                .zipWith(packageRepository.count())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
    }

    @Override
    public Mono<Package> createNewPackage(Package newPackage) {
        Mono<Package> packageMono = Mono.just(newPackage);

        return sequenceGeneratorService.generateSequence(Package.SEQUENCE_NAME)
                .zipWith(packageMono)
                .map(tuple -> {
                    tuple.getT2().setId(tuple.getT1().getSeq()+"");
                    return tuple.getT2();
                })

                .zipWith(generateUniquePackageTrackingNumberMono())
                .map(tuple -> {
                    tuple.getT1().setTrackingNumber(tuple.getT2());
                    return tuple.getT1();
                })

                .map(pkg -> {
                    pkg.setPackageStatus(PackageState.NEW);
//                    pkg.setTrackingNumber(generateUniquePackageTrackingNumber());
                    pkg.setCreatedDate(LocalDateTime.now());
                    pkg.setLastModifiedDate(LocalDateTime.now());
                    log.debug("Package processed before saving to DB: " + pkg);
                    return pkg;
                }).flatMap(packageRepository::save);
    }

    private Mono<String> generateUniquePackageTrackingNumberMono(){
        String randomTrackingNumber = UUID.randomUUID().toString().replaceAll("-", "");
        // check if not duplicate
        return packageRepository.findByTrackingNumber(randomTrackingNumber)
                .filter(aPackage -> aPackage != null)
                .flatMap(existingPackage -> generateUniquePackageTrackingNumberMono())
                .switchIfEmpty(Mono.just(randomTrackingNumber));
    }

    /*private String generateUniquePackageTrackingNumber(){
        String randomTrackingNumber = UUID.randomUUID().toString().replaceAll("-", "");
        return randomTrackingNumber;
    }*/

}
