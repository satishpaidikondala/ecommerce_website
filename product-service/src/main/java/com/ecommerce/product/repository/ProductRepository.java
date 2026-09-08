package com.ecommerce.product.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by category
    List<Product> findByCategory_Id(Long categoryId);

    // Find product by SKU (unique code)
    java.util.Optional<Product> findBySku(String sku);

    // Find only active products
    List<Product> findByActive(boolean active);

    // Search products by name (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    List<Product> findByPriceBetween(java.math.BigDecimal min, java.math.BigDecimal max);

    org.springframework.data.domain.Page<Product> findByActive(boolean active, org.springframework.data.domain.Pageable pageable);
}