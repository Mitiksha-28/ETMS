package com.etms.dao;

import com.etms.model.Venue;
import java.util.List;

/**
 * Data Access Object interface for Venue entity.
 * This interface defines methods for managing venue data in the database.
 */
public interface VenueDAO {
    /**
     * Saves a new venue to the database.
     * 
     * @param venue the venue to save
     * @throws Exception if an error occurs during the save operation
     */
    void save(Venue venue) throws Exception;

    /**
     * Updates an existing venue in the database.
     * 
     * @param venue the venue to update
     * @throws Exception if an error occurs during the update operation
     */
    void update(Venue venue) throws Exception;

    /**
     * Deletes a venue from the database.
     * 
     * @param venueId the ID of the venue to delete
     * @throws Exception if an error occurs during the delete operation
     */
    void delete(int venueId) throws Exception;

    /**
     * Finds a venue by its ID.
     * 
     * @param venueId the ID of the venue to find
     * @return the venue, or null if not found
     * @throws Exception if an error occurs during the find operation
     */
    Venue findById(int venueId) throws Exception;

    /**
     * Finds a venue by its name.
     * 
     * @param venueName the name of the venue to find
     * @return the venue, or null if not found
     * @throws Exception if an error occurs during the find operation
     */
    Venue findByName(String venueName) throws Exception;

    /**
     * Finds all venues in the database.
     * 
     * @return a list of all venues
     * @throws Exception if an error occurs during the find operation
     */
    List<Venue> findAll() throws Exception;

    /**
     * Finds all venues in a specific location.
     * 
     * @param location the location to search for
     * @return a list of venues in the specified location
     * @throws Exception if an error occurs during the find operation
     */
    List<Venue> findByLocation(String location) throws Exception;

    /**
     * Finds all venues with capacity greater than or equal to the specified
     * capacity.
     * 
     * @param minCapacity the minimum capacity required
     * @return a list of venues meeting the capacity requirement
     * @throws Exception if an error occurs during the find operation
     */
    List<Venue> findByMinCapacity(int minCapacity) throws Exception;

    /**
     * Checks if a venue name already exists in the database.
     * 
     * @param venueName the venue name to check
     * @return true if the venue name exists, false otherwise
     * @throws Exception if an error occurs during the check operation
     */
    boolean isVenueNameExists(String venueName) throws Exception;

    /**
     * Gets the total number of venues in the database.
     * 
     * @return the total number of venues
     * @throws Exception if an error occurs during the count operation
     */
    int getVenueCount() throws Exception;

    /**
     * Gets the total capacity of all venues.
     * 
     * @return the total capacity
     * @throws Exception if an error occurs during the calculation
     */
    int getTotalCapacity() throws Exception;

    /**
     * Checks if a venue is associated with any events.
     * 
     * @param venueId the ID of the venue to check
     * @return true if the venue is associated with any events, false otherwise
     * @throws Exception if an error occurs during the check operation
     */
    boolean isVenueAssociated(int venueId) throws Exception;
}