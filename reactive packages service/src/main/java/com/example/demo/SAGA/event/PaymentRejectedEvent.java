package com.example.demo.SAGA.event;

import com.example.demo.domain.Package;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRejectedEvent {  //what it needs to be done when this event object is consumed
    private UUID parcelPublicId;
}