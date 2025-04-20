package com.etms.dao;

import com.etms.model.Sponsor;
import java.util.List;

public interface SponsorDAO {
    
    Sponsor save(Sponsor sponsor) throws Exception;

    boolean update(Sponsor sponsor) throws Exception;

    boolean delete(int sponsorId) throws Exception;

    Sponsor findById(int sponsorId) throws Exception;

    List<Sponsor> findAll() throws Exception;

    List<Sponsor> findByEventId(int eventId) throws Exception;

    List<Sponsor> findByStatus(Sponsor.SponsorStatus status) throws Exception;

    List<Sponsor> findByType(Sponsor.SponsorType type) throws Exception;

    double getTotalSponsorshipAmount() throws Exception;

    int getSponsorCount() throws Exception;

    boolean isSponsorAssociated(int sponsorId) throws Exception;
}