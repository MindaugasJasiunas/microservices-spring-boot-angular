package com.example.demo.service;

import com.example.demo.domain.Receiver;
import com.example.demo.domain.Sender;
import com.example.demo.repository.SenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor

@Service
public class SenderServiceImpl implements SenderService{
    private final SenderRepository senderRepository;

    public Mono<Sender> findSenderById(String id) {
        return senderRepository.findById(id);
    }

    public Mono<Sender> saveSenderIfNotAlreadyExists(Sender sender){
        if(sender == null ) return Mono.empty();
        return senderRepository.findByAddress(sender.getAddress())
                .filter(sender1 -> sender1 != null)
                .switchIfEmpty(senderRepository.save(sender));
    }

}
