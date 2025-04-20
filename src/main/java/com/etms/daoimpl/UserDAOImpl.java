package com.etms.daoimpl;

import com.etms.dao.UserDAO;
import com.etms.model.User;
import com.etms.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    @Override
    public User findById(int id) throws Exception {
        String sql = "SELECT * FROM User WHERE UserID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    @Override
    public void save(User user) throws Exception {
        String sql = "INSERT INTO User (Name, Email, Phone, Password, UserType) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail().trim().toLowerCase());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, hashPassword(user.getPassword()));
            stmt.setString(5, user.getUserType().name());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setUserId(rs.getInt(1));
            }
        }
    }

    @Override
    public void update(User user) throws Exception {
        String sql = "UPDATE User SET Name = ?, Email = ?, Phone = ?, Password = ?, UserType = ? WHERE UserID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, hashPassword(user.getPassword()));
            stmt.setString(5, user.getUserType().name().toUpperCase());
            stmt.setInt(6, user.getUserId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM User WHERE UserID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public User findByEmail(String email) throws Exception {
        String sql = "SELECT * FROM User WHERE Email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        }
        return null;
    }

    @Override
    public User authenticate(String email, String password) throws Exception {
        String sql = "SELECT * FROM User WHERE Email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.trim().toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("Password");
                if (hashPassword(password).equals(storedHash)) {
                    User user = mapResultSetToUser(rs);
                    // Set a temporary password to allow profile updates
                    user.setPassword(storedHash);
                    return user;
                }
            }
        }
        return null;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    @Override
    public List<User> findByUserType(User.UserType userType) throws Exception {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User WHERE UserType = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userType.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws Exception {
        String hashedOldPassword = hashPassword(oldPassword);
        String hashedNewPassword = hashPassword(newPassword);
        String sql = "UPDATE User SET Password = ? WHERE UserID = ? AND Password = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hashedNewPassword);
            stmt.setInt(2, userId);
            stmt.setString(3, hashedOldPassword);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean isEmailExists(String email) throws Exception {
        String sql = "SELECT COUNT(*) FROM User WHERE Email = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserID"));
        user.setName(rs.getString("Name"));
        user.setEmail(rs.getString("Email"));
        user.setPhone(rs.getString("Phone"));
        user.setPassword(rs.getString("Password"));
        String userTypeStr = rs.getString("UserType").toUpperCase();
        user.setUserType(User.UserType.valueOf(userTypeStr));
        return user;
    }
}