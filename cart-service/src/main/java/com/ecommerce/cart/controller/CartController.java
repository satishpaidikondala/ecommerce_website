package com.ecommerce.cart.controller;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.cart.dto.AddToCartRequest;
import com.ecommerce.cart.dto.CartItemResponse;
import com.ecommerce.cart.dto.CartResponse;
import com.ecommerce.cart.dto.CartTotalResponse;
import com.ecommerce.cart.mapper.CartMapper;
import com.ecommerce.cart.service.CartItemService;
import com.ecommerce.cart.service.CartItemServiceImpl;
import com.ecommerce.cart.service.CartService;
import com.ecommerce.common.entity.Cart;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    public CartController(CartService cartService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(CartMapper.toResponse(cart));
    }

    @PostMapping("/user/{userId}/or-create")
    public ResponseEntity<CartResponse> getOrCreateCart(@PathVariable Long userId, Authentication auth) {
        // TODO: enforce userId == JWT userId to prevent IDOR; for now trust authenticated user
        Cart cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(CartMapper.toResponse(cart));
    }
    // keep legacy GET for backwards compat
    @GetMapping("/user/{userId}/or-create-legacy")
    public ResponseEntity<CartResponse> getOrCreateCartLegacy(@PathVariable Long userId) {
        Cart cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(CartMapper.toResponse(cart));
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItemResponse>> getItems(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartItemService.getCartItemsByCartId(cartId).stream().map(CartMapper::toItemResponse).toList());
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemResponse> addItem(@PathVariable Long cartId,
                                            @Valid @RequestBody AddToCartRequest req) {
        int qty = req.getQuantity() != null ? req.getQuantity() : 1;
        com.ecommerce.common.entity.CartItem item;
        if (cartItemService instanceof CartItemServiceImpl impl) {
            item = impl.addProductToCart(cartId, req.getProductId(), qty);
        } else {
            item = cartItemService.addProductToCart(cartId, req.getProductId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(CartMapper.toItemResponse(item));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long cartId,
                                           @PathVariable Long productId) {
        cartItemService.removeProductFromCart(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemResponse> updateQuantity(@PathVariable Long cartId,
                                                   @PathVariable Long productId,
                                                   @RequestParam @Min(1) int quantity) {
        return ResponseEntity.ok(CartMapper.toItemResponse(cartItemService.updateQuantity(cartId, productId, quantity)));
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.emptyCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}/total")
    public ResponseEntity<CartTotalResponse> getTotal(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartItemService.calculateCartTotal(cartId));
    }

    @GetMapping("/abandoned")
    public ResponseEntity<List<CartResponse>> abandonedCarts(@RequestParam(defaultValue = "7") int days) {
        List<CartResponse> list = cartService.findAbandonedCarts(days)
                .stream().map(CartMapper::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/active-count")
    public ResponseEntity<Long> activeCartCount() {
        return ResponseEntity.ok(cartService.countActiveCarts());
    }
}
