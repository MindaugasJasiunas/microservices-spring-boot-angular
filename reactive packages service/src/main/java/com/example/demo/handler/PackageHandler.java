package com.example.demo.handler;

import com.example.demo.exceptions.BadRequestException;
import com.example.demo.util.CustomPageImpl;
import com.example.demo.domain.Package;
import com.example.demo.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor

@Component
public class PackageHandler {
    private final PackageService packageService;

    public Mono<ServerResponse> trackPackage(ServerRequest request) {
        String trackingNumber = request.pathVariable("trackingNumber");
        return packageService.findPackageByTrackingNumber(trackingNumber)
                .flatMap(pkg -> ServerResponse.ok().contentType(APPLICATION_JSON).body(BodyInserters.fromValue(pkg)))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> listPackages(ServerRequest request) {
        int page = 0;
        int size = 5;
        String sortFieldName = "lastModifiedDate";

        if(request.queryParam("page").isPresent()){
            try{
                page = Integer.parseInt(request.queryParam("page").get());
            }catch (NumberFormatException e){}
        }
        if(request.queryParam("size").isPresent()){
            try{
                size = Integer.parseInt(request.queryParam("size").get());
            }catch (NumberFormatException e){}
        }
        if(request.queryParam("sort").isPresent()){
            sortFieldName = request.queryParam("sort").get();
        }
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(BodyInserters.fromPublisher(packageService.findAll(PageRequest.of(page, size).withSort(Sort.by(sortFieldName).ascending())), new ParameterizedTypeReference<CustomPageImpl<Package>>(){}));
    }

    public Mono<ServerResponse> createPackage(ServerRequest request) {
        /*return request.bodyToMono(Package.class)
                .flatMap(packageService::createNewPackage)
                .flatMap(createdPackage -> ServerResponse.created(URI.create("/tracking/" + createdPackage.getTrackingNumber())).build())
                .switchIfEmpty(ServerResponse.badRequest().build());*/
        return Mono.error(() -> new BadRequestException("createPackage handler doesnt exists anymore"));
    }

}
