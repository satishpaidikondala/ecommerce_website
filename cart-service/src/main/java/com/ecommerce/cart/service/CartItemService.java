package com.ecommerce.cart.service;

import java.util.List;
import java.util.Optional;

import com.ecommerce.cart.dto.CartTotalResponse;
import com.ecommerce.common.entity.CartItem;

public interface CartItemService {

    List<CartItem> getCartItemsByCartId(Long cartId);

    Optional<CartItem> getCartItemByCartIdAndProductId(Long cartId, Long productId);

    CartItem addProductToCart(Long cartId, Long productId);

    void removeProductFromCart(Long cartId, Long productId);

    CartItem updateQuantity(Long cartId, Long productId, int quantity);

    CartTotalResponse calculateCartTotal(Long cartId);
}
