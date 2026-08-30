package com.ecommerce.product.service;

import java.util.List;
import com.ecommerce.common.entity.Product;

public interface ProductService {
    Product createProduct(Product product, Long categoryId);
    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(Long categoryId);
    List<Product> searchProducts(String keyword);
    Product updateProduct(Long id, Product updated);
    void deactivateProduct(Long id);
}
