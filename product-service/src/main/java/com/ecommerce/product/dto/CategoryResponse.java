package com.ecommerce.product.dto;

public class CategoryResponse {
    private Long id; private String name; private String description; private String imageUrl; private boolean active;
    public CategoryResponse() {}
    public CategoryResponse(Long id, String name, String description, String imageUrl, boolean active) {
        this.id=id; this.name=name; this.description=description; this.imageUrl=imageUrl; this.active=active;
    }
    public Long getId() { return id; } public void setId(Long id) { this.id=id; }
    public String getName() { return name; } public void setName(String n) { this.name=n; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description=d; }
    public String getImageUrl() { return imageUrl; } public void setImageUrl(String u) { this.imageUrl=u; }
    public boolean isActive() { return active; } public void setActive(boolean a) { this.active=a; }
}
