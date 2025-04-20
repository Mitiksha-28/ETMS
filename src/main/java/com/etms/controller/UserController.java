package com.etms.controller;

import com.etms.dao.UserDAO;
import com.etms.daoimpl.UserDAOImpl;
import com.etms.exception.ETMSException;
import com.etms.model.User;

import java.util.List;

// Handles business logic for user-related operations.
public class UserController {
    private final UserDAO userDAO;

    // Default constructor using UserDAOImpl
    public UserController() {
        this.userDAO = new UserDAOImpl();
    }

    // Constructor for injecting a custom UserDAO (mainly for testing)
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // Authenticates user with email and password
    public User authenticate(String email, String password) throws ETMSException {
        try {
            return userDAO.authenticate(email, password);
        } catch (Exception e) {
            throw new ETMSException("Authentication failed: " + e.getMessage(), e);
        }
    }

    // Registers a new user after checking for email duplication
    public void registerUser(User user) throws ETMSException {
        try {
            if (userDAO.isEmailExists(user.getEmail())) {
                throw new ETMSException("Email already exists");
            }
            userDAO.save(user);
        } catch (Exception e) {
            throw new ETMSException("Registration failed: " + e.getMessage(), e);
        }
    }

    // Retrieves user by ID
    public User getUserById(int userId) throws ETMSException {
        try {
            return userDAO.findById(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve user: " + e.getMessage(), e);
        }
    }

    // Retrieves user by email
    public User getUserByEmail(String email) throws ETMSException {
        try {
            return userDAO.findByEmail(email);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve user: " + e.getMessage(), e);
        }
    }

    // Updates existing user information
    public void updateUser(User user) throws ETMSException {
        try {
            userDAO.update(user);
        } catch (Exception e) {
            throw new ETMSException("Failed to update user: " + e.getMessage(), e);
        }
    }

    // Changes user password after verifying old password
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws ETMSException {
        try {
            return userDAO.changePassword(userId, oldPassword, newPassword);
        } catch (Exception e) {
            throw new ETMSException("Failed to change password: " + e.getMessage(), e);
        }
    }

    // Retrieves all users
    public List<User> getAllUsers() throws ETMSException {
        try {
            return userDAO.findAll();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve users: " + e.getMessage(), e);
        }
    }

    // Retrieves users by specified user type
    public List<User> getUsersByType(User.UserType userType) throws ETMSException {
        try {
            return userDAO.findByUserType(userType);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve users: " + e.getMessage(), e);
        }
    }

    // Deletes a user by ID
    public void deleteUser(int userId) throws ETMSException {
        try {
            userDAO.delete(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to delete user: " + e.getMessage(), e);
        }
    }
}
