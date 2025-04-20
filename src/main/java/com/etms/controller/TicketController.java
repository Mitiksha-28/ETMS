package com.etms.controller;

import com.etms.dao.TicketDAO;
import com.etms.daoimpl.TicketDAOImpl;
import com.etms.exception.ETMSException;
import com.etms.model.Ticket;

import java.util.List;

/**
 * Controller class for ticket-related operations.
 * This class handles the business logic for ticket management.
 */
public class TicketController {
    private final TicketDAO ticketDAO;

    /**
     * Constructs a new TicketController with the default TicketDAO implementation.
     */
    public TicketController() {
        this.ticketDAO = new TicketDAOImpl();
    }

    /**
     * Constructs a new TicketController with the specified TicketDAO.
     * This constructor is mainly used for testing purposes.
     * 
     * @param ticketDAO the TicketDAO to use
     */
    public TicketController(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    /**
     * Creates a new ticket.
     * 
     * @param ticket the ticket to create
     * @throws ETMSException if an error occurs during creation
     */
    public void createTicket(Ticket ticket) throws ETMSException {
        try {
            // Check if the seat is available
            if (!ticketDAO.isSeatAvailable(ticket.getEventId(), ticket.getSeatNumber())) {
                throw new ETMSException("Seat is not available");
            }

            // Save the ticket
            ticketDAO.save(ticket);
        } catch (Exception e) {
            throw new ETMSException("Failed to create ticket: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a ticket by its ID.
     * 
     * @param ticketId the ticket's ID
     * @return the ticket, or null if not found
     * @throws ETMSException if an error occurs during retrieval
     */
    public Ticket getTicketById(int ticketId) throws ETMSException {
        try {
            return ticketDAO.findById(ticketId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve ticket: " + e.getMessage(), e);
        }
    }

    /**
     * Updates a ticket's information.
     * 
     * @param ticket the ticket to update
     * @throws ETMSException if an error occurs during update
     */
    public void updateTicket(Ticket ticket) throws ETMSException {
        try {
            ticketDAO.update(ticket);
        } catch (Exception e) {
            throw new ETMSException("Failed to update ticket: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a ticket.
     * 
     * @param ticketId the ID of the ticket to delete
     * @throws ETMSException if an error occurs during deletion
     */
    public void deleteTicket(int ticketId) throws ETMSException {
        try {
            ticketDAO.delete(ticketId);
        } catch (Exception e) {
            throw new ETMSException("Failed to delete ticket: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all tickets.
     * 
     * @return a list of all tickets
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Ticket> getAllTickets() throws ETMSException {
        try {
            return ticketDAO.findAll();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve tickets: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all tickets for a specific user.
     * 
     * @param userId the user's ID
     * @return a list of tickets for the specified user
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Ticket> getTicketsByUserId(int userId) throws ETMSException {
        try {
            return ticketDAO.findByUserId(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve tickets: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all tickets for a specific event.
     * 
     * @param eventId the event's ID
     * @return a list of tickets for the specified event
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Ticket> getTicketsByEventId(int eventId) throws ETMSException {
        try {
            return ticketDAO.findByEventId(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve tickets: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all tickets of a specific type.
     * 
     * @param ticketType the type of tickets to retrieve
     * @return a list of tickets of the specified type
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Ticket> getTicketsByType(Ticket.TicketType ticketType) throws ETMSException {
        try {
            return ticketDAO.findByTicketType(ticketType);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve tickets: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a seat is available for an event.
     * 
     * @param eventId    the event's ID
     * @param seatNumber the seat number to check
     * @return true if the seat is available, false otherwise
     * @throws ETMSException if an error occurs during check
     */
    public boolean isSeatAvailable(int eventId, String seatNumber) throws ETMSException {
        try {
            return ticketDAO.isSeatAvailable(eventId, seatNumber);
        } catch (Exception e) {
            throw new ETMSException("Failed to check seat availability: " + e.getMessage(), e);
        }
    }

    /**
     * Gets the number of booked tickets for an event.
     * 
     * @param eventId the event's ID
     * @return the number of booked tickets
     * @throws ETMSException if an error occurs during retrieval
     */
    public int getBookedTicketsCount(int eventId) throws ETMSException {
        try {
            return ticketDAO.getBookedTicketsCount(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve booked tickets count: " + e.getMessage(), e);
        }
    }

    /**
     * Gets the list of available seats for an event.
     * 
     * @param eventId the event's ID
     * @return a list of available seat numbers
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<String> getAvailableSeats(int eventId) throws ETMSException {
        try {
            return ticketDAO.getAvailableSeats(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve available seats: " + e.getMessage(), e);
        }
    }

    /**
     * Cancels a ticket.
     * 
     * @param ticketId the ID of the ticket to cancel
     * @return true if the ticket was cancelled, false otherwise
     * @throws ETMSException if an error occurs during cancellation
     */
    public boolean cancelTicket(int ticketId) throws ETMSException {
        try {
            return ticketDAO.cancelTicket(ticketId);
        } catch (Exception e) {
            throw new ETMSException("Failed to cancel ticket: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all upcoming tickets for a user.
     * 
     * @param userId the user's ID
     * @return a list of upcoming tickets for the specified user
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Ticket> getUpcomingTickets(int userId) throws ETMSException {
        try {
            return ticketDAO.findUpcomingTickets(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve upcoming tickets: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all past tickets for a user.
     * 
     * @param userId the user's ID
     * @return a list of past tickets for the specified user
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Ticket> getPastTickets(int userId) throws ETMSException {
        try {
            return ticketDAO.findPastTickets(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve past tickets: " + e.getMessage(), e);
        }
    }
}