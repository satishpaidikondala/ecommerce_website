package com.ecommerce.product.mapper;

import com.ecommerce.common.entity.Product;
import com.ecommerce.product.dto.ProductResponse;

public class ProductMapper {
    private ProductMapper() {}
    public static ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getSku(),
                p.getImageUrl(), p.isActive(), p.getUnitsInStock(),
                p.getCategory()!=null ? p.getCategory().getId() : null,
                p.getCategory()!=null ? p.getCategory().getName() : null,
                p.getCreatedAt());
    }
}
