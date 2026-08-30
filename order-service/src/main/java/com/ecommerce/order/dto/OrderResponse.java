package com.ecommerce.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id; private String orderNumber; private String status;
    private BigDecimal totalAmount; private Long userId;
    private List<OrderItemResponse> items; private LocalDateTime createdAt;
    public OrderResponse() {}
    public OrderResponse(Long id, String orderNumber, String status, BigDecimal totalAmount,
                         Long userId, List<OrderItemResponse> items, LocalDateTime createdAt) {
        this.id=id; this.orderNumber=orderNumber; this.status=status; this.totalAmount=totalAmount;
        this.userId=userId; this.items=items; this.createdAt=createdAt;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id=id; }
    public String getOrderNumber() { return orderNumber; } public void setOrderNumber(String o) { this.orderNumber=o; }
    public String getStatus() { return status; } public void setStatus(String s) { this.status=s; }
    public BigDecimal getTotalAmount() { return totalAmount; } public void setTotalAmount(BigDecimal t) { this.totalAmount=t; }
    public Long getUserId() { return userId; } public void setUserId(Long u) { this.userId=u; }
    public List<OrderItemResponse> getItems() { return items; } public void setItems(List<OrderItemResponse> i) { this.items=i; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime c) { this.createdAt=c; }
}
