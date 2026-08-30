package com.ecommerce.payment.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.common.entity.Order;
import com.ecommerce.common.entity.Payment;
import com.ecommerce.common.entity.PaymentStatus;
import com.ecommerce.payment.dto.CreatePaymentRequest;
import com.ecommerce.payment.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public Payment createPayment(CreatePaymentRequest req) {
        Payment p = Payment.builder()
                .paymentNumber("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .transactionId(UUID.randomUUID().toString())
                .paymentMethod(req.getPaymentMethod())
                .amount(req.getAmount())
                .order(Order.builder().id(req.getOrderId()).build())
                .build();
        return paymentRepository.save(p);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + id));
    }

    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for order: " + orderId));
    }

    @Override
    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Payment updatePaymentStatus(Long id, PaymentStatus status) {
        Payment p = getPaymentById(id);
        p.setStatus(status);
        return paymentRepository.save(p);
    }
}
