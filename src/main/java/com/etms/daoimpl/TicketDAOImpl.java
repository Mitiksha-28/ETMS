package com.etms.daoimpl;

import com.etms.dao.TicketDAO;
import com.etms.model.Ticket;
import com.etms.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAOImpl implements TicketDAO {
    private static final Logger logger = LoggerFactory.getLogger(TicketDAOImpl.class);

    @Override
    public Ticket findById(int id) throws Exception {
        String sql = "SELECT * FROM Ticket WHERE TicketID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTicket(rs);
            }
        }
        return null;
    }

    @Override
    public List<Ticket> findAll() throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM Ticket";
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    @Override
    public void save(Ticket ticket) throws Exception {
        String sql = "INSERT INTO Ticket (UserID, EventID, SeatNumber, BookingDate, Price, TicketType) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ticket.getUserId());
            stmt.setInt(2, ticket.getEventId());
            stmt.setString(3, ticket.getSeatNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(ticket.getBookingDate()));
            stmt.setBigDecimal(5, ticket.getPrice());
            stmt.setString(6, ticket.getTicketType().name());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                ticket.setTicketId(rs.getInt(1));
            }
        }
    }

    @Override
    public void update(Ticket ticket) throws Exception {
        String sql = "UPDATE Ticket SET UserID = ?, EventID = ?, SeatNumber = ?, " +
                "BookingDate = ?, Price = ?, TicketType = ? WHERE TicketID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticket.getUserId());
            stmt.setInt(2, ticket.getEventId());
            stmt.setString(3, ticket.getSeatNumber());
            stmt.setTimestamp(4, Timestamp.valueOf(ticket.getBookingDate()));
            stmt.setBigDecimal(5, ticket.getPrice());
            stmt.setString(6, ticket.getTicketType().name());
            stmt.setInt(7, ticket.getTicketId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Ticket WHERE TicketID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Ticket> findByUserId(int userId) throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM Ticket WHERE UserID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    @Override
    public List<Ticket> findByEventId(int eventId) throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM Ticket WHERE EventID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    @Override
    public List<Ticket> findByTicketType(Ticket.TicketType ticketType) throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM Ticket WHERE TicketType = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticketType.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    @Override
    public boolean isSeatAvailable(int eventId, String seatNumber) throws Exception {
        String sql = "SELECT COUNT(*) FROM Ticket WHERE EventID = ? AND SeatNumber = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            stmt.setString(2, seatNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false;
    }

    @Override
    public int getBookedTicketsCount(int eventId) throws Exception {
        String sql = "SELECT COUNT(*) FROM Ticket WHERE EventID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public List<String> getAvailableSeats(int eventId) throws Exception {
        List<String> availableSeats = new ArrayList<>();
        String sql = "SELECT v.Capacity, t.SeatNumber " +
                "FROM Event e " +
                "JOIN Venue v ON e.VenueID = v.VenueID " +
                "LEFT JOIN Ticket t ON e.EventID = t.EventID " +
                "WHERE e.EventID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();
            int capacity = 0;
            if (rs.next()) {
                capacity = rs.getInt("Capacity");
            }

            // Generate all possible seat numbers
            for (int i = 1; i <= capacity; i++) {
                availableSeats.add("A" + i);
            }

            // Remove booked seats
            while (rs.next()) {
                String seatNumber = rs.getString("SeatNumber");
                if (seatNumber != null) {
                    availableSeats.remove(seatNumber);
                }
            }
        }
        return availableSeats;
    }

    @Override
    public boolean cancelTicket(int ticketId) throws Exception {
        String sql = "DELETE FROM Ticket WHERE TicketID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Ticket> findUpcomingTickets(int userId) throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.* FROM Ticket t " +
                "JOIN Event e ON t.EventID = e.EventID " +
                "WHERE t.UserID = ? AND e.Date >= CURRENT_DATE " +
                "ORDER BY e.Date, e.Time";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    @Override
    public List<Ticket> findPastTickets(int userId) throws Exception {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT t.* FROM Ticket t " +
                "JOIN Event e ON t.EventID = e.EventID " +
                "WHERE t.UserID = ? AND e.Date < CURRENT_DATE " +
                "ORDER BY e.Date DESC, e.Time DESC";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        }
        return tickets;
    }

    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setTicketId(rs.getInt("TicketID"));
        ticket.setUserId(rs.getInt("UserID"));
        ticket.setEventId(rs.getInt("EventID"));
        ticket.setSeatNumber(rs.getString("SeatNumber"));
        ticket.setBookingDate(rs.getTimestamp("BookingDate").toLocalDateTime());
        ticket.setPrice(rs.getBigDecimal("Price"));
        ticket.setTicketType(Ticket.TicketType.valueOf(rs.getString("TicketType")));
        return ticket;
    }
}