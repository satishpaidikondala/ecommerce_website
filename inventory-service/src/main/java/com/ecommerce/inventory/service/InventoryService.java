package com.ecommerce.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;

@Service
public class InventoryService {
    private final InventoryRepository repo;
    public InventoryService(InventoryRepository repo) { this.repo = repo; }

    @Transactional
    public boolean reserveStock(Long productId, int quantity) {
        Inventory inv = repo.findById(productId).orElse(
                com.ecommerce.inventory.entity.Inventory.builder().productId(productId).stock(100).reserved(0).build());
        int available = inv.getStock() - inv.getReserved();
        if (available < quantity) return false;
        inv.setReserved(inv.getReserved() + quantity);
        repo.save(inv);
        return true;
    }

    @Transactional
    public void confirmStock(Long productId, int quantity) {
        Inventory inv = repo.findById(productId).orElseThrow();
        inv.setStock(inv.getStock() - quantity);
        inv.setReserved(inv.getReserved() - quantity);
        repo.save(inv);
    }

    @Transactional
    public void releaseStock(Long productId, int quantity) {
        Inventory inv = repo.findById(productId).orElseThrow();
        inv.setReserved(inv.getReserved() - quantity);
        repo.save(inv);
    }
}
