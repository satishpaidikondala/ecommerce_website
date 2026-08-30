package com.ecommerce.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
