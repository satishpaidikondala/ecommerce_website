package com.ecommerce.cart.service;

import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.common.entity.Cart;

public interface CartService {

    Cart getCartByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Cart getOrCreateCart(Long userId);

    // Q5: Calculate cart totals (delegates to CartItemService, kept here for convenience)
    // Implemented in CartItemService.calculateCartTotal()

    // Q7: How many users have items in cart but haven't checked out
    long countActiveCarts();

    // Q8: Abandoned carts not updated in N days
    List<Cart> findAbandonedCarts(int days);

    // Code Review Q: Proper empty cart (vs junior's buggy version)
    void emptyCart(Long cartId);
}
