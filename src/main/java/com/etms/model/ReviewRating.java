package com.etms.model;

import java.time.LocalDateTime;

public class ReviewRating {
    private int reviewId;
    private int userId;
    private int eventId;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;

    public ReviewRating() {
    }

    public ReviewRating(int reviewId, int userId, int eventId, int rating,
            String comment, LocalDateTime reviewDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.eventId = eventId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Override
    public String toString() {
        return "ReviewRating{" +
                "reviewId=" + reviewId +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", reviewDate=" + reviewDate +
                '}';
    }
}