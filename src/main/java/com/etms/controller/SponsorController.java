package com.etms.controller;

import com.etms.dao.SponsorDAO;
import com.etms.daoimpl.SponsorDAOImpl;
import com.etms.exception.ETMSException;
import com.etms.model.Sponsor;

import java.sql.SQLException;
import java.util.List;

// Handles sponsor-related operations.
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

    // Create a new sponsor
    public Sponsor createSponsor(Sponsor sponsor) throws ETMSException {
        try {
            return sponsorDAO.save(sponsor);
        } catch (Exception e) {
            throw new ETMSException("Error creating sponsor: " + e.getMessage());
        }
    }

    // Get sponsor by ID
    public Sponsor getSponsorById(int sponsorId) throws ETMSException {
        try {
            return sponsorDAO.findById(sponsorId);
        } catch (Exception e) {
            throw new ETMSException("Error retrieving sponsor: " + e.getMessage());
        }
    }

    // Update sponsor details
    public boolean updateSponsor(Sponsor sponsor) throws ETMSException {
        try {
            return sponsorDAO.update(sponsor);
        } catch (Exception e) {
            throw new ETMSException("Error updating sponsor: " + e.getMessage());
        }
    }

    // Delete sponsor by ID
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

    // Get all sponsors
    public List<Sponsor> getAllSponsors() throws ETMSException {
        try {
            return sponsorDAO.findAll();
        } catch (Exception e) {
            throw new ETMSException("Error retrieving sponsors: " + e.getMessage());
        }
    }

    // Get sponsors for an event
    public List<Sponsor> getSponsorsByEventId(int eventId) throws ETMSException {
        try {
            return sponsorDAO.findByEventId(eventId);
        } catch (Exception e) {
            throw new ETMSException("Error retrieving sponsors for event: " + e.getMessage());
        }
    }

    // Get sponsors by status
    public List<Sponsor> getSponsorsByStatus(Sponsor.SponsorStatus status) throws ETMSException {
        try {
            return sponsorDAO.findByStatus(status);
        } catch (Exception e) {
            throw new ETMSException("Error retrieving sponsors by status: " + e.getMessage());
        }
    }

    // Get sponsors by type
    public List<Sponsor> getSponsorsByType(Sponsor.SponsorType type) throws ETMSException {
        try {
            return sponsorDAO.findByType(type);
        } catch (Exception e) {
            throw new ETMSException("Error retrieving sponsors by type: " + e.getMessage());
        }
    }

    // Get total sponsorship amount
    public double getTotalSponsorshipAmount() throws ETMSException {
        try {
            return sponsorDAO.getTotalSponsorshipAmount();
        } catch (Exception e) {
            throw new ETMSException("Error calculating total sponsorship amount: " + e.getMessage());
        }
    }

    // Get total number of sponsors
    public int getSponsorCount() throws ETMSException {
        try {
            return sponsorDAO.getSponsorCount();
        } catch (Exception e) {
            throw new ETMSException("Error getting sponsor count: " + e.getMessage());
        }
    }

    // Check sponsor-event association
    public boolean isSponsorAssociated(int sponsorId) throws ETMSException {
        try {
            return sponsorDAO.isSponsorAssociated(sponsorId);
        } catch (Exception e) {
            throw new ETMSException("Error checking sponsor association: " + e.getMessage());
        }
    }
}
