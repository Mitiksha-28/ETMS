package com.etms.dao;

import com.etms.model.Event;
import java.time.LocalDate;
import java.util.List;

public interface EventDAO extends BaseDAO<Event> {
    List<Event> findByEventType(Event.EventType eventType) throws Exception;

    List<Event> findByDateRange(LocalDate startDate, LocalDate endDate) throws Exception;

    List<Event> findByVenue(int venueId) throws Exception;

    List<Event> searchEvents(String keyword) throws Exception;

    List<Event> findUpcomingEvents() throws Exception;

    List<Event> findPastEvents() throws Exception;

    int getAvailableSeats(int eventId) throws Exception;

    boolean updateAvailableSeats(int eventId, int seats) throws Exception;
}