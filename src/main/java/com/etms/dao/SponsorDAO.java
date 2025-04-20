package com.etms.dao;

import com.etms.model.Sponsor;
import java.util.List;

/**
 * Data Access Object interface for Sponsor entity.
 * Defines methods for CRUD operations and additional business logic.
 */
public interface SponsorDAO {
    /**
     * Saves a new sponsor to the database.
     * 
     * @param sponsor The sponsor to save
     * @return The saved sponsor with generated ID
     * @throws Exception if there is an error saving the sponsor
     */
    Sponsor save(Sponsor sponsor) throws Exception;

    /**
     * Updates an existing sponsor in the database.
     * 
     * @param sponsor The sponsor to update
     * @return true if the update was successful, false otherwise
     * @throws Exception if there is an error updating the sponsor
     */
    boolean update(Sponsor sponsor) throws Exception;

    /**
     * Deletes a sponsor from the database.
     * 
     * @param sponsorId The ID of the sponsor to delete
     * @return true if the deletion was successful, false otherwise
     * @throws Exception if there is an error deleting the sponsor
     */
    boolean delete(int sponsorId) throws Exception;

    /**
     * Finds a sponsor by their ID.
     * 
     * @param sponsorId The ID of the sponsor to find
     * @return The found sponsor, or null if not found
     * @throws Exception if there is an error finding the sponsor
     */
    Sponsor findById(int sponsorId) throws Exception;

    /**
     * Retrieves all sponsors from the database.
     * 
     * @return A list of all sponsors
     * @throws Exception if there is an error retrieving the sponsors
     */
    List<Sponsor> findAll() throws Exception;

    /**
     * Finds all sponsors associated with a specific event.
     * 
     * @param eventId The ID of the event
     * @return A list of sponsors for the event
     * @throws Exception if there is an error finding the sponsors
     */
    List<Sponsor> findByEventId(int eventId) throws Exception;

    /**
     * Finds all sponsors with a specific status.
     * 
     * @param status The status to search for
     * @return A list of sponsors with the specified status
     * @throws Exception if there is an error finding the sponsors
     */
    List<Sponsor> findByStatus(Sponsor.SponsorStatus status) throws Exception;

    /**
     * Finds all sponsors of a specific type.
     * 
     * @param type The type to search for
     * @return A list of sponsors of the specified type
     * @throws Exception if there is an error finding the sponsors
     */
    List<Sponsor> findByType(Sponsor.SponsorType type) throws Exception;

    /**
     * Calculates the total sponsorship amount for all sponsors.
     * 
     * @return The total sponsorship amount
     * @throws Exception if there is an error calculating the total
     */
    double getTotalSponsorshipAmount() throws Exception;

    /**
     * Gets the count of all sponsors.
     * 
     * @return The number of sponsors
     * @throws Exception if there is an error counting the sponsors
     */
    int getSponsorCount() throws Exception;

    /**
     * Checks if a sponsor is associated with any events.
     * 
     * @param sponsorId The ID of the sponsor to check
     * @return true if the sponsor is associated with any events, false otherwise
     * @throws Exception if there is an error checking the association
     */
    boolean isSponsorAssociated(int sponsorId) throws Exception;
}