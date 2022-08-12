package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Receiver implements Serializable {
    private static final long serialVersionUID = 4985772959502218962L;
    public static final String SEQUENCE_NAME = "receivers_sequence";
    private String id;
    private String firstName;
    private String lastName;
    private String company;
    private String phoneNumber;
    private String email;
    private Address address;
}
