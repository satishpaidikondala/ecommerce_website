package com.ecommerce.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CreateProductRequest {
    @NotBlank private String name;
    private String description;
    @NotNull @DecimalMin("0.01") private BigDecimal price;
    @NotBlank private String sku;
    private String imageUrl;
    @NotNull @Min(0) private Integer unitsInStock;
    @NotNull private Long categoryId;

    public CreateProductRequest() {}
    public String getName() { return name; } public void setName(String n) { this.name = n; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description = d; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal p) { this.price = p; }
    public String getSku() { return sku; } public void setSku(String s) { this.sku = s; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String u) { this.imageUrl = u; }
    public Integer getUnitsInStock() { return unitsInStock; } public void setUnitsInStock(Integer u) { this.unitsInStock = u; }
    public Long getCategoryId() { return categoryId; } public void setCategoryId(Long c) { this.categoryId = c; }
}
