package com.example.demo.repository;

import com.example.demo.domain.Address;
import com.example.demo.domain.Sender;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SenderRepository extends ReactiveMongoRepository<Sender, String> {
    Mono<Sender> findByAddress(Address address);
}
