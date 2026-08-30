package com.ecommerce.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.inventory.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {}
