package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.logging.Level;
import java.util.logging.Logger;

@EnableDiscoveryClient
@SpringBootApplication
public class DemoApplication {
	private static final Logger LOGGER = Logger.getLogger( DemoApplication.class.getName() );

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private ReactiveWebServerApplicationContext server;

	@Bean
	CommandLineRunner start(){
		return args -> {
			LOGGER.log(Level.FINE, "------------ PORT: "+ server.getWebServer().getPort() + " ------------");
			LOGGER.log(Level.FINE, "------------ PORT: "+ server.getWebServer().getPort() + " ------------");
			LOGGER.log(Level.FINE, "------------ PORT: "+ server.getWebServer().getPort() + " ------------");
		};
	}

}
