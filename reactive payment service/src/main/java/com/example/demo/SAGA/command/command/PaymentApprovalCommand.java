package com.example.demo.SAGA.command.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public class PaymentApprovalCommand {
    @TargetAggregateIdentifier
    // this identifier is used to associate command with an aggregate object in application.
    private UUID parcelPublicId;

    //    public PaymentApprovalCommand() {}

    public PaymentApprovalCommand(UUID parcelPublicId) {
        this.parcelPublicId = parcelPublicId;
    }

    public void setParcelPublicId(UUID parcelPublicId) {
        this.parcelPublicId = parcelPublicId;
    }

    public UUID getParcelPublicId() {
        return parcelPublicId;
    }
}