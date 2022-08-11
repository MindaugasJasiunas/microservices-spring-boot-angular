package com.example.demo.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// OpenFeign is not compatible with WebFlux. If you use WebFlux you have to provide your own Encoder and Decoder
@Configuration
public class FeignConfiguration{

    // with this - no more auto-configuration (auto-config needed for custom pageRequestDeserializer deserializer to pickup(otherwise list packages not working))
    // for feign error: throws Java 8 date/time type `java.time.LocalDateTime` not supported by default
    /*@Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
     // objectMapper.findAndRegisterModules();

        objectMapper
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        return objectMapper;
    }*/

    @Bean
    public Decoder decoder() {
        return new JacksonDecoder(new ObjectMapper());
    }

    @Bean
    public Encoder encoder() {
        return new JacksonEncoder(new ObjectMapper());
    }
}
