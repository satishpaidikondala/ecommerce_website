package com.ecommerce.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryRepository repo;
    public InventoryController(InventoryRepository repo) { this.repo = repo; }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Inventory> getStock(@PathVariable Long productId) {
        return ResponseEntity.of(repo.findById(productId));
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<Inventory> updateStock(@PathVariable Long productId, @RequestParam int stock) {
        if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");
        Inventory inv = repo.findById(productId).orElseGet(() -> Inventory.builder().productId(productId).stock(0).reserved(0).build());
        // TODO: validate productId exists via product-service before creating inventory
        inv.setStock(stock);
        return ResponseEntity.ok(repo.save(inv));
    }
}
