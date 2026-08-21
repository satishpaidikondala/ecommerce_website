package com.ecommerce.cart.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Q1: Get user's shopping cart
    Optional<Cart> findByUserId(Long userId);

    // Q2: Check if user has a cart
    boolean existsByUserId(Long userId);

    // Q6: Find abandoned carts (not updated in 7 days)
    List<Cart> findByUpdatedAtBefore(LocalDateTime dateTime);

    // Q7: Count carts (for analytics)
    long count();
}