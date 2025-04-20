package com.etms.model;

public class VIPTicket extends Ticket {
    private String perksDescription;

    public VIPTicket() {
        super();
        setTicketType(TicketType.VIP);
    }

    public VIPTicket(int ticketId, int userId, int eventId, String seatNumber,
            String perksDescription) {
        super(ticketId, userId, eventId, seatNumber, null, null, TicketType.VIP);
        this.perksDescription = perksDescription;
    }

    public String getPerksDescription() {
        return perksDescription;
    }

    public void setPerksDescription(String perksDescription) {
        this.perksDescription = perksDescription;
    }

    @Override
    public String toString() {
        return "VIPTicket{" +
                "ticketId=" + getTicketId() +
                ", userId=" + getUserId() +
                ", eventId=" + getEventId() +
                ", seatNumber='" + getSeatNumber() + '\'' +
                ", perksDescription='" + perksDescription + '\'' +
                '}';
    }
}