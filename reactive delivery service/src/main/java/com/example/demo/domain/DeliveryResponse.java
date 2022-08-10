package com.example.demo.domain;

import java.util.UUID;

public class DeliveryResponse {
    private UUID parcelPublicId;
    private String registerStatus;

    public DeliveryResponse() {
    }

    public DeliveryResponse(UUID parcelPublicId, String registerStatus) {
        this.parcelPublicId = parcelPublicId;
        this.registerStatus = registerStatus;
    }

    public UUID getParcelPublicId() {
        return parcelPublicId;
    }

    public void setParcelPublicId(UUID parcelPublicId) {
        this.parcelPublicId = parcelPublicId;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }
}
