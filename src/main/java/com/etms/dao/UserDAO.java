package com.etms.dao;

import com.etms.model.User;
import java.util.List;

public interface UserDAO extends BaseDAO<User> {
    User findByEmail(String email) throws Exception;

    User authenticate(String email, String password) throws Exception;

    List<User> findByUserType(User.UserType userType) throws Exception;

    boolean changePassword(int userId, String oldPassword, String newPassword) throws Exception;

    boolean isEmailExists(String email) throws Exception;
}