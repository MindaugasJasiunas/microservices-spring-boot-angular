package com.example.demo.repository;

import com.example.demo.domain.Address;
import com.example.demo.domain.Receiver;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ReceiverRepository extends ReactiveMongoRepository<Receiver, String> {
    Mono<Receiver> findByAddress(Address address);
}
