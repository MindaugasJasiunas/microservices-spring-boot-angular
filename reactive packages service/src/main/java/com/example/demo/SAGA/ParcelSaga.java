package com.example.demo.SAGA;

import com.example.demo.SAGA.command.command.DisableParcelCommand;
import com.example.demo.SAGA.command.command.ProcessPaymentCommand;
import com.example.demo.SAGA.command.command.ReservePickupCommand;
import com.example.demo.SAGA.command.command.ReturnToSenderCommand;
import com.example.demo.SAGA.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
//@RequiredArgsConstructor // An error occurred while attempting to create a new managed instance. Caused by: java.lang.NoSuchMethodException: com.example.demo.SAGA.ParcelSaga.<init>()

@Saga
public class ParcelSaga {
    @Autowired
    private transient ReactorCommandGateway reactiveCommandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "parcelPublicId")
    public void handle(ParcelCreatedEvent event){

        log.debug("[SAGA:START] ParcelCreatedEvent received. Issuing ProcessPaymentCommand.");
        // create & issue ProcessPaymentCommand
        ProcessPaymentCommand command = new ProcessPaymentCommand(event.getParcelPublicId(), event.getParcel());

        //now send to Command Bus for further processing
        reactiveCommandGateway.send(command).subscribe(); // send-and-forget pattern
    }

    @SagaEventHandler(associationProperty = "parcelPublicId")
    public void handle(PaymentPendingEvent event){
        log.debug("[SAGA] PaymentPendingEvent received. Doing nothing.");
    }

    @SagaEventHandler(associationProperty = "parcelPublicId")
    public void handle(PaymentApprovedEvent event){
        log.debug("[SAGA] PaymentApprovedEvent received. Issuing ReservePickupCommand.");

        // create & issue ReservePickupCommand
         ReservePickupCommand command = new ReservePickupCommand(event.getParcelPublicId());

        //now send to Command Bus for further processing
        reactiveCommandGateway.send(command).subscribe(); // send-and-forget pattern
    }

    @SagaEventHandler(associationProperty = "parcelPublicId")
    public void handle(PaymentRejectedEvent event){
        log.debug("[SAGA] PaymentRejectedEvent received. Issuing ... (Compensating Transaction)");
        // COMPENSATING TRANSACTION (could make whole chain of compensating transactions but this one is simple)
        // create & issue DisableParcelCommand
        DisableParcelCommand command = new DisableParcelCommand(event.getParcelPublicId());

        //now send to Command Bus for further processing
        reactiveCommandGateway.send(command).subscribe(); // send-and-forget pattern
    }

    @SagaEventHandler(associationProperty = "parcelPublicId")
    public void handle(ParcelDisabledEvent event){
        log.debug("[SAGA] ParcelDisabledEvent received. Doing nothing.");
        // wait for sender to make a payment
    }

    @SagaEventHandler(associationProperty = "parcelPublicId")
    public void handle(PickupConfirmationPendingEvent event){
        log.debug("[SAGA] PickupConfirmationPendingEvent received. Doing nothing.");
    }

    @SagaEventHandler(associationProperty = "parcelPublicId")
    public void handle(PickupPendingEvent event){
        log.debug("[SAGA] PickupPendingEvent received. Doing nothing.");
    }

    @SagaEventHandler(associationProperty = "parcelPublicId")
    public void handle(InTransitEvent event){
        log.debug("[SAGA] InTransitEvent received. Doing nothing.");
    }

    @SagaEventHandler(associationProperty = "parcelPublicId")
    public void handle(DeliveryErrorEvent event){
        log.debug("[SAGA] DeliveryErrorEvent received. Issuing ... (Compensating Transaction)");
        // COMPENSATING TRANSACTION
        // create & issue ReturnToSenderCommand
        ReturnToSenderCommand command = new ReturnToSenderCommand(event.getParcelPublicId());

        //now send to Command Bus for further processing
        reactiveCommandGateway.send(command).subscribe(); // send-and-forget pattern
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "parcelPublicId")
    public void handle(ParcelFinishedEvent event){
        log.debug("[SAGA:END] ParcelFinishedEvent");
    }

}
