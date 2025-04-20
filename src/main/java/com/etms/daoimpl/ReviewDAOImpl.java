package com.etms.daoimpl;

import com.etms.dao.ReviewDAO;
import com.etms.model.ReviewRating;
import com.etms.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ReviewDAO interface.
 * Handles all database operations for the ReviewRating entity.
 */
public class ReviewDAOImpl implements ReviewDAO {
    private final Connection connection;

    public ReviewDAOImpl() throws SQLException {
        this.connection = DatabaseUtil.getConnection();
    }

    @Override
    public void save(ReviewRating review) throws Exception {
        String sql = "INSERT INTO reviews (user_id, event_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, review.getUserId());
            stmt.setInt(2, review.getEventId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.setTimestamp(5, Timestamp.valueOf(review.getReviewDate()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating review failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    review.setReviewId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating review failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public void update(ReviewRating review) throws Exception {
        String sql = "UPDATE reviews SET user_id = ?, event_id = ?, rating = ?, comment = ?, review_date = ? WHERE review_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, review.getUserId());
            stmt.setInt(2, review.getEventId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.setTimestamp(5, Timestamp.valueOf(review.getReviewDate()));
            stmt.setInt(6, review.getReviewId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating review failed, no rows affected.");
            }
        }
    }

    @Override
    public void delete(int reviewId) throws Exception {
        String sql = "DELETE FROM reviews WHERE review_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting review failed, no rows affected.");
            }
        }
    }

    @Override
    public ReviewRating findById(int reviewId) throws Exception {
        String sql = "SELECT * FROM reviews WHERE review_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reviewId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReview(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<ReviewRating> findAll() throws Exception {
        String sql = "SELECT * FROM reviews";
        List<ReviewRating> reviews = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
            return reviews;
        }
    }

    @Override
    public List<ReviewRating> findByEventId(int eventId) throws Exception {
        String sql = "SELECT * FROM reviews WHERE event_id = ?";
        List<ReviewRating> reviews = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
                return reviews;
            }
        }
    }

    @Override
    public List<ReviewRating> findByUserId(int userId) throws Exception {
        String sql = "SELECT * FROM reviews WHERE user_id = ?";
        List<ReviewRating> reviews = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
                return reviews;
            }
        }
    }

    @Override
    public List<ReviewRating> findByRating(int rating) throws Exception {
        String sql = "SELECT * FROM reviews WHERE rating = ?";
        List<ReviewRating> reviews = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rating);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
                return reviews;
            }
        }
    }

    @Override
    public double getAverageRating(int eventId) throws Exception {
        String sql = "SELECT AVG(rating) FROM reviews WHERE event_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
                return 0.0;
            }
        }
    }

    @Override
    public int getReviewCount(int eventId) throws Exception {
        String sql = "SELECT COUNT(*) FROM reviews WHERE event_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    @Override
    public boolean hasUserReviewed(int userId, int eventId) throws Exception {
        String sql = "SELECT COUNT(*) FROM reviews WHERE user_id = ? AND event_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, eventId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        }
    }

    private ReviewRating mapResultSetToReview(ResultSet rs) throws SQLException {
        ReviewRating review = new ReviewRating();
        review.setReviewId(rs.getInt("review_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setEventId(rs.getInt("event_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setReviewDate(rs.getTimestamp("review_date").toLocalDateTime());
        return review;
    }
}