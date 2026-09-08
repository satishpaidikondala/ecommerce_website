package com.ecommerce.order.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.common.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Get all items in an order
    List<OrderItem> findByOrder_Id(Long orderId);

    // Total quantity sold for a product
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.product.id = :productId")
    Integer totalQuantitySoldByProductId(@Param("productId") Long productId);

    // Total revenue for an order
    @Query("SELECT COALESCE(SUM(oi.subtotal), 0) FROM OrderItem oi WHERE oi.order.id = :orderId")
    BigDecimal calculateOrderTotal(@Param("orderId") Long orderId);

    // Top 5 most ordered products
    @Query("SELECT oi.product.id, SUM(oi.quantity) FROM OrderItem oi GROUP BY oi.product.id ORDER BY SUM(oi.quantity) DESC")
    List<Object[]> findTop5MostOrderedProducts(Pageable pageable);

    // Find specific item in order
    Optional<OrderItem> findByOrder_IdAndProduct_Id(Long orderId, Long productId);

    // Get items by product (for analytics)
    List<OrderItem> findByProduct_Id(Long productId);
}