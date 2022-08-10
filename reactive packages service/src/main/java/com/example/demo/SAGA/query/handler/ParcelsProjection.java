package com.example.demo.SAGA.query.handler;

import com.example.demo.SAGA.event.ParcelCreatedEvent;
import com.example.demo.SAGA.event.PaymentApprovedEvent;
import com.example.demo.SAGA.event.PaymentPendingEvent;
import com.example.demo.domain.PackageState;
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

    // TODO: update Package in DB with different state in each step

    @EventHandler
    public void on(ParcelCreatedEvent parcelCreatedEvent){
        log.debug("[ParcelsProjection(Events Handler)][ParcelCreatedEvent] saving parcel:" + parcelCreatedEvent.getParcel());
        packageRepository.save(parcelCreatedEvent.getParcel()).subscribe();
    }

    // TODO: implement methods to update Package state

    /*@EventHandler
    public void on(PaymentApprovedEvent paymentPendingEvent){
        log.debug("[ParcelsProjection(Events Handler)][PaymentApprovedEvent] updating parcel state to PAYMENT_APPROVED");
        packageRepository.findByPublicId(paymentPendingEvent.getParcelPublicId())
                .flatMap(pkgInDB -> {
                    pkgInDB.setPackageStatus(PackageState.PAYMENT_APPROVED);
                    return packageRepository.save(pkgInDB);
                })
                .subscribe();
    }*/

}