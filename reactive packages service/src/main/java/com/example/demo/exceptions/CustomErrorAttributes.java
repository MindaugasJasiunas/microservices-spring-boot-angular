package com.example.demo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryExecutionException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributesMap = super.getErrorAttributes(request, options);
        Throwable throwable = getError(request);

        log.debug("[PackagesService][ERROR] Error occurred. Throwable: " + throwable);

        if(throwable instanceof ResponseStatusException){
            ResponseStatusException ex = (ResponseStatusException) throwable;
            errorAttributesMap.put("message", ex.getMessage());
            //errorAttributesMap.put("customAttribute", "customAttributeValue");
        }
        if(throwable instanceof QueryExecutionException && throwable.getMessage().contains("The given id must not be null!")){
            errorAttributesMap.put("message", "Server Error. Check your request and try again.");
        }

        errorAttributesMap.putIfAbsent("message", throwable.getMessage());

        return errorAttributesMap;
    }
}
