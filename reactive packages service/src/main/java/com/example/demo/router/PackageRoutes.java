package com.example.demo.router;

import com.example.demo.handler.PackageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class PackageRoutes {
    @Bean
    public RouterFunction<ServerResponse> authRoutes(PackageHandler packageHandler){
        return RouterFunctions.route()
                .GET("/tracking/{trackingNumber}", accept(MediaType.APPLICATION_JSON), packageHandler::trackPackage)
                .GET("/list", accept(APPLICATION_JSON), packageHandler::listPackages)
                .POST("/submitNewPackage", accept(APPLICATION_JSON), packageHandler::createPackage)
                .build();
    }
}
