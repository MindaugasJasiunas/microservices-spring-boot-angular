package com.example.demo.SAGA.event;

import com.example.demo.domain.Package;
import lombok.Data;

import java.util.UUID;

@Data
public class ParcelCreatedEvent {  //what it needs to be done when this event object is consumed
    private UUID parcelPublicId;
    private Package parcel;
}