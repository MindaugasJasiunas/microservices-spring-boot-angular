package com.example.demo.service;

import com.example.demo.feign.DeliveryServiceClient;
import com.example.demo.feign.PaymentServiceClient;
import com.example.demo.util.CustomPageImpl;
import com.example.demo.domain.*;
import com.example.demo.domain.Package;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.repository.PackageRepository;
import com.example.demo.util.validator.PackageValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.demo.util.validator.PackageValidator.*;

@Slf4j
@RequiredArgsConstructor

@Service
public class PackageServiceImpl implements PackageService{
    private final PackageRepository packageRepository;
    private final SenderService senderService;
    private final ReceiverService receiverService;
    private final SequenceGeneratorService sequenceGeneratorService;

    private final PaymentServiceClient paymentServiceClient;  // feign client
    private final DeliveryServiceClient deliveryServiceClient;  // feign client

    @Override
    public Mono<Package> findPackageByPublicId(UUID publicId) {
        return packageRepository.findByPublicId(publicId);
    }

    @Override
    public Mono<Package> findPackageByTrackingNumber(String trackingNumber) {
        log.debug("[PackageServiceImpl] findPackageByTrackingNumber(trackingNumber: "+trackingNumber+") called");
        return packageRepository.findByTrackingNumber(trackingNumber)
                .flatMap(aPackage -> setReceiverAndSender(Mono.just(aPackage)))
                .switchIfEmpty(Mono.error(() -> new BadRequestException("Package with provided tracking number not found")));
    }

    @Override
    public Mono<CustomPageImpl<Package>> findAll(PageRequest pageRequest) {
        log.debug("[PackageServiceImpl] findAll("+pageRequest+") called");
        return packageRepository.findAllBy(pageRequest)
                .flatMap(aPackage -> setReceiverAndSender(Mono.just(aPackage))) // set receivers & senders for each package - can throw error ' java.lang.IllegalArgumentException: The given id must not be null! '
                .collectList()
                .zipWith(packageRepository.count())
                .map(tuple -> new CustomPageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
    }

    @Override
    public Mono<Long> getPackageCount(){
        return packageRepository.count();
    }

    @Override
    public Mono<Package> populateNewPackage(Package newPackage) {
        Mono<Package> packageMono = Mono.just(newPackage);

        return sequenceGeneratorService.generateSequence(Package.SEQUENCE_NAME)
                .zipWith(packageMono)
                .map(tuple -> {
                    tuple.getT2().setId(tuple.getT1().getSeq()+"");
                    return tuple.getT2();
                })

                // generate & set package tracking number
                .zipWith(generateUniquePackageTrackingNumberMono())
                .map(tuple -> {
                    tuple.getT1().setTrackingNumber(tuple.getT2());
                    return tuple.getT1();
                })

                // save sender
                .zipWith(senderService.saveSenderIfNotAlreadyExists(newPackage.getSender()))
                .map(tuple -> {
                    tuple.getT1().setSenderId(tuple.getT2().getId());
                    return tuple.getT1();
                })

                // save receiver
                .zipWith(receiverService.saveReceiverIfNotAlreadyExists(newPackage.getReceiver()))
                .map(tuple -> {
                    tuple.getT1().setReceiverId(tuple.getT2().getId());
                    return tuple.getT1();
                })
                .map(pkg -> {
                    pkg.setPackageStatus(PackageState.NEW);
                    pkg.setCreatedDate(LocalDateTime.now());
                    pkg.setLastModifiedDate(LocalDateTime.now());
                    pkg.setReturn(false);
                    return pkg;
                });
    }

    @Override
    public PackageValidator.ValidationResult isPackageValid(Package pkg){
        return PackageValidator
                .isPackageExists()
                .and(isNumberOfPackagesValid())
                .and(isPackagesWeightValid())
                .and(isSenderExists())
                .and(isSenderAddressValid())
                .and(isSenderFirstNameValid())
                .and(isSenderLastNameValid())
                .and(isSenderPhoneNumberValid())
                .and(isReceiverExists())
                .and(isReceiverAddressValid())
                .and(isReceiverFirstNameValid())
                .and(isReceiverLastNameValid())
                .and(isReceiverPhoneNumberValid())
                .apply(pkg);
    }

    @Override
    public Mono<PaymentResponse> sendPaymentHTTP(Package pkg) {
        Mono<PaymentResponse> payment = paymentServiceClient.createPayment(pkg)
                .map(paymentResponse -> {
                    log.debug("package sent to payments service to register. Response: "+paymentResponse);
                    return paymentResponse;
                });
        return payment;
    }

    @Override
    public Mono<DeliveryResponse> sendDeliveryHTTP(UUID parcelPublicId){
        Mono<DeliveryResponse> delivery = deliveryServiceClient.createDelivery(parcelPublicId)
                .map(deliveryResponse -> {
                    log.debug("package public ID sent to delivery service to register pickup time. Response: "+deliveryResponse);
                    return deliveryResponse;
                });
        return delivery;
    }

    private Mono<String> generateUniquePackageTrackingNumberMono(){
        String randomTrackingNumber = UUID.randomUUID().toString().replaceAll("-", "");
        // check if not duplicate
        return packageRepository.findByTrackingNumber(randomTrackingNumber)
                .filter(aPackage -> aPackage != null)
                .flatMap(existingPackage -> generateUniquePackageTrackingNumberMono())
                .switchIfEmpty(Mono.just(randomTrackingNumber));
    }

    private Mono<Package> setReceiverAndSender(Mono<Package> pkg){
        return pkg.flatMap(aPackage -> Mono.zip(Mono.just(aPackage), receiverService.findReceiverById(aPackage.getReceiverId()), senderService.findSenderById(aPackage.getSenderId())))
                .map(tuple -> {
                    tuple.getT1().setReceiver(tuple.getT2());
                    tuple.getT1().setSender(tuple.getT3());
                    return tuple.getT1();
                });
    }

}
