package com.etms.controller;

import com.etms.dao.EventDAO;
import com.etms.daoimpl.EventDAOImpl;
import com.etms.exception.ETMSException;
import com.etms.exception.ValidationException;
import com.etms.model.Event;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller class for event-related operations.
 * This class handles the business logic for event management.
 */
public class EventController {
    private final EventDAO eventDAO;

    /**
     * Constructs a new EventController with the default EventDAO implementation.
     */
    public EventController() {
        this.eventDAO = new EventDAOImpl();
    }

    /**
     * Constructs a new EventController with the specified EventDAO.
     * This constructor is mainly used for testing purposes.
     * 
     * @param eventDAO the EventDAO to use
     */
    public EventController(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    /**
     * Creates a new event.
     * 
     * @param event the event to create
     * @throws ETMSException if an error occurs during creation
     */
    public void createEvent(Event event) throws ETMSException {
        try {
            validateEvent(event);
            eventDAO.save(event);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ETMSException("Error creating event: " + e.getMessage());
        }
    }

    /**
     * Retrieves an event by its ID.
     * 
     * @param eventId the event's ID
     * @return the event, or null if not found
     * @throws ETMSException if an error occurs during retrieval
     */
    public Event getEventById(int eventId) throws ETMSException {
        try {
            return eventDAO.findById(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve event: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an event's information.
     * 
     * @param event the event to update
     * @throws ETMSException if an error occurs during update
     */
    public void updateEvent(Event event) throws ETMSException {
        try {
            validateEvent(event);
            eventDAO.update(event);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ETMSException("Error updating event: " + e.getMessage());
        }
    }

    /**
     * Deletes an event.
     * 
     * @param eventId the ID of the event to delete
     * @throws ETMSException if an error occurs during deletion
     */
    public void deleteEvent(int eventId) throws ETMSException {
        try {
            eventDAO.delete(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to delete event: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all events.
     * 
     * @return a list of all events
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Event> getAllEvents() throws ETMSException {
        try {
            return eventDAO.findAll();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve events: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all events of a specific type.
     * 
     * @param eventType the type of events to retrieve
     * @return a list of events of the specified type
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Event> getEventsByType(Event.EventType eventType) throws ETMSException {
        try {
            return eventDAO.findByEventType(eventType);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve events: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all events within a date range.
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return a list of events within the date range
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate) throws ETMSException {
        try {
            return eventDAO.findByDateRange(startDate, endDate);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve events: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all events at a specific venue.
     * 
     * @param venueId the venue's ID
     * @return a list of events at the specified venue
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Event> getEventsByVenue(int venueId) throws ETMSException {
        try {
            return eventDAO.findByVenue(venueId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve events: " + e.getMessage(), e);
        }
    }

    /**
     * Searches for events by keyword.
     * 
     * @param keyword the keyword to search for
     * @return a list of events matching the keyword
     * @throws ETMSException if an error occurs during search
     */
    public List<Event> searchEvents(String keyword) throws ETMSException {
        try {
            return eventDAO.searchEvents(keyword);
        } catch (Exception e) {
            throw new ETMSException("Failed to search events: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all upcoming events.
     * 
     * @return a list of upcoming events
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Event> getUpcomingEvents() throws ETMSException {
        try {
            return eventDAO.findUpcomingEvents();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve upcoming events: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all past events.
     * 
     * @return a list of past events
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Event> getPastEvents() throws ETMSException {
        try {
            return eventDAO.findPastEvents();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve past events: " + e.getMessage(), e);
        }
    }

    /**
     * Gets the number of available seats for an event.
     * 
     * @param eventId the event's ID
     * @return the number of available seats
     * @throws ETMSException if an error occurs during retrieval
     */
    public int getAvailableSeats(int eventId) throws ETMSException {
        try {
            return eventDAO.getAvailableSeats(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve available seats: " + e.getMessage(), e);
        }
    }

    /**
     * Updates the number of available seats for an event.
     * 
     * @param eventId the event's ID
     * @param seats   the new number of available seats
     * @return true if the update was successful, false otherwise
     * @throws ETMSException if an error occurs during update
     */
    public boolean updateAvailableSeats(int eventId, int seats) throws ETMSException {
        try {
            return eventDAO.updateAvailableSeats(eventId, seats);
        } catch (Exception e) {
            throw new ETMSException("Failed to update available seats: " + e.getMessage(), e);
        }
    }

    private void validateEvent(Event event) throws ValidationException {
        // Check for past dates
        if (event.getDate().isBefore(LocalDate.now())) {
            throw new ValidationException("Event date cannot be in the past");
        }

        // Check for overlapping events at the same venue
        try {
            List<Event> existingEvents = eventDAO.findByVenue(event.getVenueId());
            for (Event existingEvent : existingEvents) {
                if (existingEvent.getEventId() != event.getEventId() && // Skip current event when updating
                        existingEvent.getDate().equals(event.getDate())) {
                    // Check if time slots overlap
                    if (!(event.getTime().plusHours(2).isBefore(existingEvent.getTime()) ||
                            event.getTime().isAfter(existingEvent.getTime().plusHours(2)))) {
                        throw new ValidationException("Event time overlaps with another event at the same venue");
                    }
                }
            }
        } catch (Exception e) {
            throw new ValidationException("Error checking for overlapping events: " + e.getMessage());
        }
    }
}