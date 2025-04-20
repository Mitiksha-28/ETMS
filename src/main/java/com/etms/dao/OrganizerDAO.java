package com.etms.dao;

import com.etms.model.Organizer;
import java.util.List;

public interface OrganizerDAO {
    
    void save(Organizer organizer) throws Exception;

    void update(Organizer organizer) throws Exception;

    void delete(int organizerId) throws Exception;

    Organizer findById(int organizerId) throws Exception;

    Organizer findByEmail(String email) throws Exception;

    List<Organizer> findAll() throws Exception;

    List<Organizer> findByStatus(Organizer.OrganizerStatus status) throws Exception;

    boolean isEmailExists(String email) throws Exception;

    Organizer authenticate(String email, String password) throws Exception;

    boolean changePassword(int organizerId, String oldPassword, String newPassword) throws Exception;
    
}