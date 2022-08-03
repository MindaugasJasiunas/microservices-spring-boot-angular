package com.example.demo.repository;

import com.example.demo.domain.Address;
import com.example.demo.domain.Package;
import com.example.demo.domain.Sender;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SenderRepository extends ReactiveMongoRepository<Sender, String> {
    Mono<Sender> findByAddress(Address address);
}
