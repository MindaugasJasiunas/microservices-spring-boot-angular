package com.example.demo.repository;

import com.example.demo.domain.Package;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PackageRepository extends ReactiveMongoRepository<Package, String> {
    Flux<Package> findAllBy(Pageable pageable);
    Mono<Package> findByPublicId(UUID publicId);
    Mono<Package> findByTrackingNumber(String trackingNumber);
}
