package com.example.demo.domain;

public enum PackageState {
    NEW,
    INFO_RECEIVED, INFO_RECEIVED_ERROR,
    COLLECTED, ERROR_COLLECTING, SENDER_NOT_PRESENT_ERROR, PARCEL_NOT_READY_FOR_SHIPPING_ERROR, MORE_PACKAGES_RECEIVED_ERROR,
    IN_TRANSIT, ERROR_IN_TRANSIT,
    INBOUND, ERROR_INBOUND,
    OUT_FOR_DELIVERY, OUT_FOR_DELIVERY_ERROR, WRONG_ADDRESS_ERROR, RETURN_TO_SENDER,
    DELIVERED, DELIVERY_ERROR
}
