package com.example.demo.SAGA.command.controller;

import com.example.demo.SAGA.command.command.CreateParcelCommand;
import com.example.demo.domain.Package;
import lombok.RequiredArgsConstructor;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor

@RestController
@RequestMapping("/parcels")
public class ParcelCommandController {
    private final ReactorCommandGateway reactiveCommandGateway;

    @PostMapping("/submitNewPackage")
    public Mono<Package> createParcel(@RequestBody Package parcel){
        UUID uuid = UUID.randomUUID();
        parcel.setPublicId(uuid);
        CreateParcelCommand createParcelCommand = new CreateParcelCommand(uuid, parcel);

        //now send to Command Bus for further processing
        return reactiveCommandGateway.send(createParcelCommand);
    }

}
