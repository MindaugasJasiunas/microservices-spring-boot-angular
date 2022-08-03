package com.example.demo.service;

import com.example.demo.domain.Sender;
import reactor.core.publisher.Mono;

public interface SenderService {
    Mono<Sender> findSenderById(String id);
    Mono<Sender> saveSenderIfNotAlreadyExists(Sender sender);
}
