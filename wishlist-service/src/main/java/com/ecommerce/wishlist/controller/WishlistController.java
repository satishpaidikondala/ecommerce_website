package com.ecommerce.wishlist.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.wishlist.entity.Wishlist;
import com.ecommerce.wishlist.service.WishlistService;

@RestController @RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService service;
    public WishlistController(WishlistService service) { this.service = service; }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishlist>> getWishlist(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getWishlist(userId));
    }

    @PostMapping
    public ResponseEntity<Wishlist> add(@RequestParam Long userId, @RequestParam Long productId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addToWishlist(userId, productId));
    }

    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestParam Long userId, @RequestParam Long productId) {
        service.removeFromWishlist(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
