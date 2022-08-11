package com.example.demo.SAGA.command;

import com.example.demo.SAGA.command.command.*;
import com.example.demo.SAGA.event.*;
import com.example.demo.domain.Package;
import com.example.demo.domain.PackageState;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.service.PackageService;
import com.example.demo.util.validator.PackageValidator;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.extensions.reactor.eventhandling.gateway.ReactorEventGateway;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

import static com.example.demo.util.validator.PackageValidator.*;
import static com.example.demo.util.validator.PackageValidator.isReceiverPhoneNumberValid;


@Slf4j

@Aggregate
public class ParcelAggregate {
    private static final long serialVersionUID = -4137564316551265019L;

    @AggregateIdentifier //unique Product aggregate. It helps Axon to associate CreateParcelCommand with the aggregate (in command we have @TargetAggregateIdentifier. this annotations help axon framework associate dispatch command with right aggregate)
    private UUID parcelPublicId;
    private Package parcel;
    //@Autowired
    private PackageService packageService;
    //@Autowired // autowired fields is still NULL
    private ReactorEventGateway reactiveEventGateway;
//    private EventGateway eventGateway;

    protected ParcelAggregate() {
        // no args constructor required by Axon Framework. Otherwise: CommandExecutionException: The aggregate was not found in the event store
    }

    @CommandHandler
    public ParcelAggregate(CreateParcelCommand command, PackageService packageService, ReactorEventGateway reactiveEventGateway) {
        this.packageService = packageService;
        this.reactiveEventGateway = reactiveEventGateway;

        log.debug("[ParcelAggregate] CreateParcelCommand called");
        //Second constructor will be used as a Command Handler method. Invoked when CreateParcelCommand is dispatched.
        //(very first command that creates new package will create instance of aggregate as well)

        // validation
        PackageValidator.ValidationResult result = packageService.isPackageValid(command.getParcel());
        if(result != PackageValidator.ValidationResult.SUCCESS){
            log.error("[ERROR][PackageServiceImpl][validateAndPopulateNewPackage]: Package is invalid: "+result.name());
            throw new BadRequestException("Package is invalid: "+result.name());
        }

        // logic & publishing of event
        packageService.populateNewPackage(command.getParcel())
                .subscribe(populatedParcel -> {
                    ParcelCreatedEvent event = new ParcelCreatedEvent(command.getParcelPublicId(), populatedParcel);
                    // BeanUtils.copyProperties(command, event);  //copy properties between objects

                    // publish event from Aggregate
                    //AggregateLifecycle.apply(event);  //event dispatched & @EventSourcingHandler will be called first(aggregate state will be updated & then product event will be scheduled for publication to other event handler and then persisted to the Event Store
                    // publish event from another component
                    //eventGateway.publish(event);
                    // publish event from another component reactive way (support return types such as Flux)
                    log.debug("ParcelCreatedEvent published to reactiveEventGateway. Parcel: "+populatedParcel);
                    reactiveEventGateway.publish(event).subscribe();
                });
    }

    //event sourcing handler method (typically with very short name of 2 characters - on)
    @EventSourcingHandler
    public void on(ParcelCreatedEvent parcelCreatedEvent){
        // used to initialize current state of aggregate with latest information (add product related properties at the top of the class)
        // avoid adding any business logic inside this method. Use method only to update aggregate state.

        // initialize fields
        this.parcelPublicId = parcelCreatedEvent.getParcelPublicId();
        this.parcel = parcelCreatedEvent.getParcel();
    }


    @CommandHandler
    public ParcelAggregate(ProcessPaymentCommand command, PackageService packageService, ReactorEventGateway reactiveEventGateway) {  //    public void handle(ProcessPaymentCommand command){  // 'CommandExecutionException: The aggregate was not found in the event store'
        this.packageService = packageService;
        this.reactiveEventGateway = reactiveEventGateway;

        log.debug("[ParcelAggregate] ProcessPaymentCommand called");

        Package parcel = command.getParcel();
        parcel.setPackageStatus(PackageState.PAYMENT_PENDING);

        // (!) send HTTP with parcel to payment service
        packageService.sendPaymentHTTP(parcel).subscribe();

        PaymentPendingEvent event = new PaymentPendingEvent(command.getParcelPublicId(), parcel);

        log.debug("PaymentPendingEvent published to reactiveEventGateway. Parcel: "+event.getParcel());
        reactiveEventGateway.publish(event).subscribe();
    }

    @EventSourcingHandler
    public void on(PaymentPendingEvent paymentPendingEvent){
        // initialize fields
        this.parcelPublicId = paymentPendingEvent.getParcelPublicId();
        this.parcel = paymentPendingEvent.getParcel();
    }


    @CommandHandler
    public ParcelAggregate(PaymentApprovalCommand command, ReactorEventGateway reactiveEventGateway) { // public void handle(PaymentApprovalCommand command){ - not working
        log.debug("[ParcelAggregate] PaymentApprovalCommand called with parcel public ID: "+command.getParcelPublicId());

        // send new event to saga PaymentApprovedEvent
        PaymentApprovedEvent event = new PaymentApprovedEvent(command.getParcelPublicId());
        log.debug("PaymentApprovedEvent published to reactiveEventGateway.");
        reactiveEventGateway.publish(event).subscribe();
    }

    @CommandHandler
    public ParcelAggregate(PaymentRejectionCommand command, ReactorEventGateway reactiveEventGateway) {
        log.debug("[ParcelAggregate] PaymentRejectionCommand called with parcel public ID: "+command.getParcelPublicId());

        // send new event to saga PaymentRejectedEvent
        PaymentRejectedEvent event = new PaymentRejectedEvent(command.getParcelPublicId());
        log.debug("PaymentRejectedEvent published to reactiveEventGateway.");
        reactiveEventGateway.publish(event).subscribe();
    }

    @CommandHandler
    public ParcelAggregate(DisableParcelCommand command, ReactorEventGateway reactiveEventGateway) {
        log.debug("[ParcelAggregate] DisableParcelCommand called: Parcel UUID: "+command.getParcelPublicId());

        ParcelDisabledEvent event = new ParcelDisabledEvent(command.getParcelPublicId());
        log.debug("ParcelDisabledEvent published to reactiveEventGateway.");
        reactiveEventGateway.publish(event).subscribe();
    }

    @CommandHandler
    public ParcelAggregate(ReservePickupCommand command, PackageService packageService, ReactorEventGateway reactiveEventGateway) {
        log.debug("[ParcelAggregate] ReservePickupCommand called");

        // send HTTP to Delivery Service to save new pickup by parcel.
        packageService.sendDeliveryHTTP(command.getParcelPublicId()).subscribe();

        PickupConfirmationPendingEvent event = new PickupConfirmationPendingEvent(command.getParcelPublicId());
        log.debug("PickupConfirmationPendingEvent published to reactiveEventGateway.");
        reactiveEventGateway.publish(event).subscribe();
    }

    @CommandHandler
    public ParcelAggregate(PickupAssignedCommand command, ReactorEventGateway reactiveEventGateway) {
        log.debug("[ParcelAggregate] PickupAssignedCommand called: Parcel UUID: "+command.getParcelPublicId()+", pickup time: "+command.getPickupDateAndTime());

        // TODO: send HTTP to email service(create service too) to inform sender about pickup time

        PickupPendingEvent event = new PickupPendingEvent(command.getParcelPublicId());
        log.debug("PickupPendingEvent published to reactiveEventGateway.");
        reactiveEventGateway.publish(event).subscribe();
    }

    @CommandHandler
    public ParcelAggregate(InTransitCommand command, ReactorEventGateway reactiveEventGateway) {
        log.debug("[ParcelAggregate] InTransitCommand called: Parcel UUID: "+command.getParcelPublicId());

        // TODO: send HTTP to email service(create service too) to inform receiver about delivery time

        InTransitEvent event = new InTransitEvent(command.getParcelPublicId());
        log.debug("InTransitEvent published to reactiveEventGateway.");
        reactiveEventGateway.publish(event).subscribe();
    }

    @CommandHandler
    public ParcelAggregate(DeliveryErrorCommand command, ReactorEventGateway reactiveEventGateway) {
        log.debug("[ParcelAggregate] DeliveryErrorCommand called: Parcel UUID: "+command.getParcelPublicId());

        DeliveryErrorEvent event = new DeliveryErrorEvent(command.getParcelPublicId());
        log.debug("DeliveryErrorEvent published to reactiveEventGateway.");
        reactiveEventGateway.publish(event).subscribe();
    }

    @CommandHandler
    public ParcelAggregate(ReturnToSenderCommand command, PackageService packageService, ReactorEventGateway reactiveEventGateway) {
        log.debug("[ParcelAggregate] ReturnToSenderCommand called: Parcel UUID: "+command.getParcelPublicId());

        // ParcelsProjection shifted sender & receiver in places, now we can just send it

        // TODO: send HTTP to email service(create service too) to inform sender about pickup time

        // send HTTP to Delivery Service to save new pickup by parcel.
        packageService.sendDeliveryHTTP(command.getParcelPublicId()).subscribe();

        PickupConfirmationPendingEvent event = new PickupConfirmationPendingEvent(command.getParcelPublicId());
        log.debug("PickupConfirmationPendingEvent published to reactiveEventGateway.");
        reactiveEventGateway.publish(event).subscribe();
    }

    @CommandHandler
    public ParcelAggregate(DeliveredCommand command, ReactorEventGateway reactiveEventGateway) {
        log.debug("[ParcelAggregate] DeliveredCommand called: Parcel UUID: "+command.getParcelPublicId());

        // TODO: archive parcel (add to archived parcels, delete from current parcels DB)

        ParcelFinishedEvent event = new ParcelFinishedEvent(command.getParcelPublicId());
        log.debug("ParcelFinishedEvent published to reactiveEventGateway - for Saga to be finished.");
        reactiveEventGateway.publish(event).subscribe();
    }

}
