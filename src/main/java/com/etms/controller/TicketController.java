package com.etms.controller;

import com.etms.dao.TicketDAO;
import com.etms.daoimpl.TicketDAOImpl;
import com.etms.exception.ETMSException;
import com.etms.model.Ticket;

import java.util.List;

// Handles ticket-related business logic
public class TicketController {
    private final TicketDAO ticketDAO;

    // Default constructor
    public TicketController() {
        this.ticketDAO = new TicketDAOImpl();
    }

    // Constructor with custom DAO (for testing)
    public TicketController(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    // Creates a new ticket
    public void createTicket(Ticket ticket) throws ETMSException {
        try {
            if (!ticketDAO.isSeatAvailable(ticket.getEventId(), ticket.getSeatNumber())) {
                throw new ETMSException("Seat is not available");
            }
            ticketDAO.save(ticket);
        } catch (Exception e) {
            throw new ETMSException("Failed to create ticket: " + e.getMessage(), e);
        }
    }

    // Retrieves a ticket by ID
    public Ticket getTicketById(int ticketId) throws ETMSException {
        try {
            return ticketDAO.findById(ticketId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve ticket: " + e.getMessage(), e);
        }
    }

    // Updates ticket details
    public void updateTicket(Ticket ticket) throws ETMSException {
        try {
            ticketDAO.update(ticket);
        } catch (Exception e) {
            throw new ETMSException("Failed to update ticket: " + e.getMessage(), e);
        }
    }

    // Deletes a ticket by ID
    public void deleteTicket(int ticketId) throws ETMSException {
        try {
            ticketDAO.delete(ticketId);
        } catch (Exception e) {
            throw new ETMSException("Failed to delete ticket: " + e.getMessage(), e);
        }
    }

    // Retrieves all tickets
    public List<Ticket> getAllTickets() throws ETMSException {
        try {
            return ticketDAO.findAll();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve tickets: " + e.getMessage(), e);
        }
    }

    // Retrieves tickets for a user
    public List<Ticket> getTicketsByUserId(int userId) throws ETMSException {
        try {
            return ticketDAO.findByUserId(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve tickets: " + e.getMessage(), e);
        }
    }

    // Retrieves tickets for an event
    public List<Ticket> getTicketsByEventId(int eventId) throws ETMSException {
        try {
            return ticketDAO.findByEventId(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve tickets: " + e.getMessage(), e);
        }
    }

    // Retrieves tickets by type
    public List<Ticket> getTicketsByType(Ticket.TicketType ticketType) throws ETMSException {
        try {
            return ticketDAO.findByTicketType(ticketType);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve tickets: " + e.getMessage(), e);
        }
    }

    // Checks seat availability
    public boolean isSeatAvailable(int eventId, String seatNumber) throws ETMSException {
        try {
            return ticketDAO.isSeatAvailable(eventId, seatNumber);
        } catch (Exception e) {
            throw new ETMSException("Failed to check seat availability: " + e.getMessage(), e);
        }
    }

    // Gets booked tickets count
    public int getBookedTicketsCount(int eventId) throws ETMSException {
        try {
            return ticketDAO.getBookedTicketsCount(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve booked tickets count: " + e.getMessage(), e);
        }
    }

    // Gets available seats
    public List<String> getAvailableSeats(int eventId) throws ETMSException {
        try {
            return ticketDAO.getAvailableSeats(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve available seats: " + e.getMessage(), e);
        }
    }

    // Cancels a ticket
    public boolean cancelTicket(int ticketId) throws ETMSException {
        try {
            return ticketDAO.cancelTicket(ticketId);
        } catch (Exception e) {
            throw new ETMSException("Failed to cancel ticket: " + e.getMessage(), e);
        }
    }

    // Gets upcoming tickets for a user
    public List<Ticket> getUpcomingTickets(int userId) throws ETMSException {
        try {
            return ticketDAO.findUpcomingTickets(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve upcoming tickets: " + e.getMessage(), e);
        }
    }

    // Gets past tickets for a user
    public List<Ticket> getPastTickets(int userId) throws ETMSException {
        try {
            return ticketDAO.findPastTickets(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve past tickets: " + e.getMessage(), e);
        }
    }
}

