package com.example.demo.SAGA.query.controller;

import com.example.demo.SAGA.query.queries.FindParcelCountQuery;
import com.example.demo.SAGA.query.queries.FindParcelQuery;
import com.example.demo.SAGA.query.queries.FindParcelsQuery;
import com.example.demo.util.PageResponseType;
import com.example.demo.domain.Package;
import lombok.RequiredArgsConstructor;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor

@RestController
@RequestMapping("/parcels")
public class ParcelQueryController {
    private final ReactorQueryGateway reactiveQueryGateway;

    @GetMapping("/tracking/{trackingNumber}")
    public Mono<Package> getParcelByTrackingNumber(FindParcelQuery query, @PathVariable("trackingNumber") String trackingNumber){
        query.setTrackingNumber(trackingNumber);
        return reactiveQueryGateway.query(query, ResponseTypes.instanceOf(Package.class));
    }

    @GetMapping
    public Mono<Page<Package>> getParcels(@RequestParam(value = "page", defaultValue = "0") String page, @RequestParam(value = "size", defaultValue = "5") String size, @RequestParam(value = "sort", defaultValue = "lastModifiedDate") String sort, @RequestParam(value = "direction", defaultValue = "asc") String sortDirection){
        FindParcelsQuery query = new FindParcelsQuery(PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), sortDirection.equalsIgnoreCase("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending()));  // sorting not implemented - maybe need custom serializer & deserializer(in deserializer we cant see sorting information)

        // we need to implement our OWN Response Type for it to work with Axon framework...
        final ResponseType<Page<Package>> responseType = new PageResponseType<>(Package.class);
        final Mono<Page<Package>> resultFuture = reactiveQueryGateway.query(query, responseType);
        return resultFuture;
    }

    @GetMapping("/count")
    public Mono<Long> getParcelCount(){
        FindParcelCountQuery query = new FindParcelCountQuery();
        return reactiveQueryGateway.query(query, Long.class);
    }
}
