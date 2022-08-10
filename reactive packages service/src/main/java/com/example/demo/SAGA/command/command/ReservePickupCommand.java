package com.example.demo.SAGA.command.command;

import com.example.demo.domain.Package;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservePickupCommand {
    @TargetAggregateIdentifier
    private UUID parcelPublicId;
}