package com.etms.dao;

import com.etms.model.Payment;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentDAO extends BaseDAO<Payment> {
    List<Payment> findByUserId(int userId) throws Exception;

    List<Payment> findByStatus(Payment.PaymentStatus status) throws Exception;

    List<Payment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws Exception;

    boolean updatePaymentStatus(int paymentId, Payment.PaymentStatus status) throws Exception;

    double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) throws Exception;

    List<Payment> findPendingPayments() throws Exception;

    List<Payment> findFailedPayments() throws Exception;

    List<Payment> findRefundedPayments() throws Exception;
}