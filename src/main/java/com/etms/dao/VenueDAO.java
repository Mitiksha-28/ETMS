package com.etms.dao;

import com.etms.model.Venue;
import java.util.List;

public interface VenueDAO {
    
    void save(Venue venue) throws Exception;

    void update(Venue venue) throws Exception;

    void delete(int venueId) throws Exception;

    Venue findById(int venueId) throws Exception;

    Venue findByName(String venueName) throws Exception;

    List<Venue> findAll() throws Exception;

    List<Venue> findByLocation(String location) throws Exception;

    List<Venue> findByMinCapacity(int minCapacity) throws Exception;

    boolean isVenueNameExists(String venueName) throws Exception;

    int getVenueCount() throws Exception;

    int getTotalCapacity() throws Exception;

    boolean isVenueAssociated(int venueId) throws Exception;
}