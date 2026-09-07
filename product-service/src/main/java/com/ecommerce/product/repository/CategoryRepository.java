package com.ecommerce.product.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.ecommerce.common.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByActiveTrue();

    List<Category> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Category> searchCategories(@Param("searchTerm") String searchTerm);

    boolean existsByName(String name);

    long countByActive(boolean active);

    List<Category> findByUpdatedAtAfter(LocalDateTime dateTime);
}
