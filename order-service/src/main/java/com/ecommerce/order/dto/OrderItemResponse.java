package com.ecommerce.order.dto;

import java.math.BigDecimal;

public class OrderItemResponse {
    private Long productId; private String productName;
    private Integer quantity; private BigDecimal price; private BigDecimal subtotal;
    public OrderItemResponse() {}
    public OrderItemResponse(Long productId, String productName, Integer quantity, BigDecimal price, BigDecimal subtotal) {
        this.productId=productId; this.productName=productName; this.quantity=quantity; this.price=price; this.subtotal=subtotal;
    }
    public Long getProductId() { return productId; } public void setProductId(Long p) { this.productId=p; }
    public String getProductName() { return productName; } public void setProductName(String p) { this.productName=p; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer q) { this.quantity=q; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal p) { this.price=p; }
    public BigDecimal getSubtotal() { return subtotal; } public void setSubtotal(BigDecimal s) { this.subtotal=s; }
}
