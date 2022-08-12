package com.example.demo.SAGA.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParcelFinishedEvent {  //what it needs to be done when this event object is consumed
    private UUID parcelPublicId;
}