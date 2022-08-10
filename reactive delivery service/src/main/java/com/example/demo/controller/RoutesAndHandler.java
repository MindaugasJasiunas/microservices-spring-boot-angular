package com.example.demo.controller;

import com.example.demo.SAGA.command.command.DeliveredCommand;
import com.example.demo.SAGA.command.command.DeliveryErrorCommand;
import com.example.demo.SAGA.command.command.InTransitCommand;
import com.example.demo.SAGA.command.command.PickupAssignedCommand;
import com.example.demo.domain.DeliveryResponse;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

// for small mocked service it's sufficient to have handler & routes inline
@Configuration
public class RoutesAndHandler {
    Logger logger = Logger.getLogger(RoutesAndHandler.class.getName());
    private final ReactorCommandGateway reactiveCommandGateway;

    public RoutesAndHandler(ReactorCommandGateway reactiveCommandGateway) {
        this.reactiveCommandGateway = reactiveCommandGateway;
    }

    @Bean
    RouterFunction<ServerResponse> composedDeliveryRoutes() {
        return
                RouterFunctions.route()
                        .GET("new-delivery/{parcelPublicId}", accept(APPLICATION_JSON),
                            request -> {
                                String parcelPublicId = request.pathVariable("parcelPublicId");
                                logger.log(Level.FINE, "[DeliveryService] register new parcel to pickup. Parcel ID:" + parcelPublicId + "");

                                return Mono.just(new DeliveryResponse(UUID.fromString(parcelPublicId), "NEW PICKUP REGISTERED"))
                                        .flatMap(paymentResponse -> ServerResponse.ok().bodyValue(paymentResponse))
                                        .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
                            }
                        )
                        .GET("assign/{parcelPublicId}", accept(APPLICATION_JSON),
                                request -> {
                                    String parcelPublicId = request.pathVariable("parcelPublicId");
                                    logger.log(Level.FINE, "[DeliveryService] Pickup assigning to Parcel ID:" + parcelPublicId + "");

                                    // send PickupAssignedCommand to handle in Aggregate & to publish PickupPendingEvent to be handled in @Saga.
                                    LocalDateTime pickup = LocalDateTime.now().plusDays(2);
                                    PickupAssignedCommand command = new PickupAssignedCommand(UUID.fromString(parcelPublicId), pickup);
                                    reactiveCommandGateway.send(command).subscribe();
                                    logger.log(Level.FINE, "[PaymentService] assignPickup( " + parcelPublicId + " ) [PickupAssignedCommand sent]");
                                    return ServerResponse.ok().bodyValue("Pickup assigned for parcel with public Id: "+parcelPublicId+" to: "+pickup);
                                }
                        )
                        .GET("picked-up/{parcelPublicId}", accept(APPLICATION_JSON),
                                request -> {
                                    String parcelPublicId = request.pathVariable("parcelPublicId");
                                    logger.log(Level.FINE, "[DeliveryService] Parcel has been picked up and now is in transit. Parcel UUID:" + parcelPublicId);

                                    // send InTransitCommand to handle in Aggregate & to publish InTransitEvent to be handled in @Saga.
                                    InTransitCommand command = new InTransitCommand(UUID.fromString(parcelPublicId));
                                    reactiveCommandGateway.send(command).subscribe();
                                    logger.log(Level.FINE, "[PaymentService] picked up( " + parcelPublicId + " ) [InTransitCommand sent]");
                                    return ServerResponse.ok().bodyValue("Parcel has been picked up and now is IN TRANSIT. Parcel UUID: "+ parcelPublicId);
                                }
                        )
                        .GET("delivered/{parcelPublicId}", accept(APPLICATION_JSON),
                                request -> {
                                    String parcelPublicId = request.pathVariable("parcelPublicId");
                                    logger.log(Level.FINE, "[DeliveryService] Parcel delivered! Parcel UUID:" + parcelPublicId);

                                    // send DeliveredCommand to handle in Aggregate & to publish DeliveredEvent to be handled in @Saga.
                                    DeliveredCommand command = new DeliveredCommand(UUID.fromString(parcelPublicId));
                                    reactiveCommandGateway.send(command).subscribe();
                                    logger.log(Level.FINE, "[PaymentService] delivered( " + parcelPublicId + " ) [DeliveredCommand sent]");
                                    return ServerResponse.ok().bodyValue("Parcel Delivered! Parcel UUID: "+ parcelPublicId);
                                }
                        )
                        .GET("delivery-error/{parcelPublicId}", accept(APPLICATION_JSON),
                                request -> {
                                    String parcelPublicId = request.pathVariable("parcelPublicId");
                                    logger.log(Level.FINE, "[DeliveryService] Delivery ERROR! Parcel UUID:" + parcelPublicId);

                                    // send DeliveryErrorCommand to handle in Aggregate & to publish DeliveryErrorEvent to be handled in @Saga.
                                    DeliveryErrorCommand command = new DeliveryErrorCommand(UUID.fromString(parcelPublicId));
                                    reactiveCommandGateway.send(command).subscribe();
                                    logger.log(Level.FINE, "[PaymentService] delivery error( " + parcelPublicId + " ) [DeliveryErrorCommand sent]");
                                    return ServerResponse.ok().bodyValue("Delivery ERROR! Parcel UUID: "+ parcelPublicId);
                                }
                        )
                        .build();
    }
}
