package com.ecommerce.cart.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.cart.repository.CartItemRepository;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.common.entity.Cart;

@Service
public class CartServiceImp implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImp(CartRepository cartRepository,
                          CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .userId(userId)
                                .build()));
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cart not found for user id: " + userId));
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return cartRepository.existsByUserId(userId);
    }

    @Override
    public long countActiveCarts() {
        return cartRepository.countNonEmptyCarts();
    }

    @Override
    public List<Cart> findAbandonedCarts(int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        return cartRepository.findByUpdatedAtBefore(cutoff);
    }

    @Override
    @Transactional
    public void emptyCart(Long cartId) {
        // Proper version: validates existence, uses delete query, updates cart totals
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cart not found with id: " + cartId));

        cartItemRepository.deleteByCartId(cartId);

        // Reset cart totals after clearing
        cart.setTotalItems(0);
        cart.setTotalAmount(java.math.BigDecimal.ZERO);
        cartRepository.save(cart);
    }
}
