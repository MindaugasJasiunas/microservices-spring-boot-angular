package com.example.demo.SAGA.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelDisabledEvent {  //what it needs to be done when this event object is consumed
    private UUID parcelPublicId;
}