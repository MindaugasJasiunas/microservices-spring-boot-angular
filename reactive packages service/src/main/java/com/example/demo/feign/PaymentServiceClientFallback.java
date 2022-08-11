package com.example.demo.feign;

import com.example.demo.domain.Package;
import com.example.demo.domain.PaymentResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentServiceClientFallback implements PaymentServiceClient {
    private final Throwable throwable;

    public PaymentServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
        log.error("[Feign Client FALLBACK (Payment Service is down!) throwable: "+throwable);
    }

    @Override
    public PaymentResponse createPayment(Package parcel) {
        // our service is down: do smth to save data, inform administration and return default message.
        log.error("[Feign Client FALLBACK (Payment Service is down!) Parcel: " + parcel + " with public ID: "+parcel.getPublicId()+" payment can't be created!");
        return new PaymentResponse(parcel.getPublicId(), "ERROR OCCURRED", "ERROR OCCURRED");
    }
}
