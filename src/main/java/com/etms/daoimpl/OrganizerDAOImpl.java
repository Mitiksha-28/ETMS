package com.etms.daoimpl;

import com.etms.dao.OrganizerDAO;
import com.etms.model.Organizer;
import com.etms.util.DatabaseUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Implementation of the OrganizerDAO interface.
 * Handles all database operations for the Organizer entity.
 */
public class OrganizerDAOImpl implements OrganizerDAO {

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    @Override
    public void save(Organizer organizer) throws Exception {
        String sql = "INSERT INTO organizers (name, contact_info, email, password, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, organizer.getName());
            stmt.setString(2, organizer.getContactInfo());
            stmt.setString(3, organizer.getEmail());
            stmt.setString(4, hashPassword(organizer.getPassword()));
            stmt.setString(5, organizer.getStatus().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating organizer failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    organizer.setOrganizerId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating organizer failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public void update(Organizer organizer) throws Exception {
        String sql = "UPDATE organizers SET name = ?, contact_info = ?, email = ?, password = ?, status = ? WHERE organizer_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, organizer.getName());
            stmt.setString(2, organizer.getContactInfo());
            stmt.setString(3, organizer.getEmail());
            stmt.setString(4, hashPassword(organizer.getPassword()));
            stmt.setString(5, organizer.getStatus().name());
            stmt.setInt(6, organizer.getOrganizerId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating organizer failed, no rows affected.");
            }
        }
    }

    @Override
    public void delete(int organizerId) throws Exception {
        String sql = "DELETE FROM organizers WHERE organizer_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, organizerId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting organizer failed, no rows affected.");
            }
        }
    }

    @Override
    public Organizer findById(int organizerId) throws Exception {
        String sql = "SELECT * FROM organizers WHERE organizer_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, organizerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrganizer(rs);
                }
                return null;
            }
        }
    }

    @Override
    public Organizer findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM organizers WHERE email = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrganizer(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<Organizer> findAll() throws Exception {
        String sql = "SELECT * FROM organizers";
        List<Organizer> organizers = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                organizers.add(mapResultSetToOrganizer(rs));
            }
            return organizers;
        }
    }

    @Override
    public List<Organizer> findByStatus(Organizer.OrganizerStatus status) throws Exception {
        String sql = "SELECT * FROM organizers WHERE status = ?";
        List<Organizer> organizers = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    organizers.add(mapResultSetToOrganizer(rs));
                }
                return organizers;
            }
        }
    }

    @Override
    public boolean isEmailExists(String email) throws Exception {
        String sql = "SELECT COUNT(*) FROM organizers WHERE email = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    @Override
    public Organizer authenticate(String email, String password) throws Exception {
        String hashedPassword = hashPassword(password);
        String sql = "SELECT * FROM organizers WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToOrganizer(rs);
                }
                return null;
            }
        }
    }

    @Override
    public boolean changePassword(int organizerId, String oldPassword, String newPassword) throws Exception {
        String hashedOldPassword = hashPassword(oldPassword);
        String hashedNewPassword = hashPassword(newPassword);
        String sql = "UPDATE organizers SET password = ? WHERE organizer_id = ? AND password = ?";

        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hashedNewPassword);
            stmt.setInt(2, organizerId);
            stmt.setString(3, hashedOldPassword);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    private Organizer mapResultSetToOrganizer(ResultSet rs) throws SQLException {
        Organizer organizer = new Organizer();
        organizer.setOrganizerId(rs.getInt("organizer_id"));
        organizer.setName(rs.getString("name"));
        organizer.setContactInfo(rs.getString("contact_info"));
        organizer.setEmail(rs.getString("email"));
        organizer.setPassword(rs.getString("password"));
        organizer.setStatus(Organizer.OrganizerStatus.valueOf(rs.getString("status")));
        return organizer;
    }
}