package com.example.demo.service;

import com.example.demo.domain.DeliveryResponse;
import com.example.demo.domain.PaymentResponse;
import com.example.demo.util.CustomPageImpl;
import com.example.demo.domain.Package;
import com.example.demo.util.validator.PackageValidator;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PackageService {
    Mono<Package> findPackageByPublicId(UUID publicId);
    Mono<Package> findPackageByTrackingNumber(String trackingNumber);
    Mono<CustomPageImpl<Package>> findAll(PageRequest pageRequest);

    PackageValidator.ValidationResult isPackageValid(Package pkg);

    Mono<Package> populateNewPackage(Package newPackage);
    Mono<PaymentResponse> sendPaymentHTTP(Package pkg);
    Mono<DeliveryResponse> sendDeliveryHTTP(UUID parcelPublicId);
}
