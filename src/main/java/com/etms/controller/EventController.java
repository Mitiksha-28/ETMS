package com.etms.controller;

import com.etms.dao.EventDAO;
import com.etms.daoimpl.EventDAOImpl;
import com.etms.exception.ETMSException;
import com.etms.exception.ValidationException;
import com.etms.model.Event;

import java.time.LocalDate;
import java.util.List;
public class EventController {
    private final EventDAO eventDAO;

    public EventController() {
        this.eventDAO = new EventDAOImpl();
    }

    public EventController(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    // Creates a new event.
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

    // Retrieves an event by its ID.  
    public Event getEventById(int eventId) throws ETMSException {
        try {
            return eventDAO.findById(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve event: " + e.getMessage(), e);
        }
    }

    // Updates an event's information.
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

    // Deletes an event.
    public void deleteEvent(int eventId) throws ETMSException {
        try {
            eventDAO.delete(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to delete event: " + e.getMessage(), e);
        }
    }

    // Retrieves all events.
    public List<Event> getAllEvents() throws ETMSException {
        try {
            return eventDAO.findAll();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve events: " + e.getMessage(), e);
        }
    }

    // Retrieves all events of a specific type.
    public List<Event> getEventsByType(Event.EventType eventType) throws ETMSException {
        try {
            return eventDAO.findByEventType(eventType);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve events: " + e.getMessage(), e);
        }
    }

    // Retrieves all events within a date range.
        public List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate) throws ETMSException {
        try {
            return eventDAO.findByDateRange(startDate, endDate);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve events: " + e.getMessage(), e);
        }
    }

    // Retrieves all events at a specific venue.
    public List<Event> getEventsByVenue(int venueId) throws ETMSException {
        try {
            return eventDAO.findByVenue(venueId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve events: " + e.getMessage(), e);
        }
    }

    // Searches for events by keyword.
    public List<Event> searchEvents(String keyword) throws ETMSException {
        try {
            return eventDAO.searchEvents(keyword);
        } catch (Exception e) {
            throw new ETMSException("Failed to search events: " + e.getMessage(), e);
        }
    }
    // Retrieves all upcoming events.
    public List<Event> getUpcomingEvents() throws ETMSException {
        try {
            return eventDAO.findUpcomingEvents();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve upcoming events: " + e.getMessage(), e);
        }
    }

    // Retrieves all past events.
    public List<Event> getPastEvents() throws ETMSException {
        try {
            return eventDAO.findPastEvents();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve past events: " + e.getMessage(), e);
        }
    }

    // Gets the number of available seats for an event.
    public int getAvailableSeats(int eventId) throws ETMSException {
        try {
            return eventDAO.getAvailableSeats(eventId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve available seats: " + e.getMessage(), e);
        }
    }

    // Updates the number of available seats for an event.
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