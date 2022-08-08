package com.example.demo.SAGA.query.handler;

import com.example.demo.SAGA.query.queries.FindParcelQuery;
import com.example.demo.SAGA.query.queries.FindParcelsQuery;
import com.example.demo.domain.Package;
import com.example.demo.service.PackageService;
import com.example.demo.util.CustomPageImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j

@RequiredArgsConstructor
@Component
public class ParcelsQueryHandler {
  private final PackageService packageService;

    // method handling FindParcelsQuery will be called from Query Bus (sent from queryGateway in controller)
    @QueryHandler
    public CompletableFuture<CustomPageImpl<Package>> findProducts(FindParcelsQuery query){
        log.debug("[ParcelsQueryHandler] findProducts("+query.getClass().getName()+"("+query.pageable()+")) called");
        return packageService.findAll(query.pageable()).toFuture();
    }

    @QueryHandler
    public CompletableFuture<Package> findProduct(FindParcelQuery query) {
        log.debug("[ParcelsQueryHandler] findProduct("+query.getClass().getName()+"(trackingNumber: "+query.getTrackingNumber()+")) called");
        return packageService.findPackageByTrackingNumber(query.getTrackingNumber()).toFuture();
    }

}
