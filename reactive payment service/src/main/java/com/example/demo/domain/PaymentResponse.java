package com.example.demo.domain;

import java.util.UUID;

public class PaymentResponse {
    private UUID parcelPublicId;
    private String payment;
    private String currency;

    public PaymentResponse(UUID parcelPublicId, String payment, String currency) {
        this.parcelPublicId = parcelPublicId;
        this.payment = payment;
        this.currency = currency;
    }

    // getters for Jackson serializer

    public UUID getParcelPublicId() {
        return parcelPublicId;
    }

    public String getPayment() {
        return payment;
    }

    public String getCurrency() {
        return currency;
    }
}
