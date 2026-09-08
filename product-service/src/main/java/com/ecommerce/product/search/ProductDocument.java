package com.ecommerce.product.search;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "products", createIndex = true)
public class ProductDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String name;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Keyword)
    private String sku;

    @Field(type = FieldType.Scaled_Float, scalingFactor = 100)
    private BigDecimal price;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    @Field(type = FieldType.Boolean)
    private boolean active;

    public ProductDocument() {}

    public ProductDocument(String id, String name, String description, String sku,
                           BigDecimal price, String categoryName, boolean active) {
        this.id = id; this.name = name; this.description = description;
        this.sku = sku; this.price = price; this.categoryName = categoryName; this.active = active;
    }

    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String d) { this.description = d; }
    public String getSku() { return sku; } public void setSku(String sku) { this.sku = sku; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal price) { this.price = price; }
    public String getCategoryName() { return categoryName; } public void setCategoryName(String c) { this.categoryName = c; }
    public boolean isActive() { return active; } public void setActive(boolean active) { this.active = active; }
}
