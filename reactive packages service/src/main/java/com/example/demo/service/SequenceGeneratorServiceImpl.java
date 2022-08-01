package com.example.demo.service;

import com.example.demo.domain.DatabaseSequence;
import com.example.demo.repository.DatabaseSequenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j

@Service
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService{
    private final DatabaseSequenceRepository repository;

    @Override
    public Mono<DatabaseSequence> generateSequence(final String sequenceName) {

        return repository.findById(sequenceName)
        .flatMap(databaseSequence -> repository.save(databaseSequence.incrementAndReturn()))
        .map(databaseSequence -> {
            log.debug("databaseSequence is evaluated: "+databaseSequence);
            return databaseSequence;
        })
        .switchIfEmpty(createAndReturnNewSequence(sequenceName));
    }

    private Mono<DatabaseSequence> createAndReturnNewSequence(String sequenceName){
        DatabaseSequence ds = new DatabaseSequence(sequenceName, 1L);
        log.debug("new databaseSequence is created: "+ds);
        return repository.save(ds);
    }

}
