package com.etms.daoimpl;

import com.etms.dao.SponsorDAO;
import com.etms.model.Sponsor;
import com.etms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the SponsorDAO interface.
 * Handles all database operations for the Sponsor entity.
 */
public class SponsorDAOImpl implements SponsorDAO {
    private final Connection connection;

    public SponsorDAOImpl() throws SQLException {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public Sponsor save(Sponsor sponsor) throws Exception {
        String sql = "INSERT INTO sponsors (name, email, phone, status, type, sponsorship_amount) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sponsor.getSponsorName());
            stmt.setString(2, sponsor.getContactInfo());
            stmt.setString(3, sponsor.getContactInfo());
            stmt.setString(4, sponsor.getStatus().name());
            stmt.setString(5, sponsor.getType().name());
            stmt.setBigDecimal(6, sponsor.getSponsorshipAmount());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating sponsor failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    sponsor.setSponsorId(generatedKeys.getInt(1));
                    return sponsor;
                } else {
                    throw new SQLException("Creating sponsor failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public boolean update(Sponsor sponsor) throws Exception {
        String sql = "UPDATE sponsors SET name = ?, email = ?, phone = ?, status = ?, type = ?, sponsorship_amount = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sponsor.getSponsorName());
            stmt.setString(2, sponsor.getContactInfo());
            stmt.setString(3, sponsor.getContactInfo());
            stmt.setString(4, sponsor.getStatus().name());
            stmt.setString(5, sponsor.getType().name());
            stmt.setBigDecimal(6, sponsor.getSponsorshipAmount());
            stmt.setInt(7, sponsor.getSponsorId());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int sponsorId) throws Exception {
        String sql = "DELETE FROM sponsors WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sponsorId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Sponsor findById(int sponsorId) throws Exception {
        String sql = "SELECT * FROM sponsors WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sponsorId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSponsor(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<Sponsor> findAll() throws Exception {
        String sql = "SELECT * FROM sponsors";
        List<Sponsor> sponsors = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                sponsors.add(mapResultSetToSponsor(rs));
            }
            return sponsors;
        }
    }

    @Override
    public List<Sponsor> findByEventId(int eventId) throws Exception {
        String sql = "SELECT s.* FROM sponsors s " +
                "JOIN event_sponsors es ON s.id = es.sponsor_id " +
                "WHERE es.event_id = ?";

        List<Sponsor> sponsors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sponsors.add(mapResultSetToSponsor(rs));
                }
                return sponsors;
            }
        }
    }

    @Override
    public List<Sponsor> findByStatus(Sponsor.SponsorStatus status) throws Exception {
        String sql = "SELECT * FROM sponsors WHERE status = ?";

        List<Sponsor> sponsors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sponsors.add(mapResultSetToSponsor(rs));
                }
                return sponsors;
            }
        }
    }

    @Override
    public List<Sponsor> findByType(Sponsor.SponsorType type) throws Exception {
        String sql = "SELECT * FROM sponsors WHERE type = ?";

        List<Sponsor> sponsors = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sponsors.add(mapResultSetToSponsor(rs));
                }
                return sponsors;
            }
        }
    }

    @Override
    public double getTotalSponsorshipAmount() throws Exception {
        String sql = "SELECT SUM(sponsorship_amount) FROM sponsors";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
            return 0.0;
        }
    }

    @Override
    public int getSponsorCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM sponsors";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    @Override
    public boolean isSponsorAssociated(int sponsorId) throws Exception {
        String sql = "SELECT COUNT(*) FROM event_sponsors WHERE sponsor_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, sponsorId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    private Sponsor mapResultSetToSponsor(ResultSet rs) throws SQLException {
        Sponsor sponsor = new Sponsor();
        sponsor.setSponsorId(rs.getInt("id"));
        sponsor.setSponsorName(rs.getString("name"));
        sponsor.setContactInfo(rs.getString("contact_info"));
        sponsor.setContactInfo(rs.getString("phone"));
        sponsor.setStatus(Sponsor.SponsorStatus.valueOf(rs.getString("status")));
        sponsor.setType(Sponsor.SponsorType.valueOf(rs.getString("type")));
        sponsor.setSponsorshipAmount(rs.getBigDecimal("sponsorship_amount"));
        return sponsor;
    }
}