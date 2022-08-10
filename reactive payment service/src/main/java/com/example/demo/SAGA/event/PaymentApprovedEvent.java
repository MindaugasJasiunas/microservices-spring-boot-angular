package com.example.demo.SAGA.event;

import com.example.demo.domain.Package;

import java.util.UUID;

public class PaymentApprovedEvent {  //what it needs to be done when this event object is consumed
    private UUID parcelPublicId;

    public PaymentApprovedEvent(UUID parcelPublicId) {
        this.parcelPublicId = parcelPublicId;
    }

    public UUID getParcelPublicId() {
        return parcelPublicId;
    }
}