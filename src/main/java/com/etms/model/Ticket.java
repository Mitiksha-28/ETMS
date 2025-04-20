package com.etms.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Ticket {
    private int ticketId;
    private int userId;
    private int eventId;
    private String seatNumber;
    private LocalDateTime bookingDate;
    private BigDecimal price;
    private TicketType ticketType;

    public enum TicketType {
        VIP,
        General
    }

    public Ticket() {
    }

    public Ticket(int ticketId, int userId, int eventId, String seatNumber,
            LocalDateTime bookingDate, BigDecimal price, TicketType ticketType) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.eventId = eventId;
        this.seatNumber = seatNumber;
        this.bookingDate = bookingDate;
        this.price = price;
        this.ticketType = ticketType;
    }

    // Getters and Setters
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", seatNumber='" + seatNumber + '\'' +
                ", bookingDate=" + bookingDate +
                ", price=" + price +
                ", ticketType=" + ticketType +
                '}';
    }
}