package com.example.demo.feign;

import com.example.demo.domain.Package;
import com.example.demo.domain.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// TODO: Change Feign Client to reactive

@FeignClient(name = "payment-service", fallbackFactory = PaymentServiceClientFallbackFactory.class)
public interface PaymentServiceClient{
    @RequestMapping(value = "payment", method = RequestMethod.POST, produces = "application/json", consumes = "application/json") // URL from payment microservice controller
    PaymentResponse createPayment(@RequestBody Package parcel);
}
