package com.ecommerce.cart.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.cart.dto.AddToCartRequest;
import com.ecommerce.cart.dto.CartResponse;
import com.ecommerce.cart.dto.CartTotalResponse;
import com.ecommerce.cart.mapper.CartMapper;
import com.ecommerce.cart.service.CartItemService;
import com.ecommerce.cart.service.CartService;
import com.ecommerce.common.entity.Cart;
import com.ecommerce.common.entity.CartItem;

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

    @GetMapping("/user/{userId}/or-create")
    public ResponseEntity<CartResponse> getOrCreateCart(@PathVariable Long userId) {
        Cart cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(CartMapper.toResponse(cart));
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItem>> getItems(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartItemService.getCartItemsByCartId(cartId));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItem> addItem(@PathVariable Long cartId,
                                            @Valid @RequestBody AddToCartRequest req) {
        CartItem item = cartItemService.addProductToCart(cartId, req.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long cartId,
                                           @PathVariable Long productId) {
        cartItemService.removeProductFromCart(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItem> updateQuantity(@PathVariable Long cartId,
                                                   @PathVariable Long productId,
                                                   @RequestParam int quantity) {
        return ResponseEntity.ok(cartItemService.updateQuantity(cartId, productId, quantity));
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
