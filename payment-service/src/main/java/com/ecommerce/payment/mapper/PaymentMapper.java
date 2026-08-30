package com.ecommerce.payment.mapper;

import com.ecommerce.common.entity.Payment;
import com.ecommerce.payment.dto.PaymentResponse;

public class PaymentMapper {
    private PaymentMapper() {}
    public static PaymentResponse toResponse(Payment p) {
        return new PaymentResponse(p.getId(), p.getPaymentNumber(), p.getTransactionId(),
                p.getStatus()!=null?p.getStatus().name():null, p.getPaymentMethod(), p.getAmount(),
                p.getOrder()!=null?p.getOrder().getId():null, p.getCreatedAt());
    }
}
