package com.ecommerce.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {
    private Long id; private String name; private String description;
    private BigDecimal price; private String sku; private String imageUrl;
    private boolean active; private Integer unitsInStock;
    private Long categoryId; private String categoryName; private LocalDateTime createdAt;
    public ProductResponse() {}
    public ProductResponse(Long id, String name, String description, BigDecimal price, String sku,
                           String imageUrl, boolean active, Integer unitsInStock,
                           Long categoryId, String categoryName, LocalDateTime createdAt) {
        this.id=id; this.name=name; this.description=description; this.price=price; this.sku=sku;
        this.imageUrl=imageUrl; this.active=active; this.unitsInStock=unitsInStock;
        this.categoryId=categoryId; this.categoryName=categoryName; this.createdAt=createdAt;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id=id; }
    public String getName() { return name; } public void setName(String n) { this.name=n; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description=d; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal p) { this.price=p; }
    public String getSku() { return sku; } public void setSku(String s) { this.sku=s; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String u) { this.imageUrl=u; }
    public boolean isActive() { return active; } public void setActive(boolean a) { this.active=a; }
    public Integer getUnitsInStock() { return unitsInStock; } public void setUnitsInStock(Integer u) { this.unitsInStock=u; }
    public Long getCategoryId() { return categoryId; } public void setCategoryId(Long c) { this.categoryId=c; }
    public String getCategoryName() { return categoryName; } public void setCategoryName(String c) { this.categoryName=c; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime c) { this.createdAt=c; }
}
