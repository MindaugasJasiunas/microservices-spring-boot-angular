package com.example.demo.feign;

import com.example.demo.domain.DeliveryResponse;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
public class DeliveryServiceClientFallback implements DeliveryServiceClient{
    private final Throwable throwable;

    public DeliveryServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
        log.error("[Feign Client FALLBACK (Delivery Service is down!) throwable: "+throwable);
    }

    @Override
    public Mono<DeliveryResponse> createDelivery(UUID parcelPublicId) {
        // our service is down: do smth to save data, inform administration and return default message.
        log.error("[Feign Client FALLBACK (Delivery Service is down!) Parcel with public ID: " + parcelPublicId + " was not registered for delivery!");

        return Mono.just(new DeliveryResponse(parcelPublicId, "DELIVERY SERVICE IS DOWN. NOT REGISTERED !"));
    }

}
