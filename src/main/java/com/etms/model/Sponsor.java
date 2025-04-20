package com.etms.model;

import java.math.BigDecimal;

public class Sponsor {
    public enum SponsorStatus {
        ACTIVE,
        INACTIVE,
        PENDING
    }

    public enum SponsorType {
        GOLD,
        SILVER,
        BRONZE,
        PLATINUM
    }

    private int sponsorId;
    private String sponsorName;
    private String contactInfo;
    private BigDecimal sponsorshipAmount;
    private SponsorStatus status;
    private SponsorType type;

    public Sponsor() {
    }

    public Sponsor(int sponsorId, String sponsorName, String contactInfo, BigDecimal sponsorshipAmount,
            SponsorStatus status, SponsorType type) {
        this.sponsorId = sponsorId;
        this.sponsorName = sponsorName;
        this.contactInfo = contactInfo;
        this.sponsorshipAmount = sponsorshipAmount;
        this.status = status;
        this.type = type;
    }

    // Getters and Setters
    public int getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public BigDecimal getSponsorshipAmount() {
        return sponsorshipAmount;
    }

    public void setSponsorshipAmount(BigDecimal sponsorshipAmount) {
        this.sponsorshipAmount = sponsorshipAmount;
    }

    public SponsorStatus getStatus() {
        return status;
    }

    public void setStatus(SponsorStatus status) {
        this.status = status;
    }

    public SponsorType getType() {
        return type;
    }

    public void setType(SponsorType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Sponsor{" +
                "sponsorId=" + sponsorId +
                ", sponsorName='" + sponsorName + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", sponsorshipAmount=" + sponsorshipAmount +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}