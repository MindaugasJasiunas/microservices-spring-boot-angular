package com.example.demo.feign;

import com.example.demo.domain.Package;
import com.example.demo.domain.PaymentResponse;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

//@FeignClient(name = "payment-service", fallbackFactory = PaymentServiceClientFallbackFactory.class)
@ReactiveFeignClient(name = "payment-service", fallbackFactory = PaymentServiceClientFallbackFactory.class)
public interface PaymentServiceClient{
    @RequestMapping(value = "payment", method = RequestMethod.POST, produces = "application/json", consumes = "application/json") // URL from payment microservice controller
    Mono<PaymentResponse> createPayment(@RequestBody Package parcel);
}
