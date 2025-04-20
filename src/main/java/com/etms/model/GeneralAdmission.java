package com.etms.model;

public class GeneralAdmission extends Ticket {
    private String sectionName;

    public GeneralAdmission() {
        super();
        setTicketType(TicketType.General);
    }

    public GeneralAdmission(int ticketId, int userId, int eventId, String seatNumber,
            String sectionName) {
        super(ticketId, userId, eventId, seatNumber, null, null, TicketType.General);
        this.sectionName = sectionName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    @Override
    public String toString() {
        return "GeneralAdmission{" +
                "ticketId=" + getTicketId() +
                ", userId=" + getUserId() +
                ", eventId=" + getEventId() +
                ", seatNumber='" + getSeatNumber() + '\'' +
                ", sectionName='" + sectionName + '\'' +
                '}';
    }
}