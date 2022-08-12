package com.example.demo.feign;

//import org.springframework.cloud.openfeign.FeignClient;
import com.example.demo.domain.DeliveryResponse;
import org.springframework.web.bind.annotation.*;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

//@FeignClient(name = "delivery-service", fallbackFactory = DeliveryServiceClientFallbackFactory.class)
@ReactiveFeignClient(name = "delivery-service", fallbackFactory = DeliveryServiceClientFallbackFactory.class)
public interface DeliveryServiceClient{
    @RequestMapping(value = "new-delivery/{parcelPublicId}", method = RequestMethod.GET, produces = "application/json", consumes = "application/json") // URL from delivery microservice controller
    Mono<DeliveryResponse> createDelivery(@PathVariable("parcelPublicId") UUID parcelPublicId);
}
