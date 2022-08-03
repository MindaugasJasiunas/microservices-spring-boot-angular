package com.example.demo.service;

import com.example.demo.domain.Receiver;
import com.example.demo.repository.ReceiverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor

@Service
public class ReceiverServiceImpl implements ReceiverService{
    private final ReceiverRepository receiverRepository;

    @Override
    public Mono<Receiver> findReceiverById(String id){
        return receiverRepository.findById(id);
    }

    @Override
    public Mono<Receiver> saveReceiverIfNotAlreadyExists(Receiver receiver){
        if(receiver == null) return Mono.empty();
        return receiverRepository.findByAddress(receiver.getAddress())
                .filter(receiver1 -> receiver1 != null)
                .switchIfEmpty(receiverRepository.save(receiver));
    }
}
