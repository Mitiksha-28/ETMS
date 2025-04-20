package com.etms.controller;

import com.etms.dao.UserDAO;
import com.etms.daoimpl.UserDAOImpl;
import com.etms.exception.ETMSException;
import com.etms.model.User;

import java.util.List;

/**
 * Controller class for user-related operations.
 * This class handles the business logic for user management.
 */
public class UserController {
    private final UserDAO userDAO;

    /**
     * Constructs a new UserController with the default UserDAO implementation.
     */
    public UserController() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Constructs a new UserController with the specified UserDAO.
     * This constructor is mainly used for testing purposes.
     * 
     * @param userDAO the UserDAO to use
     */
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Authenticates a user with the given email and password.
     * 
     * @param email    the user's email
     * @param password the user's password
     * @return the authenticated user, or null if authentication fails
     * @throws ETMSException if an error occurs during authentication
     */
    public User authenticate(String email, String password) throws ETMSException {
        try {
            return userDAO.authenticate(email, password);
        } catch (Exception e) {
            throw new ETMSException("Authentication failed: " + e.getMessage(), e);
        }
    }

    /**
     * Registers a new user.
     * 
     * @param user the user to register
     * @throws ETMSException if an error occurs during registration
     */
    public void registerUser(User user) throws ETMSException {
        try {
            // Check if email already exists
            if (userDAO.isEmailExists(user.getEmail())) {
                throw new ETMSException("Email already exists");
            }

            // Save the user
            userDAO.save(user);
        } catch (Exception e) {
            throw new ETMSException("Registration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a user by their ID.
     * 
     * @param userId the user's ID
     * @return the user, or null if not found
     * @throws ETMSException if an error occurs during retrieval
     */
    public User getUserById(int userId) throws ETMSException {
        try {
            return userDAO.findById(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve user: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a user by their email.
     * 
     * @param email the user's email
     * @return the user, or null if not found
     * @throws ETMSException if an error occurs during retrieval
     */
    public User getUserByEmail(String email) throws ETMSException {
        try {
            return userDAO.findByEmail(email);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve user: " + e.getMessage(), e);
        }
    }

    /**
     * Updates a user's information.
     * 
     * @param user the user to update
     * @throws ETMSException if an error occurs during update
     */
    public void updateUser(User user) throws ETMSException {
        try {
            userDAO.update(user);
        } catch (Exception e) {
            throw new ETMSException("Failed to update user: " + e.getMessage(), e);
        }
    }

    /**
     * Changes a user's password.
     * 
     * @param userId      the user's ID
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return true if the password was changed, false otherwise
     * @throws ETMSException if an error occurs during password change
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws ETMSException {
        try {
            return userDAO.changePassword(userId, oldPassword, newPassword);
        } catch (Exception e) {
            throw new ETMSException("Failed to change password: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all users.
     * 
     * @return a list of all users
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<User> getAllUsers() throws ETMSException {
        try {
            return userDAO.findAll();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve users: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all users of a specific type.
     * 
     * @param userType the type of users to retrieve
     * @return a list of users of the specified type
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<User> getUsersByType(User.UserType userType) throws ETMSException {
        try {
            return userDAO.findByUserType(userType);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve users: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a user.
     * 
     * @param userId the ID of the user to delete
     * @throws ETMSException if an error occurs during deletion
     */
    public void deleteUser(int userId) throws ETMSException {
        try {
            userDAO.delete(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to delete user: " + e.getMessage(), e);
        }
    }
}