package com.ecommerce.cart.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.ecommerce.cart.dto.CartItemResponse;
import com.ecommerce.cart.dto.CartResponse;
import com.ecommerce.common.entity.Cart;
import com.ecommerce.common.entity.CartItem;

public class CartMapper {

    private CartMapper() {}

    public static CartItemResponse toItemResponse(CartItem item) {
        return new CartItemResponse(
                item.getId(),
                item.getProduct() != null ? item.getProduct().getId() : null,
                item.getProduct() != null ? item.getProduct().getName() : null,
                item.getPrice(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }

    public static CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems() == null ? List.of()
                : cart.getItems().stream().map(CartMapper::toItemResponse).collect(Collectors.toList());

        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                cart.getTotalAmount(),
                cart.getTotalItems(),
                items,
                cart.getUpdatedAt()
        );
    }
}
