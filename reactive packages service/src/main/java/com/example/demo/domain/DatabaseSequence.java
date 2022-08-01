package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Document(collection = "database_sequences")
public class DatabaseSequence {
    @Id
    private String id;
    private long seq;

    public DatabaseSequence incrementAndReturn(){
        this.seq += 1;
        return this;
    }
}
