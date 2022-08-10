package com.example.demo.SAGA.command.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public class PaymentRejectionCommand {
    @TargetAggregateIdentifier
    // this identifier is used to associate command with an aggregate object in application.
    private UUID parcelPublicId;

    //    public PaymentApprovalCommand() {}

    public PaymentRejectionCommand(UUID parcelPublicId) {
        this.parcelPublicId = parcelPublicId;
    }

    public void setParcelPublicId(UUID parcelPublicId) {
        this.parcelPublicId = parcelPublicId;
    }

    public UUID getParcelPublicId() {
        return parcelPublicId;
    }
}