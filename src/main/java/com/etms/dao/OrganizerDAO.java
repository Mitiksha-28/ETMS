package com.etms.dao;

import com.etms.model.Organizer;
import java.util.List;

/**
 * Data Access Object interface for Organizer entity.
 * This interface defines methods for managing organizer data in the database.
 */
public interface OrganizerDAO {
    /**
     * Saves a new organizer to the database.
     * 
     * @param organizer the organizer to save
     * @throws Exception if an error occurs during the save operation
     */
    void save(Organizer organizer) throws Exception;

    /**
     * Updates an existing organizer in the database.
     * 
     * @param organizer the organizer to update
     * @throws Exception if an error occurs during the update operation
     */
    void update(Organizer organizer) throws Exception;

    /**
     * Deletes an organizer from the database.
     * 
     * @param organizerId the ID of the organizer to delete
     * @throws Exception if an error occurs during the delete operation
     */
    void delete(int organizerId) throws Exception;

    /**
     * Finds an organizer by their ID.
     * 
     * @param organizerId the ID of the organizer to find
     * @return the organizer, or null if not found
     * @throws Exception if an error occurs during the find operation
     */
    Organizer findById(int organizerId) throws Exception;

    /**
     * Finds an organizer by their email.
     * 
     * @param email the email of the organizer to find
     * @return the organizer, or null if not found
     * @throws Exception if an error occurs during the find operation
     */
    Organizer findByEmail(String email) throws Exception;

    /**
     * Finds all organizers in the database.
     * 
     * @return a list of all organizers
     * @throws Exception if an error occurs during the find operation
     */
    List<Organizer> findAll() throws Exception;

    /**
     * Finds all organizers by their status.
     * 
     * @param status the status of organizers to find
     * @return a list of organizers with the specified status
     * @throws Exception if an error occurs during the find operation
     */
    List<Organizer> findByStatus(Organizer.OrganizerStatus status) throws Exception;

    /**
     * Checks if an email already exists in the database.
     * 
     * @param email the email to check
     * @return true if the email exists, false otherwise
     * @throws Exception if an error occurs during the check operation
     */
    boolean isEmailExists(String email) throws Exception;

    /**
     * Authenticates an organizer with the given email and password.
     * 
     * @param email    the organizer's email
     * @param password the organizer's password
     * @return the authenticated organizer, or null if authentication fails
     * @throws Exception if an error occurs during authentication
     */
    Organizer authenticate(String email, String password) throws Exception;

    /**
     * Changes an organizer's password.
     * 
     * @param organizerId the organizer's ID
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return true if the password was changed, false otherwise
     * @throws Exception if an error occurs during the password change
     */
    boolean changePassword(int organizerId, String oldPassword, String newPassword) throws Exception;
}