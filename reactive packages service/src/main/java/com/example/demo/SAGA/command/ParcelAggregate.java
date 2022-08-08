package com.example.demo.SAGA.command;

import com.example.demo.SAGA.command.command.CreateParcelCommand;
import com.example.demo.SAGA.event.ParcelCreatedEvent;
import com.example.demo.domain.Package;
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

import java.util.UUID;

import static com.example.demo.util.validator.PackageValidator.*;
import static com.example.demo.util.validator.PackageValidator.isReceiverPhoneNumberValid;


@Slf4j

@Aggregate
public class ParcelAggregate {
    @AggregateIdentifier //unique Product aggregate. It helps Axon to associate CreateParcelCommand with the aggregate (in command we have @TargetAggregateIdentifier. this annotations help axon framework associate dispatch command with right aggregate)
    private UUID parcelPublicId;
    private Package parcel;
    private final PackageService packageService;

//    private EventGateway eventGateway;
    private final ReactorEventGateway reactiveEventGateway;


    public ParcelAggregate(PackageService packageService, ReactorEventGateway reactiveEventGateway) {
        // no args(more like no commands) constructor required by Axon Framework
        this.packageService = packageService;
        this.reactiveEventGateway = reactiveEventGateway;
    }

    @CommandHandler
    public ParcelAggregate(CreateParcelCommand command, PackageService packageService, ReactorEventGateway reactiveEventGateway) {
        this.packageService = packageService;
        this.reactiveEventGateway = reactiveEventGateway;
        //Second constructor will be used as a Command Handler method. Invoked when CreateParcelCommand is dispatched.
        //(very first command that creates new package will create instance of aggregate as well)

        // validation
        PackageValidator.ValidationResult result = PackageValidator
                .isPackageExists()
                .and(isNumberOfPackagesValid())
                .and(isPackagesWeightValid())
                .and(isSenderExists())
                .and(isSenderAddressValid())
                .and(isSenderFirstNameValid())
                .and(isSenderLastNameValid())
                .and(isSenderPhoneNumberValid())
                .and(isReceiverExists())
                .and(isReceiverAddressValid())
                .and(isReceiverFirstNameValid())
                .and(isReceiverLastNameValid())
                .and(isReceiverPhoneNumberValid())
                .apply(command.getParcel());
        if(result != PackageValidator.ValidationResult.SUCCESS){
            log.error("[ERROR][PackageServiceImpl][validateAndPopulateNewPackage]: Package is invalid: "+result.name());
            throw new BadRequestException("Package is invalid: "+result.name());
        }

        // logic & publishing of event
        packageService.populateNewPackage(command.getParcel())
                .subscribe(aPackage -> {
                    //create new instance of ProductCreatedEvent
                    ParcelCreatedEvent event = new ParcelCreatedEvent();
                    BeanUtils.copyProperties(command, event);  //copy properties between objects

                    // publish event from Aggregate
                    //AggregateLifecycle.apply(event);  //event dispatched & @EventSourcingHandler will be called first(aggregate state will be updated & then product event will be scheduled for publication to other event handler and then persisted to the Event Store
                    // publish event from another component
                    //eventGateway.publish(event);
                    // publish event from another component reactive way (support return types such as Flux)
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

}
