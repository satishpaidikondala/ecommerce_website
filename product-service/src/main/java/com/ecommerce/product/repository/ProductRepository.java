package com.ecommerce.product.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by category
    List<Product> findByCategoryId(Long categoryId);

    // Find product by SKU (unique code)
    Product findBySku(String sku);

    // Find only active products
    List<Product> findByActive(boolean active);

    // Search products by name (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);
}