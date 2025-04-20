package com.etms.dao;

import com.etms.model.Ticket;
import java.util.List;

public interface TicketDAO extends BaseDAO<Ticket> {
    List<Ticket> findByUserId(int userId) throws Exception;

    List<Ticket> findByEventId(int eventId) throws Exception;

    List<Ticket> findByTicketType(Ticket.TicketType ticketType) throws Exception;

    boolean isSeatAvailable(int eventId, String seatNumber) throws Exception;

    int getBookedTicketsCount(int eventId) throws Exception;

    List<String> getAvailableSeats(int eventId) throws Exception;

    boolean cancelTicket(int ticketId) throws Exception;

    List<Ticket> findUpcomingTickets(int userId) throws Exception;

    List<Ticket> findPastTickets(int userId) throws Exception;
}