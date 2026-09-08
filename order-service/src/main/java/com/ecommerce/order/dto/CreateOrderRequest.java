package com.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {
    @NotNull private Long userId;
    @NotBlank private String shippingAddress;
    @NotBlank private String shippingCity; private String shippingState;
    @NotBlank private String shippingZip; @NotBlank private String shippingCountry;
    public CreateOrderRequest() {}
    public Long getUserId() { return userId; } public void setUserId(Long u) { this.userId=u; }
    public String getShippingAddress() { return shippingAddress; } public void setShippingAddress(String s) { this.shippingAddress=s; }
    public String getShippingCity() { return shippingCity; } public void setShippingCity(String s) { this.shippingCity=s; }
    public String getShippingState() { return shippingState; } public void setShippingState(String s) { this.shippingState=s; }
    public String getShippingZip() { return shippingZip; } public void setShippingZip(String s) { this.shippingZip=s; }
    public String getShippingCountry() { return shippingCountry; } public void setShippingCountry(String s) { this.shippingCountry=s; }
}
