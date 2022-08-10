package com.example.demo.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Package implements Serializable {
    private static final long serialVersionUID = 1337447015571327775L;
    public static final String SEQUENCE_NAME = "packages_sequence";
    private String id;
    private UUID publicId;
    private String trackingNumber;
    private PackageState packageStatus;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Long numberOfPackages = 1L;
    private Long packageWeight;
    private String packageContentsDescription;
    private boolean fragile;

    private String senderId;
    private String receiverId;

    private Sender sender;
    private Receiver receiver;

    //constructors
    public Package() {
    }

    public Package(String id, UUID publicId, String trackingNumber, PackageState packageStatus, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Long numberOfPackages, Long packageWeight, String packageContentsDescription, boolean fragile, String senderId, String receiverId, Sender sender, Receiver receiver) {
        this.id = id;
        this.publicId = publicId;
        this.trackingNumber = trackingNumber;
        this.packageStatus = packageStatus;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.numberOfPackages = numberOfPackages;
        this.packageWeight = packageWeight;
        this.packageContentsDescription = packageContentsDescription;
        this.fragile = fragile;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.sender = sender;
        this.receiver = receiver;
    }

    //setters & getters
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

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public PackageState getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(PackageState packageStatus) {
        this.packageStatus = packageStatus;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getNumberOfPackages() {
        return numberOfPackages;
    }

    public void setNumberOfPackages(Long numberOfPackages) {
        this.numberOfPackages = numberOfPackages;
    }

    public Long getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(Long packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getPackageContentsDescription() {
        return packageContentsDescription;
    }

    public void setPackageContentsDescription(String packageContentsDescription) {
        this.packageContentsDescription = packageContentsDescription;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "Package{" +
                "id='" + id + '\'' +
                ", publicId=" + publicId +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", packageStatus=" + packageStatus +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", numberOfPackages=" + numberOfPackages +
                ", packageWeight=" + packageWeight +
                ", packageContentsDescription='" + packageContentsDescription + '\'' +
                ", fragile=" + fragile +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
