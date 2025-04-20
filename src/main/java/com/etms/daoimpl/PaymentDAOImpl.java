package com.etms.daoimpl;

import com.etms.dao.PaymentDAO;
import com.etms.model.Payment;
import com.etms.util.DatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {
    private static final Logger logger = LoggerFactory.getLogger(PaymentDAOImpl.class);

    @Override
    public Payment findById(int id) throws Exception {
        String sql = "SELECT * FROM Payment WHERE PaymentID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        }
        return null;
    }

    @Override
    public List<Payment> findAll() throws Exception {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment";
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        }
        return payments;
    }

    @Override
    public void save(Payment payment) throws Exception {
        String sql = "INSERT INTO Payment (UserID, Amount, Status, TransactionDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, payment.getUserId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getStatus().name().toUpperCase());
            stmt.setTimestamp(4, Timestamp.valueOf(payment.getTransactionDate()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                payment.setPaymentId(rs.getInt(1));
            }
        }
    }

    @Override
    public void update(Payment payment) throws Exception {
        String sql = "UPDATE Payment SET UserID = ?, Amount = ?, Status = ?, TransactionDate = ? WHERE PaymentID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getUserId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(payment.getTransactionDate()));
            stmt.setInt(5, payment.getPaymentId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Payment WHERE PaymentID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Payment> findByUserId(int userId) throws Exception {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment WHERE UserID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        }
        return payments;
    }

    @Override
    public List<Payment> findByStatus(Payment.PaymentStatus status) throws Exception {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment WHERE Status = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        }
        return payments;
    }

    @Override
    public List<Payment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment WHERE TransactionDate BETWEEN ? AND ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        }
        return payments;
    }

    @Override
    public boolean updatePaymentStatus(int paymentId, Payment.PaymentStatus status) throws Exception {
        String sql = "UPDATE Payment SET Status = ? WHERE PaymentID = ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, paymentId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        String sql = "SELECT SUM(Amount) as TotalRevenue FROM Payment " +
                "WHERE Status = 'COMPLETED' AND TransactionDate BETWEEN ? AND ?";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("TotalRevenue");
            }
        }
        return 0.0;
    }

    @Override
    public List<Payment> findPendingPayments() throws Exception {
        return findByStatus(Payment.PaymentStatus.PENDING);
    }

    @Override
    public List<Payment> findFailedPayments() throws Exception {
        return findByStatus(Payment.PaymentStatus.FAILED);
    }

    @Override
    public List<Payment> findRefundedPayments() throws Exception {
        return findByStatus(Payment.PaymentStatus.REFUNDED);
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("PaymentID"));
        payment.setUserId(rs.getInt("UserID"));
        payment.setAmount(rs.getBigDecimal("Amount"));
        String statusStr = rs.getString("Status");
        try {
            payment.setStatus(Payment.PaymentStatus.valueOf(statusStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            logger.error("Invalid payment status in database: {}", statusStr);
            payment.setStatus(Payment.PaymentStatus.PENDING); // Default to PENDING if status is invalid
        }
        payment.setTransactionDate(rs.getTimestamp("TransactionDate").toLocalDateTime());
        return payment;
    }
}