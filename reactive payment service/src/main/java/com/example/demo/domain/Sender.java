package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Sender implements Serializable {
    private static final long serialVersionUID = 6916942093917601631L;
    public static final String SEQUENCE_NAME = "senders_sequence";
    private String id;
    private String firstName;
    private String lastName;
    private String company;
    private String phoneNumber;
    private String email;
    private Address address;

    @Override
    public String toString() {
        return "Sender{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", company='" + company + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                '}';
    }

    public Sender() {
    }

    public Sender(String id, String firstName, String lastName, String company, String phoneNumber, String email, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static String getSequenceName() {
        return SEQUENCE_NAME;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}