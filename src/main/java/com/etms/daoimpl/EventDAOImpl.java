package com.etms.daoimpl;

import com.etms.dao.EventDAO;
import com.etms.model.Event;
import com.etms.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDAOImpl implements EventDAO {
    private static final Logger logger = LoggerFactory.getLogger(EventDAOImpl.class);

    @Override
    public Event findById(int id) throws Exception {
        String sql = "SELECT * FROM Event WHERE EventID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEvent(rs);
            }
        }
        return null;
    }

    @Override
    public List<Event> findAll() throws Exception {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Event";
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Executing SQL: " + sql);

            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }

            System.out.println("Found " + events.size() + " total events");
        }
        return events;
    }

    @Override
    public void save(Event event) throws Exception {
        String sql = "INSERT INTO Event (EventName, Description, Date, Time, TicketPrice, EventType, VenueID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, event.getEventName());
            stmt.setString(2, event.getDescription());
            stmt.setDate(3, Date.valueOf(event.getDate()));
            stmt.setTime(4, Time.valueOf(event.getTime()));
            stmt.setBigDecimal(5, event.getTicketPrice());
            stmt.setString(6, event.getEventType().name());
            stmt.setInt(7, event.getVenueId());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                event.setEventId(rs.getInt(1));
            }
        }
    }

    @Override
    public void update(Event event) throws Exception {
        String sql = "UPDATE Event SET EventName = ?, Description = ?, Date = ?, Time = ?, " +
                "TicketPrice = ?, EventType = ?, VenueID = ? WHERE EventID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getEventName());
            stmt.setString(2, event.getDescription());
            stmt.setDate(3, Date.valueOf(event.getDate()));
            stmt.setTime(4, Time.valueOf(event.getTime()));
            stmt.setBigDecimal(5, event.getTicketPrice());
            stmt.setString(6, event.getEventType().name());
            stmt.setInt(7, event.getVenueId());
            stmt.setInt(8, event.getEventId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Event WHERE EventID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Event> findByEventType(Event.EventType eventType) throws Exception {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Event WHERE EventType = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventType.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        }
        return events;
    }

    @Override
    public List<Event> findByDateRange(LocalDate startDate, LocalDate endDate) throws Exception {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Event WHERE Date BETWEEN ? AND ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        }
        return events;
    }

    @Override
    public List<Event> findByVenue(int venueId) throws Exception {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Event WHERE VenueID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, venueId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        }
        return events;
    }

    @Override
    public List<Event> searchEvents(String keyword) throws Exception {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Event WHERE EventName LIKE ? OR Description LIKE ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        }
        return events;
    }

    @Override
    public List<Event> findUpcomingEvents() throws Exception {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Event WHERE Date >= CURRENT_DATE ORDER BY Date, Time";
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        }
        return events;
    }

    @Override
    public List<Event> findPastEvents() throws Exception {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Event WHERE Date < CURRENT_DATE ORDER BY Date DESC, Time DESC";
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        }
        return events;
    }

    @Override
    public int getAvailableSeats(int eventId) throws Exception {
        String sql = "SELECT v.Capacity - COUNT(t.TicketID) as AvailableSeats " +
                "FROM Event e " +
                "JOIN Venue v ON e.VenueID = v.VenueID " +
                "LEFT JOIN Ticket t ON e.EventID = t.EventID " +
                "WHERE e.EventID = ? " +
                "GROUP BY v.Capacity";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("AvailableSeats");
            }
        }
        return 0;
    }

    @Override
    public boolean updateAvailableSeats(int eventId, int seats) throws Exception {
        String sql = "UPDATE Event SET AvailableSeats = ? WHERE EventID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seats);
            stmt.setInt(2, eventId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getInt("EventID"));
        event.setEventName(rs.getString("EventName"));
        event.setDescription(rs.getString("Description"));
        event.setDate(rs.getDate("Date").toLocalDate());
        event.setTime(rs.getTime("Time").toLocalTime());
        event.setTicketPrice(rs.getBigDecimal("TicketPrice"));
        event.setEventType(Event.EventType.valueOf(rs.getString("EventType")));
        event.setVenueId(rs.getInt("VenueID"));
        return event;
    }
}