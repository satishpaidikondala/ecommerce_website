package com.ecommerce.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.common.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Q3: Get all items in a cart
    List<CartItem> findByCartId(Long cartId);

    // Q4: Find specific product in cart
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    // Q6: Delete all items in a cart (empty cart)
    void deleteByCartId(Long cartId);

    // Q7: Count items by user and product
    @Query("SELECT ci.quantity FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId")
    Integer findQuantityByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);
}
