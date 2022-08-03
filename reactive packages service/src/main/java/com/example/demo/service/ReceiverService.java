package com.example.demo.service;

import com.example.demo.domain.Receiver;
import reactor.core.publisher.Mono;

public interface ReceiverService {
    Mono<Receiver> findReceiverById(String id);
    Mono<Receiver> saveReceiverIfNotAlreadyExists(Receiver receiver);
}
