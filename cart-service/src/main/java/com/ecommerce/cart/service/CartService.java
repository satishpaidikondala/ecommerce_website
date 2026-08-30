package com.ecommerce.cart.service;

import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.common.entity.Cart;

public interface CartService {

    Cart getCartByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Cart getOrCreateCart(Long userId);

    long countActiveCarts();

    List<Cart> findAbandonedCarts(int days);

    void emptyCart(Long cartId);
}
