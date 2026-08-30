package com.ecommerce.payment.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CreatePaymentRequest {
    @NotNull private Long orderId;
    @NotBlank private String paymentMethod;
    @NotNull @DecimalMin("0.01") private BigDecimal amount;
    public CreatePaymentRequest() {}
    public Long getOrderId() { return orderId; } public void setOrderId(Long o) { this.orderId=o; }
    public String getPaymentMethod() { return paymentMethod; } public void setPaymentMethod(String p) { this.paymentMethod=p; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal a) { this.amount=a; }
}
