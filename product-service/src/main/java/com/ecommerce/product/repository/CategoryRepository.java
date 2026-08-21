package com.ecommerce.product.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.common.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Active categories only
    List<Category> findByActiveTrue();

    // Search by name (case-insensitive)
    List<Category> findByNameContainingIgnoreCase(String name);

    // Search by name OR description
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Category> searchCategories(@Param("searchTerm") String searchTerm);

    // Check if name exists
    boolean existsByName(String name);

    // Count active categories
    long countByActive(boolean active);
}