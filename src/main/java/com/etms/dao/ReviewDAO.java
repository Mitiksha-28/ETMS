package com.etms.dao;

import com.etms.model.ReviewRating;
import java.util.List;

/**
 * Data Access Object interface for ReviewRating entity.
 * This interface defines methods for managing review data in the database.
 */
public interface ReviewDAO {
    /**
     * Saves a new review to the database.
     * 
     * @param review the review to save
     * @throws Exception if an error occurs during the save operation
     */
    void save(ReviewRating review) throws Exception;

    /**
     * Updates an existing review in the database.
     * 
     * @param review the review to update
     * @throws Exception if an error occurs during the update operation
     */
    void update(ReviewRating review) throws Exception;

    /**
     * Deletes a review from the database.
     * 
     * @param reviewId the ID of the review to delete
     * @throws Exception if an error occurs during the delete operation
     */
    void delete(int reviewId) throws Exception;

    /**
     * Finds a review by its ID.
     * 
     * @param reviewId the ID of the review to find
     * @return the review, or null if not found
     * @throws Exception if an error occurs during the find operation
     */
    ReviewRating findById(int reviewId) throws Exception;

    /**
     * Finds all reviews in the database.
     * 
     * @return a list of all reviews
     * @throws Exception if an error occurs during the find operation
     */
    List<ReviewRating> findAll() throws Exception;

    /**
     * Finds all reviews for a specific event.
     * 
     * @param eventId the ID of the event
     * @return a list of reviews for the specified event
     * @throws Exception if an error occurs during the find operation
     */
    List<ReviewRating> findByEventId(int eventId) throws Exception;

    /**
     * Finds all reviews by a specific user.
     * 
     * @param userId the ID of the user
     * @return a list of reviews by the specified user
     * @throws Exception if an error occurs during the find operation
     */
    List<ReviewRating> findByUserId(int userId) throws Exception;

    /**
     * Finds all reviews with a specific rating.
     * 
     * @param rating the rating to find
     * @return a list of reviews with the specified rating
     * @throws Exception if an error occurs during the find operation
     */
    List<ReviewRating> findByRating(int rating) throws Exception;

    /**
     * Gets the average rating for an event.
     * 
     * @param eventId the ID of the event
     * @return the average rating, or 0 if no reviews exist
     * @throws Exception if an error occurs during the calculation
     */
    double getAverageRating(int eventId) throws Exception;

    /**
     * Gets the total number of reviews for an event.
     * 
     * @param eventId the ID of the event
     * @return the total number of reviews
     * @throws Exception if an error occurs during the count
     */
    int getReviewCount(int eventId) throws Exception;

    /**
     * Checks if a user has already reviewed an event.
     * 
     * @param userId  the ID of the user
     * @param eventId the ID of the event
     * @return true if the user has reviewed the event, false otherwise
     * @throws Exception if an error occurs during the check
     */
    boolean hasUserReviewed(int userId, int eventId) throws Exception;
}