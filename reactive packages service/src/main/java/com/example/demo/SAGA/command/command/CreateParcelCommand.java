package com.example.demo.SAGA.command.command;

import com.example.demo.domain.Package;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
public class CreateParcelCommand {
    @TargetAggregateIdentifier
    // this identifier is used to associate command with an aggregate object in application.
    private final UUID parcelPublicId;
    private final Package parcel;
}