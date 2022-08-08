package com.example.demo.SAGA.query.handler;

import com.example.demo.SAGA.event.ParcelCreatedEvent;
import com.example.demo.repository.PackageRepository;
import com.example.demo.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor

@Component
public class ParcelsProjection {  //instead of ParcelsEventsHandler
    private final PackageRepository packageRepository;

    @EventHandler
    public void on(ParcelCreatedEvent parcelCreatedEvent){
        log.debug("[ParcelsProjection(Events Handler)][ParcelCreatedEvent] saving parcel:" + parcelCreatedEvent.getParcel());
        packageRepository.save(parcelCreatedEvent.getParcel()).subscribe();
    }
}