package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Document(collection="packages")
public class Package implements Serializable {
    private static final long serialVersionUID = 1337447015571327775L;
    public static final String SEQUENCE_NAME = "packages_sequence";
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    private String id;
    private UUID publicId;// = UUID.randomUUID();
    private String trackingNumber;
    private PackageState packageStatus = PackageState.NEW;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdDate = LocalDateTime.now();
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
    private Long numberOfPackages = 1L;
    private Long packageWeight;
    private String packageContentsDescription;
    private boolean fragile = false;

    @JsonIgnore
    private String senderId;
    @JsonIgnore
    private String receiverId;

    @Transient
    private Sender sender;
    @Transient
    private Receiver receiver;
}
