package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Document(collection="senders")
public class Sender implements Serializable {
    private static final long serialVersionUID = 6916942093917601631L;
    public static final String SEQUENCE_NAME = "senders_sequence";
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String company;
    private String phoneNumber;
    private String email;
    private Address address;
}
