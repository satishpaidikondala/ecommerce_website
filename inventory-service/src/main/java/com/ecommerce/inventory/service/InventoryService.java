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
        Inventory inv = repo.findById(productId).orElse(null);
        if (inv == null) return false;
        int available = (inv.getStock()!=null?inv.getStock():0) - (inv.getReserved()!=null?inv.getReserved():0);
        if (available < quantity) return false;
        inv.setReserved(inv.getReserved() + quantity);
        repo.save(inv);
        return true;
    }

    @Transactional
    public void confirmStock(Long productId, int quantity) {
        Inventory inv = repo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Inventory not found "+productId));
        if (quantity > inv.getStock() || quantity > inv.getReserved()) throw new IllegalArgumentException("Insufficient stock/reserved");
        inv.setStock(inv.getStock() - quantity);
        inv.setReserved(Math.max(0, inv.getReserved() - quantity));
        repo.save(inv);
    }

    @Transactional
    public void releaseStock(Long productId, int quantity) {
        Inventory inv = repo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Inventory not found "+productId));
        int newReserved = inv.getReserved() - quantity;
        if (newReserved < 0) newReserved = 0;
        inv.setReserved(newReserved);
        repo.save(inv);
    }
}
