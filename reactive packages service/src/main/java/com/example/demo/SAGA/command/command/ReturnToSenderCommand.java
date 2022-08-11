package com.example.demo.SAGA.command.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReturnToSenderCommand {
    @TargetAggregateIdentifier
    private UUID parcelPublicId;
}