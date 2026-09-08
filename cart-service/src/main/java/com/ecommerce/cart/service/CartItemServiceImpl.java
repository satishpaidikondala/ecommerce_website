package com.ecommerce.cart.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;

    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               ProductServiceClient productServiceClient) {
        this.cartItemRepository = cartItemRepository;
        this.productServiceClient = productServiceClient;
    }

    @Override
    public List<CartItem> getCartItemsByCartId(Long cartId) {
        return cartItemRepository.findByCart_Id(cartId);
    }

    @Override
    public Optional<CartItem> getCartItemByCartIdAndProductId(Long cartId, Long productId) {
        return cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId);
    }

    @Override
    @Transactional
    public CartItem addProductToCart(Long cartId, Long productId) {
        return addProductToCart(cartId, productId, 1);
    }

    @Transactional
    public CartItem addProductToCart(Long cartId, Long productId, int requestedQty) {
        if (requestedQty < 1) throw new IllegalArgumentException("Quantity must be at least 1");
        Optional<CartItem> existing = cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            int newQty = item.getQuantity() + requestedQty;
            Product p = productServiceClient.getProduct(productId);
            if (p != null && newQty > p.getUnitsInStock()) throw new IllegalArgumentException("Insufficient stock for " + p.getName());
            item.setQuantity(newQty);
            item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(newQty)));
            return cartItemRepository.save(item);
        }
        Product product = productServiceClient.getProduct(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + productId);
        }
        if (!product.isActive()) {
            throw new IllegalArgumentException("Product is not available: " + product.getName());
        }
        if (product.getUnitsInStock() < requestedQty) {
            throw new IllegalArgumentException("Product out of stock: " + product.getName());
        }
        Cart cartRef = entityManager.getReference(Cart.class, cartId);
        CartItem newItem = CartItem.builder()
                .cart(cartRef)
                .product(product)
                .quantity(requestedQty)
                .price(product.getPrice())
                .subtotal(product.getPrice().multiply(BigDecimal.valueOf(requestedQty)))
                .build();
        try {
            return cartItemRepository.save(newItem);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Product already in cart, retry");
        }
    }

    @Override
    @Transactional
    public void removeProductFromCart(Long cartId, Long productId) {
        CartItem item = cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found in cart"));
        cartItemRepository.delete(item);
    }

    @Override
    @Transactional
    public CartItem updateQuantity(Long cartId, Long productId, int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be at least 1");
        CartItem item = cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found in cart"));
        Product p = productServiceClient.getProduct(productId);
        if (p != null && quantity > p.getUnitsInStock()) throw new IllegalArgumentException("Insufficient stock");
        item.setQuantity(quantity);
        item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return cartItemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public CartTotalResponse calculateCartTotal(Long cartId) {
        List<CartItem> items = cartItemRepository.findByCart_Id(cartId);
        int totalItems = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : items) {
            BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalItems += item.getQuantity();
            totalAmount = totalAmount.add(subtotal);
        }
        return new CartTotalResponse(totalItems, totalAmount);
    }
}
