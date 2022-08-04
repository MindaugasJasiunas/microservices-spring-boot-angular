package com.example.demo.exceptions;

import lombok.extern.slf4j.Slf4j;
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

        log.debug("[PackagesService][ERROR] Error occured. Throwable: " + throwable);

        if(throwable instanceof ResponseStatusException){
            ResponseStatusException ex = (ResponseStatusException) throwable;
            errorAttributesMap.put("message", ex.getMessage());
            //errorAttributesMap.put("customAttribute", "customAttributeValue");
        }

        errorAttributesMap.putIfAbsent("message", throwable.getMessage());

        return errorAttributesMap;
    }
}
