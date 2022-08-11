package com.example.demo.feign;

import com.example.demo.domain.DeliveryResponse;
import com.example.demo.domain.Package;
import com.example.demo.domain.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// TODO: Change Feign Client to reactive

@FeignClient(name = "delivery-service")
public interface DeliveryServiceClient{
    @RequestMapping(value = "new-delivery/{parcelPublicId}", method = RequestMethod.GET, produces = "application/json", consumes = "application/json") // URL from payment microservice controller
    DeliveryResponse createDelivery(@PathVariable("parcelPublicId") UUID parcelPublicId);
}
