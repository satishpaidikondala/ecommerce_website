package com.ecommerce.product.dto;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {
    @NotBlank private String name;
    private String description;
    private String imageUrl;
    public CategoryRequest() {}
    public String getName() { return name; } public void setName(String n) { this.name=n; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description=d; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String u) { this.imageUrl=u; }
}
