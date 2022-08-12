package com.example.demo.SAGA.command.command;

import com.example.demo.domain.Package;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcessPaymentCommand {
    @TargetAggregateIdentifier // this identifier is used to associate command with an aggregate object in application.
    private UUID parcelPublicId;
    private Package parcel;
}