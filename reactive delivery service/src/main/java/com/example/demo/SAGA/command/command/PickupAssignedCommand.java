package com.example.demo.SAGA.command.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;
import java.util.UUID;

public class PickupAssignedCommand {
    @TargetAggregateIdentifier
    // this identifier is used to associate command with an aggregate object in application.
    private UUID parcelPublicId;
    private LocalDateTime pickupDateAndTime;

    public PickupAssignedCommand() {
    }

    public PickupAssignedCommand(UUID parcelPublicId, LocalDateTime pickupDateAndTime) {
        this.parcelPublicId = parcelPublicId;
        this.pickupDateAndTime = pickupDateAndTime;
    }

    public UUID getParcelPublicId() {
        return parcelPublicId;
    }

    public void setParcelPublicId(UUID parcelPublicId) {
        this.parcelPublicId = parcelPublicId;
    }

    public LocalDateTime getPickupDateAndTime() {
        return pickupDateAndTime;
    }

    public void setPickupDateAndTime(LocalDateTime pickupDateAndTime) {
        this.pickupDateAndTime = pickupDateAndTime;
    }
}
