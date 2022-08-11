package com.example.demo;

import com.example.demo.util.json.PageRequestDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@Slf4j

//@EnableFeignClients
@EnableReactiveFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private ReactiveWebServerApplicationContext server;

	@Bean
	CommandLineRunner start(){
		return args -> {
			log.debug("------------ PORT: "+ server.getWebServer().getPort() + " ------------");
			log.debug("------------ PORT: "+ server.getWebServer().getPort() + " ------------");
			log.debug("------------ PORT: "+ server.getWebServer().getPort() + " ------------");
			log.debug("------------ PORT: "+ server.getWebServer().getPort() + " ------------");
			log.debug("------------ PORT: "+ server.getWebServer().getPort() + " ------------");
			log.debug("------------ PORT: "+ server.getWebServer().getPort() + " ------------");
		};
	}

	@Bean
	public SimpleModule pageRequestDeserializer() {
		// Automatically registered with the auto-configured Jackson2ObjectMapperBuilder and are applied to any ObjectMapper instances that it creates.
		SimpleModule module = new SimpleModule();
		module.addDeserializer(PageRequest.class, new PageRequestDeserializer());
		return module;
	}

}
