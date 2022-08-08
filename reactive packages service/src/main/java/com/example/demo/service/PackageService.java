package com.example.demo.service;

import com.example.demo.util.CustomPageImpl;
import com.example.demo.domain.Package;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PackageService {
    Mono<Package> findPackageByPublicId(UUID publicId);
    Mono<Package> findPackageByTrackingNumber(String trackingNumber);
    Mono<CustomPageImpl<Package>> findAll(PageRequest pageRequest);
    Mono<Package> createNewPackage(Package newPackage);
}
