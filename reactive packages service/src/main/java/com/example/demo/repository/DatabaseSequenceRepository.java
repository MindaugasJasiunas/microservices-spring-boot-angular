package com.example.demo.repository;

import com.example.demo.domain.DatabaseSequence;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DatabaseSequenceRepository extends ReactiveMongoRepository<DatabaseSequence, String> {}
