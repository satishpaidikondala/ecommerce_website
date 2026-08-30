package com.ecommerce.payment.service;

import java.util.List;
import com.ecommerce.common.entity.Payment;
import com.ecommerce.common.entity.PaymentStatus;
import com.ecommerce.payment.dto.CreatePaymentRequest;

public interface PaymentService {
    Payment createPayment(CreatePaymentRequest req);
    Payment getPaymentById(Long id);
    Payment getPaymentByOrderId(Long orderId);
    List<Payment> getPaymentsByStatus(PaymentStatus status);
    Payment updatePaymentStatus(Long id, PaymentStatus status);
}
