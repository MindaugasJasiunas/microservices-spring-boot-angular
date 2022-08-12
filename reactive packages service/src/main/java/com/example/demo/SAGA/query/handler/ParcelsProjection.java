package com.example.demo.SAGA.query.handler;

import com.example.demo.SAGA.event.DeliveryErrorEvent;
import com.example.demo.SAGA.event.ParcelCreatedEvent;
import com.example.demo.domain.Package;
import com.example.demo.domain.Receiver;
import com.example.demo.domain.Sender;
import com.example.demo.repository.PackageRepository;
import com.example.demo.repository.ReceiverRepository;
import com.example.demo.repository.SenderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor

@Component
public class ParcelsProjection {  //instead of ParcelsEventsHandler
    private final PackageRepository packageRepository;
    private final ReceiverRepository receiverRepository;
    private final SenderRepository senderRepository;

    // TODO: update Package in DB with different state in each step

    @EventHandler
    public void on(ParcelCreatedEvent parcelCreatedEvent){
        log.debug("[ParcelsProjection(Events Handler)][ParcelCreatedEvent] saving parcel:" + parcelCreatedEvent.getParcel());
        packageRepository.save(parcelCreatedEvent.getParcel()).subscribe(parcel ->
            log.debug("[ParcelsProjection(Events Handler)][ParcelCreatedEvent] saved parcel:" + parcel)
        );
    }

    @EventHandler
    public void on(DeliveryErrorEvent deliveryErrorEvent){
        log.debug("[ParcelsProjection(Events Handler)][DeliveryErrorEvent] parcel UUID:" + deliveryErrorEvent.getParcelPublicId());

        packageRepository.findByPublicId(deliveryErrorEvent.getParcelPublicId())
                .flatMap(parcel ->  Mono.zip(
                                        packageRepository.findByPublicId(parcel.getPublicId()),
                                        senderRepository.findById(parcel.getSenderId()),
                                        receiverRepository.findById(parcel.getReceiverId())
                                        )
                )
                .map(objects -> {
                    Package parcel = objects.getT1();
                    // to not return to original receiver (!)
                    if(!parcel.isReturn()){
                        // change sides to return
                        Sender s = objects.getT2();
                        Receiver r = objects.getT3();

                        Sender s2= new Sender(null, r.getFirstName(), r.getLastName(), r.getCompany(), r.getPhoneNumber(), r.getEmail(), r.getAddress());
                        Receiver r2= new Receiver(null, s.getFirstName(), s.getLastName(), s.getCompany(), s.getPhoneNumber(), s.getEmail(), s.getAddress());
                        parcel.setSender(s2);
                        parcel.setReceiver(r2);

                        // mark as return package
                        parcel.setReturn(true);
                    }
                    return objects.getT1();
                })
                .flatMap(packageRepository::save)
                .subscribe(parcel -> log.debug("[ParcelsProjection(Events Handler)][DeliveryErrorEvent] parcel updated & saved: " + parcel));
    }

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