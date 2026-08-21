package com.ecommerce.order.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.common.entity.Order;
import com.ecommerce.common.entity.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Q1: Find all orders for a user
    List<Order> findByUserId(Long userId);

    // Q3: Find all pending orders
    List<Order> findByStatus(OrderStatus status);

    // Q4: Find orders in date range
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Q5: Find user's orders with specific status
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    // Bonus: Find order by order number
    Order findByOrderNumber(String orderNumber);

    // Additional useful methods
    List<Order> findByStatusAndCreatedAtBetween(OrderStatus status, LocalDateTime start, LocalDateTime end);
}