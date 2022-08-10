package com.example.demo.SAGA.event;

import java.util.UUID;

public class PaymentRejectedEvent {  //what it needs to be done when this event object is consumed
    private UUID parcelPublicId;

    public PaymentRejectedEvent(UUID parcelPublicId) {
        this.parcelPublicId = parcelPublicId;
    }

    public UUID getParcelPublicId() {
        return parcelPublicId;
    }
}