package com.example.demo.controller;

import com.example.demo.SAGA.command.command.PaymentApprovalCommand;
import com.example.demo.SAGA.command.command.PaymentRejectionCommand;
import com.example.demo.SAGA.event.PaymentRejectedEvent;
import com.example.demo.domain.Package;
import com.example.demo.domain.PaymentResponse;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

// for small mocked service it's sufficient to have handler & routes inline
@Configuration
public class PaymentHandlerAndRoutesInline {
    Logger logger = Logger.getLogger(PaymentHandlerAndRoutesInline.class.getName());
    private final ReactorCommandGateway reactiveCommandGateway;

    public PaymentHandlerAndRoutesInline(ReactorCommandGateway reactiveCommandGateway) {
        this.reactiveCommandGateway = reactiveCommandGateway;
    }

    @Bean
    RouterFunction<ServerResponse> composedRoutes() {
        return
                RouterFunctions.route()
                    .GET("payment/{parcelPublicId}", accept(APPLICATION_JSON),
                        request -> {
                            String parcelPublicId = request.pathVariable("parcelPublicId");
                            logger.log(Level.FINE, "[PaymentService] getPayment( " + parcelPublicId + " )");

                            return Mono.just(new PaymentResponse(UUID.fromString(parcelPublicId), "19.99", "EUR"))
                                    .flatMap(paymentResponse -> ServerResponse.ok().bodyValue(paymentResponse))
                                    .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
                    })

                    .POST("payment", accept(APPLICATION_JSON),
                        request -> {
                            Mono<Package> parcelRequest = request.bodyToMono(Package.class);

                            return parcelRequest
                                    .flatMap(aPackage -> {
                                        logger.log(Level.FINE, "[PaymentService] createPayment( " + aPackage + " )");

                                        // TODO: call service to process payment by given parcel, save new payment to DB and return payment response
                                        PaymentResponse paymentResponse = new PaymentResponse(aPackage.getPublicId(), "29.99", "EUR");
                                        return ServerResponse.ok().bodyValue(paymentResponse);
                                    })
                                    .switchIfEmpty(ServerResponse.status(HttpStatus.BAD_REQUEST).build());
                    })

                    .GET("payment/approve/{parcelPublicId}", accept(APPLICATION_JSON),
                        request -> {
                            String parcelPublicId = request.pathVariable("parcelPublicId");
                            logger.log(Level.FINE, "[PaymentService] approvePayment( " + parcelPublicId + " )");

                            // send PaymentApprovalCommand to handle in Aggregate & to publish PaymentApprovedEvent to be handled in @Saga.
                            PaymentApprovalCommand command = new PaymentApprovalCommand(UUID.fromString(parcelPublicId));
                            reactiveCommandGateway.send(command).subscribe(); //.flatMap(unused -> ServerResponse.ok().bodyValue("Payment successful for parcel with public Id: "+parcelPublicId));
                            logger.log(Level.FINE, "[PaymentService] approvePayment( " + parcelPublicId + " ) [PaymentApprovalCommand sent]");
                            return ServerResponse.ok().contentType(APPLICATION_JSON).body(Mono.just("Payment successful for parcel with public Id: "+parcelPublicId), String.class);
                    })

                    .GET("payment/decline/{parcelPublicId}", accept(APPLICATION_JSON),
                        request -> {
                            String parcelPublicId = request.pathVariable("parcelPublicId");
                            logger.log(Level.FINE, "[PaymentService] declinePayment( " + parcelPublicId + " )");

                            // send PaymentRejectionCommand to handle in Aggregate & to publish PaymentRejectedEvent to be handled in @Saga.
                            PaymentRejectionCommand command = new PaymentRejectionCommand(UUID.fromString(parcelPublicId));
                            reactiveCommandGateway.send(command).subscribe(); //.flatMap(eventMessage -> ServerResponse.ok().bodyValue("Payment declined for parcel with public Id: "+parcelPublicId));
                            logger.log(Level.FINE, "[PaymentService] declinePayment( " + parcelPublicId + " ) [PaymentRejectionCommand sent]");
                            return ServerResponse.ok().bodyValue("Payment declined for parcel with public Id: "+parcelPublicId);
                    })
                    .build();
    }
}