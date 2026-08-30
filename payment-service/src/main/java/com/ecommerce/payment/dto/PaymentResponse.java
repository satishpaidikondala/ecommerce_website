package com.ecommerce.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
    private Long id; private String paymentNumber; private String transactionId;
    private String status; private String paymentMethod; private BigDecimal amount;
    private Long orderId; private LocalDateTime createdAt;
    public PaymentResponse() {}
    public PaymentResponse(Long id, String paymentNumber, String transactionId, String status,
                           String paymentMethod, BigDecimal amount, Long orderId, LocalDateTime createdAt) {
        this.id=id; this.paymentNumber=paymentNumber; this.transactionId=transactionId;
        this.status=status; this.paymentMethod=paymentMethod; this.amount=amount;
        this.orderId=orderId; this.createdAt=createdAt;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id=id; }
    public String getPaymentNumber() { return paymentNumber; } public void setPaymentNumber(String p) { this.paymentNumber=p; }
    public String getTransactionId() { return transactionId; } public void setTransactionId(String t) { this.transactionId=t; }
    public String getStatus() { return status; } public void setStatus(String s) { this.status=s; }
    public String getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(String p) { this.paymentMethod=p; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal a) { this.amount=a; }
    public Long getOrderId() { return orderId; } public void setOrderId(Long o) { this.orderId=o; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime c) { this.createdAt=c; }
}
