package com.example.demo.SAGA.command.command;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DisableParcelCommand {
    @TargetAggregateIdentifier
    // this identifier is used to associate command with an aggregate object in application.
    private UUID parcelPublicId;

}