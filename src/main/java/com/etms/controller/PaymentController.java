package com.etms.controller;

import com.etms.dao.PaymentDAO;
import com.etms.daoimpl.PaymentDAOImpl;
import com.etms.exception.ETMSException;
import com.etms.model.Payment;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class for payment-related operations.
 * This class handles the business logic for payment management.
 */
public class PaymentController {
    private final PaymentDAO paymentDAO;

    /**
     * Constructs a new PaymentController with the default PaymentDAO
     * implementation.
     */
    public PaymentController() {
        this.paymentDAO = new PaymentDAOImpl();
    }

    /**
     * Constructs a new PaymentController with the specified PaymentDAO.
     * This constructor is mainly used for testing purposes.
     * 
     * @param paymentDAO the PaymentDAO to use
     */
    public PaymentController(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    /**
     * Creates a new payment.
     * 
     * @param payment the payment to create
     * @throws ETMSException if an error occurs during creation
     */
    public void createPayment(Payment payment) throws ETMSException {
        try {
            paymentDAO.save(payment);
        } catch (Exception e) {
            throw new ETMSException("Failed to create payment: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a payment by its ID.
     * 
     * @param paymentId the payment's ID
     * @return the payment, or null if not found
     * @throws ETMSException if an error occurs during retrieval
     */
    public Payment getPaymentById(int paymentId) throws ETMSException {
        try {
            return paymentDAO.findById(paymentId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve payment: " + e.getMessage(), e);
        }
    }

    /**
     * Updates a payment's information.
     * 
     * @param payment the payment to update
     * @throws ETMSException if an error occurs during update
     */
    public void updatePayment(Payment payment) throws ETMSException {
        try {
            paymentDAO.update(payment);
        } catch (Exception e) {
            throw new ETMSException("Failed to update payment: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a payment.
     * 
     * @param paymentId the ID of the payment to delete
     * @throws ETMSException if an error occurs during deletion
     */
    public void deletePayment(int paymentId) throws ETMSException {
        try {
            paymentDAO.delete(paymentId);
        } catch (Exception e) {
            throw new ETMSException("Failed to delete payment: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all payments.
     * 
     * @return a list of all payments
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Payment> getAllPayments() throws ETMSException {
        try {
            return paymentDAO.findAll();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve payments: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all payments for a specific user.
     * 
     * @param userId the user's ID
     * @return a list of payments for the specified user
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Payment> getPaymentsByUserId(int userId) throws ETMSException {
        try {
            return paymentDAO.findByUserId(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve payments: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all payments with a specific status.
     * 
     * @param status the status of payments to retrieve
     * @return a list of payments with the specified status
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Payment> getPaymentsByStatus(Payment.PaymentStatus status) throws ETMSException {
        try {
            return paymentDAO.findByStatus(status);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve payments: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all payments within a date range.
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return a list of payments within the date range
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Payment> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws ETMSException {
        try {
            return paymentDAO.findByDateRange(startDate, endDate);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve payments: " + e.getMessage(), e);
        }
    }

    /**
     * Updates the status of a payment.
     * 
     * @param paymentId the payment's ID
     * @param status    the new status
     * @return true if the update was successful, false otherwise
     * @throws ETMSException if an error occurs during update
     */
    public boolean updatePaymentStatus(int paymentId, Payment.PaymentStatus status) throws ETMSException {
        try {
            return paymentDAO.updatePaymentStatus(paymentId, status);
        } catch (Exception e) {
            throw new ETMSException("Failed to update payment status: " + e.getMessage(), e);
        }
    }

    /**
     * Gets the total revenue for a date range.
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return the total revenue
     * @throws ETMSException if an error occurs during calculation
     */
    public double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) throws ETMSException {
        try {
            return paymentDAO.getTotalRevenue(startDate, endDate);
        } catch (Exception e) {
            throw new ETMSException("Failed to calculate total revenue: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all pending payments.
     * 
     * @return a list of pending payments
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Payment> getPendingPayments() throws ETMSException {
        try {
            return paymentDAO.findPendingPayments();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve pending payments: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all failed payments.
     * 
     * @return a list of failed payments
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Payment> getFailedPayments() throws ETMSException {
        try {
            return paymentDAO.findFailedPayments();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve failed payments: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all refunded payments.
     * 
     * @return a list of refunded payments
     * @throws ETMSException if an error occurs during retrieval
     */
    public List<Payment> getRefundedPayments() throws ETMSException {
        try {
            return paymentDAO.findRefundedPayments();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve refunded payments: " + e.getMessage(), e);
        }
    }
}