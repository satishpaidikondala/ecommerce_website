package com.ecommerce.cart.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.cart.client.ProductServiceClient;
import com.ecommerce.cart.dto.CartTotalResponse;
import com.ecommerce.cart.repository.CartItemRepository;
import com.ecommerce.common.entity.Cart;
import com.ecommerce.common.entity.CartItem;
import com.ecommerce.common.entity.Product;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;

    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               ProductServiceClient productServiceClient) {
        this.cartItemRepository = cartItemRepository;
        this.productServiceClient = productServiceClient;
    }

    @Override
    public List<CartItem> getCartItemsByCartId(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public Optional<CartItem> getCartItemByCartIdAndProductId(Long cartId, Long productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }

    @Override
    @Transactional
    public CartItem addProductToCart(Long cartId, Long productId) {
        Optional<CartItem> existing = cartItemRepository.findByCartIdAndProductId(cartId, productId);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + 1);
            item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            return cartItemRepository.save(item);
        }
        Product product = productServiceClient.getProduct(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }
        if (!product.isActive()) {
            throw new IllegalArgumentException("Product is not available: " + product.getName());
        }
        if (product.getUnitsInStock() < 1) {
            throw new IllegalArgumentException("Product out of stock: " + product.getName());
        }
        CartItem newItem = CartItem.builder()
                .cart(Cart.builder().id(cartId).build())
                .product(product)
                .quantity(1)
                .price(product.getPrice())
                .subtotal(product.getPrice())
                .build();
        return cartItemRepository.save(newItem);
    }

    @Override
    @Transactional
    public void removeProductFromCart(Long cartId, Long productId) {
        CartItem item = cartItemRepository.findByCartIdAndProductId(cartId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found in cart"));
        cartItemRepository.delete(item);
    }

    @Override
    public CartTotalResponse calculateCartTotal(Long cartId) {
        List<CartItem> items = cartItemRepository.findByCartId(cartId);
        int totalItems = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : items) {
            totalItems += item.getQuantity();
            totalAmount = totalAmount.add(item.getSubtotal());
        }
        return new CartTotalResponse(totalItems, totalAmount);
    }
}
