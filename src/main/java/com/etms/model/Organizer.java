package com.etms.model;

public class Organizer {
    public enum OrganizerStatus {
        ACTIVE,
        INACTIVE,
        PENDING
    }

    private int organizerId;
    private String name;
    private String contactInfo;
    private String email;
    private String password;
    private OrganizerStatus status;

    public Organizer() {
    }

    public Organizer(int organizerId, String name, String contactInfo, String email, String password,
            OrganizerStatus status) {
        this.organizerId = organizerId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    // Getters and Setters
    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OrganizerStatus getStatus() {
        return status;
    }

    public void setStatus(OrganizerStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Organizer{" +
                "organizerId=" + organizerId +
                ", name='" + name + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}