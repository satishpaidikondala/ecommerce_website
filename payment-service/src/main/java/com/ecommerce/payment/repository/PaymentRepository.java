package com.ecommerce.payment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.Payment;
import com.ecommerce.common.entity.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Q1: Find by transaction ID (unique)
    Optional<Payment> findByTransactionId(String transactionId);

    // Q2: Find payment for a specific order
    Optional<Payment> findByOrderId(Long orderId);

    // Q3: Find all successful payments
    List<Payment> findByStatus(PaymentStatus status);

    // Q4: Find all UPI payments
    List<Payment> findByPaymentMethod(String paymentMethod);

    // Q5: Find payments in date range
    List<Payment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Q6: Find UPI + Success + Date range
    List<Payment> findByPaymentMethodAndStatusAndCreatedAtBetween(
        String paymentMethod, 
        PaymentStatus status, 
        LocalDateTime start, 
        LocalDateTime end
    );

    // Q7: Find by payment number (unique)
    java.util.Optional<Payment> findByPaymentNumber(String paymentNumber);
}