package com.etms.model;

public class Venue {
    private int venueId;
    private String venueName;
    private String location;
    private int capacity;
    private String contactInfo;

    public Venue() {
    }

    public Venue(int venueId, String venueName, String location, int capacity, String contactInfo) {
        this.venueId = venueId;
        this.venueName = venueName;
        this.location = location;
        this.capacity = capacity;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "venueId=" + venueId +
                ", venueName='" + venueName + '\'' +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}