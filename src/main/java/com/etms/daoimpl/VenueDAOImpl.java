package com.etms.daoimpl;

import com.etms.dao.VenueDAO;
import com.etms.model.Venue;
import com.etms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the VenueDAO interface.
 * Handles all database operations for the Venue entity.
 */
public class VenueDAOImpl implements VenueDAO {
    private final Connection connection;

    public VenueDAOImpl() throws SQLException {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public void save(Venue venue) throws Exception {
        String sql = "INSERT INTO venues (venue_name, location, capacity, contact_info) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, venue.getVenueName());
            stmt.setString(2, venue.getLocation());
            stmt.setInt(3, venue.getCapacity());
            stmt.setString(4, venue.getContactInfo());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating venue failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    venue.setVenueId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating venue failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public void update(Venue venue) throws Exception {
        String sql = "UPDATE venues SET venue_name = ?, location = ?, capacity = ?, contact_info = ? WHERE venue_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, venue.getVenueName());
            stmt.setString(2, venue.getLocation());
            stmt.setInt(3, venue.getCapacity());
            stmt.setString(4, venue.getContactInfo());
            stmt.setInt(5, venue.getVenueId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating venue failed, no rows affected.");
            }
        }
    }

    @Override
    public void delete(int venueId) throws Exception {
        String sql = "DELETE FROM venues WHERE venue_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venueId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting venue failed, no rows affected.");
            }
        }
    }

    @Override
    public Venue findById(int venueId) throws Exception {
        String sql = "SELECT * FROM venues WHERE venue_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venueId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVenue(rs);
                }
                return null;
            }
        }
    }

    @Override
    public Venue findByName(String venueName) throws Exception {
        String sql = "SELECT * FROM venues WHERE venue_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, venueName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVenue(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<Venue> findAll() throws Exception {
        String sql = "SELECT * FROM venues";
        List<Venue> venues = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                venues.add(mapResultSetToVenue(rs));
            }
            return venues;
        }
    }

    @Override
    public List<Venue> findByLocation(String location) throws Exception {
        String sql = "SELECT * FROM venues WHERE location = ?";
        List<Venue> venues = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, location);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    venues.add(mapResultSetToVenue(rs));
                }
                return venues;
            }
        }
    }

    @Override
    public List<Venue> findByMinCapacity(int minCapacity) throws Exception {
        String sql = "SELECT * FROM venues WHERE capacity >= ?";
        List<Venue> venues = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, minCapacity);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    venues.add(mapResultSetToVenue(rs));
                }
                return venues;
            }
        }
    }

    @Override
    public boolean isVenueNameExists(String venueName) throws Exception {
        String sql = "SELECT COUNT(*) FROM venues WHERE venue_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, venueName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    @Override
    public int getVenueCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM venues";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    @Override
    public int getTotalCapacity() throws Exception {
        String sql = "SELECT SUM(capacity) FROM venues";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    @Override
    public boolean isVenueAssociated(int venueId) throws Exception {
        String sql = "SELECT COUNT(*) FROM events WHERE venue_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venueId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    private Venue mapResultSetToVenue(ResultSet rs) throws SQLException {
        Venue venue = new Venue();
        venue.setVenueId(rs.getInt("venue_id"));
        venue.setVenueName(rs.getString("venue_name"));
        venue.setLocation(rs.getString("location"));
        venue.setCapacity(rs.getInt("capacity"));
        venue.setContactInfo(rs.getString("contact_info"));
        return venue;
    }
}