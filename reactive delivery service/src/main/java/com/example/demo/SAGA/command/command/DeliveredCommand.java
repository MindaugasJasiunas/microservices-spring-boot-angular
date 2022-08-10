package com.example.demo.SAGA.command.command;


import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public class DeliveredCommand {
    @TargetAggregateIdentifier
    // this identifier is used to associate command with an aggregate object in application.
    private UUID parcelPublicId;

    public DeliveredCommand() {
    }

    public DeliveredCommand(UUID parcelPublicId) {
        this.parcelPublicId = parcelPublicId;
    }

    public UUID getParcelPublicId() {
        return parcelPublicId;
    }

    public void setParcelPublicId(UUID parcelPublicId) {
        this.parcelPublicId = parcelPublicId;
    }
}