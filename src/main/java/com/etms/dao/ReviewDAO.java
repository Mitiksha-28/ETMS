package com.etms.dao;

import com.etms.model.ReviewRating;
import java.util.List;

public interface ReviewDAO {
    
    void save(ReviewRating review) throws Exception;

    void update(ReviewRating review) throws Exception;

    void delete(int reviewId) throws Exception;

    ReviewRating findById(int reviewId) throws Exception;

    List<ReviewRating> findAll() throws Exception;

    List<ReviewRating> findByEventId(int eventId) throws Exception;

    List<ReviewRating> findByUserId(int userId) throws Exception;

    List<ReviewRating> findByRating(int rating) throws Exception;

    double getAverageRating(int eventId) throws Exception;

    int getReviewCount(int eventId) throws Exception;

    boolean hasUserReviewed(int userId, int eventId) throws Exception;
}