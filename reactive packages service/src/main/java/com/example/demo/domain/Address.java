package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Address {
    private String address1;
    private String address2;
    private String address3;
    private String houseNumber;
    private String apartmentNumber;
    private String city;
    private String state;
    private String postalCode;
}
