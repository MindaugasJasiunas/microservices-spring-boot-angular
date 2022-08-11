package com.example.demo.feign;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceClientFallbackFactory implements FallbackFactory<PaymentServiceClient> {

    @Override
    public PaymentServiceClient create(Throwable cause) {
        return new PaymentServiceClientFallback(cause);
    }
}
