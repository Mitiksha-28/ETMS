package com.etms.controller;

import com.etms.dao.SponsorDAO;
import com.etms.daoimpl.SponsorDAOImpl;
import com.etms.exception.ETMSException;
import com.etms.model.Sponsor;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller class for handling sponsor-related business logic.
 */
public class SponsorController {
    private final SponsorDAO sponsorDAO;

    public SponsorController() throws ETMSException {
        try {
            this.sponsorDAO = new SponsorDAOImpl();
        } catch (SQLException e) {
            throw new ETMSException("Error initializing sponsor DAO: " + e.getMessage());
        }
    }

    public SponsorController(SponsorDAO sponsorDAO) {
        this.sponsorDAO = sponsorDAO;
    }

    /**
     * Creates a new sponsor.
     *
     * @param sponsor The sponsor to create
     * @return The created sponsor with generated ID
     * @throws ETMSException if there is an error creating the sponsor
     */
    public Sponsor createSponsor(Sponsor sponsor) throws ETMSException {
        try {
            return sponsorDAO.save(sponsor);
        } catch (Exception e) {
            throw new ETMSException("Error creating sponsor: " + e.getMessage());
        }
    }

    /**
     * Retrieves a sponsor by ID.
     *
     * @param sponsorId The ID of the sponsor to retrieve
     * @return The sponsor if found, null otherwise
     * @throws ETMSException if there is an error retrieving the sponsor
     */
    public Sponsor getSponsorById(int sponsorId) throws ETMSException {
        try {
            return sponsorDAO.findById(sponsorId);
        } catch (Exception e) {
            throw new ETMSException("Error retrieving sponsor: " + e.getMessage());
        }
    }

    /**
     * Updates a sponsor's information.
     *
     * @param sponsor The sponsor to update
     * @return true if the update was successful, false otherwise
     * @throws ETMSException if there is an error updating the sponsor
     */
    public boolean updateSponsor(Sponsor sponsor) throws ETMSException {
        try {
            return sponsorDAO.update(sponsor);
        } catch (Exception e) {
            throw new ETMSException("Error updating sponsor: " + e.getMessage());
        }
    }

    /**
     * Deletes a sponsor.
     *
     * @param sponsorId The ID of the sponsor to delete
     * @return true if the deletion was successful, false otherwise
     * @throws ETMSException if there is an error deleting the sponsor
     */
    public boolean deleteSponsor(int sponsorId) throws ETMSException {
        try {
            if (sponsorDAO.isSponsorAssociated(sponsorId)) {
                throw new ETMSException("Cannot delete sponsor that is associated with events");
            }
            return sponsorDAO.delete(sponsorId);
        } catch (Exception e) {
            throw new ETMSException("Error deleting sponsor: " + e.getMessage());
        }
    }

    /**
     * Retrieves all sponsors.
     *
     * @return List of all sponsors
     * @throws ETMSException if there is an error retrieving sponsors
     */
    public List<Sponsor> getAllSponsors() throws ETMSException {
        try {
            return sponsorDAO.findAll();
        } catch (Exception e) {
            throw new ETMSException("Error retrieving sponsors: " + e.getMessage());
        }
    }

    /**
     * Retrieves sponsors associated with a specific event.
     *
     * @param eventId The ID of the event
     * @return List of sponsors associated with the event
     * @throws ETMSException if there is an error retrieving sponsors
     */
    public List<Sponsor> getSponsorsByEventId(int eventId) throws ETMSException {
        try {
            return sponsorDAO.findByEventId(eventId);
        } catch (Exception e) {
            throw new ETMSException("Error retrieving sponsors for event: " + e.getMessage());
        }
    }

    /**
     * Retrieves sponsors by their status.
     *
     * @param status The status to filter by
     * @return List of sponsors with the specified status
     * @throws ETMSException if there is an error retrieving sponsors
     */
    public List<Sponsor> getSponsorsByStatus(Sponsor.SponsorStatus status) throws ETMSException {
        try {
            return sponsorDAO.findByStatus(status);
        } catch (Exception e) {
            throw new ETMSException("Error retrieving sponsors by status: " + e.getMessage());
        }
    }

    /**
     * Retrieves sponsors by their type.
     *
     * @param type The type to filter by
     * @return List of sponsors of the specified type
     * @throws ETMSException if there is an error retrieving sponsors
     */
    public List<Sponsor> getSponsorsByType(Sponsor.SponsorType type) throws ETMSException {
        try {
            return sponsorDAO.findByType(type);
        } catch (Exception e) {
            throw new ETMSException("Error retrieving sponsors by type: " + e.getMessage());
        }
    }

    /**
     * Calculates the total sponsorship amount.
     *
     * @return The total sponsorship amount
     * @throws ETMSException if there is an error calculating the total
     */
    public double getTotalSponsorshipAmount() throws ETMSException {
        try {
            return sponsorDAO.getTotalSponsorshipAmount();
        } catch (Exception e) {
            throw new ETMSException("Error calculating total sponsorship amount: " + e.getMessage());
        }
    }

    /**
     * Gets the total count of sponsors.
     *
     * @return The total number of sponsors
     * @throws ETMSException if there is an error getting the count
     */
    public int getSponsorCount() throws ETMSException {
        try {
            return sponsorDAO.getSponsorCount();
        } catch (Exception e) {
            throw new ETMSException("Error getting sponsor count: " + e.getMessage());
        }
    }

    /**
     * Checks if a sponsor is associated with any events.
     *
     * @param sponsorId The ID of the sponsor to check
     * @return true if the sponsor is associated with events, false otherwise
     * @throws ETMSException if there is an error checking the association
     */
    public boolean isSponsorAssociated(int sponsorId) throws ETMSException {
        try {
            return sponsorDAO.isSponsorAssociated(sponsorId);
        } catch (Exception e) {
            throw new ETMSException("Error checking sponsor association: " + e.getMessage());
        }
    }
}