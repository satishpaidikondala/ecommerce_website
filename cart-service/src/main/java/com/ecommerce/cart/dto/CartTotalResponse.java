package com.ecommerce.cart.dto;

import java.math.BigDecimal;

public class CartTotalResponse {

    private final int totalItems;
    private final BigDecimal totalAmount;

    public CartTotalResponse(int totalItems, BigDecimal totalAmount) {
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
}
