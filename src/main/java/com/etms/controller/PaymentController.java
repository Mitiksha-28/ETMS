package com.etms.controller;

import com.etms.dao.PaymentDAO;
import com.etms.daoimpl.PaymentDAOImpl;
import com.etms.exception.ETMSException;
import com.etms.model.Payment;

import java.time.LocalDateTime;
import java.util.List;

public class PaymentController {
    private final PaymentDAO paymentDAO;

    public PaymentController() {
        this.paymentDAO = new PaymentDAOImpl();
    }

    public PaymentController(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    // Creates a payment
    public void createPayment(Payment payment) throws ETMSException {
        try {
            paymentDAO.save(payment);
        } catch (Exception e) {
            throw new ETMSException("Failed to create payment: " + e.getMessage(), e);
        }
    }

    // Gets a payment by ID
    public Payment getPaymentById(int paymentId) throws ETMSException {
        try {
            return paymentDAO.findById(paymentId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve payment: " + e.getMessage(), e);
        }
    }

    // Updates a payment
    public void updatePayment(Payment payment) throws ETMSException {
        try {
            paymentDAO.update(payment);
        } catch (Exception e) {
            throw new ETMSException("Failed to update payment: " + e.getMessage(), e);
        }
    }

    // Deletes a payment
    public void deletePayment(int paymentId) throws ETMSException {
        try {
            paymentDAO.delete(paymentId);
        } catch (Exception e) {
            throw new ETMSException("Failed to delete payment: " + e.getMessage(), e);
        }
    }

    // Gets all payments
    public List<Payment> getAllPayments() throws ETMSException {
        try {
            return paymentDAO.findAll();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve payments: " + e.getMessage(), e);
        }
    }

    // Gets payments by user ID
    public List<Payment> getPaymentsByUserId(int userId) throws ETMSException {
        try {
            return paymentDAO.findByUserId(userId);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve payments: " + e.getMessage(), e);
        }
    }

    // Gets payments by status
    public List<Payment> getPaymentsByStatus(Payment.PaymentStatus status) throws ETMSException {
        try {
            return paymentDAO.findByStatus(status);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve payments: " + e.getMessage(), e);
        }
    }

    // Gets payments in a date range
    public List<Payment> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws ETMSException {
        try {
            return paymentDAO.findByDateRange(startDate, endDate);
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve payments: " + e.getMessage(), e);
        }
    }

    // Updates payment status
    public boolean updatePaymentStatus(int paymentId, Payment.PaymentStatus status) throws ETMSException {
        try {
            return paymentDAO.updatePaymentStatus(paymentId, status);
        } catch (Exception e) {
            throw new ETMSException("Failed to update payment status: " + e.getMessage(), e);
        }
    }

    // Gets total revenue in a date range
    public double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) throws ETMSException {
        try {
            return paymentDAO.getTotalRevenue(startDate, endDate);
        } catch (Exception e) {
            throw new ETMSException("Failed to calculate total revenue: " + e.getMessage(), e);
        }
    }

    // Gets pending payments
    public List<Payment> getPendingPayments() throws ETMSException {
        try {
            return paymentDAO.findPendingPayments();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve pending payments: " + e.getMessage(), e);
        }
    }

    // Gets failed payments
    public List<Payment> getFailedPayments() throws ETMSException {
        try {
            return paymentDAO.findFailedPayments();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve failed payments: " + e.getMessage(), e);
        }
    }

    // Gets refunded payments
    public List<Payment> getRefundedPayments() throws ETMSException {
        try {
            return paymentDAO.findRefundedPayments();
        } catch (Exception e) {
            throw new ETMSException("Failed to retrieve refunded payments: " + e.getMessage(), e);
        }
    }
}
