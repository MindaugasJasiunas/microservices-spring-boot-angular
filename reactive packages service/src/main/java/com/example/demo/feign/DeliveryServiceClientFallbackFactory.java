package com.example.demo.feign;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DeliveryServiceClientFallbackFactory implements FallbackFactory<DeliveryServiceClient> {

    @Override
    public DeliveryServiceClient create(Throwable cause) {
        return new DeliveryServiceClientFallback(cause);
    }
}
