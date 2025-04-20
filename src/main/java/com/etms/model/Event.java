package com.etms.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int eventId;
    private String eventName;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private BigDecimal ticketPrice;
    private EventType eventType;
    private int venueId;

    public enum EventType {
        Concert, 
        Sports, 
        Conference
    }

    public Event() {
    }

    public Event(int eventId, String eventName, String description, LocalDate date, LocalTime time,
            BigDecimal ticketPrice, EventType eventType, int venueId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.description = description;
        this.date = date;
        this.time = time;
        this.ticketPrice = ticketPrice;
        this.eventType = eventType;
        this.venueId = venueId;
    }

    // Getters and Setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", ticketPrice=" + ticketPrice +
                ", eventType=" + eventType +
                ", venueId=" + venueId +
                '}';
    }
}