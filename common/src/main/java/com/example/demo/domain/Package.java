package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Package implements Serializable {
    private static final long serialVersionUID = 1337447015571327775L;
    public static final String SEQUENCE_NAME = "packages_sequence";
    private String id;
    private UUID publicId;// = UUID.randomUUID();
    private String trackingNumber;
    private PackageState packageStatus = PackageState.NEW;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
    private Long numberOfPackages = 1L;
    private Long packageWeight;
    private String packageContentsDescription;
    private boolean fragile = false;
    private boolean isReturn = false;

    private String senderId;
    private String receiverId;

    private Sender sender;
    private Receiver receiver;
}