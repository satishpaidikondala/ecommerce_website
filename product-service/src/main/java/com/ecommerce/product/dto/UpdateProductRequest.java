package com.ecommerce.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class UpdateProductRequest {
    private String name;
    private String description;
    @DecimalMin("0.01") private BigDecimal price;
    private String imageUrl;
    @Min(0) private Integer unitsInStock;
    private String sku;
    private Long categoryId;

    public UpdateProductRequest() {}
    public String getName() { return name; } public void setName(String n) { this.name = n; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description = d; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal p) { this.price = p; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String u) { this.imageUrl = u; }
    public Integer getUnitsInStock() { return unitsInStock; } public void setUnitsInStock(Integer u) { this.unitsInStock = u; }
    public String getSku() { return sku; } public void setSku(String s) { this.sku = s; }
    public Long getCategoryId() { return categoryId; } public void setCategoryId(Long c) { this.categoryId = c; }
}
