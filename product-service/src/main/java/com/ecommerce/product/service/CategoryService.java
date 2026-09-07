package com.ecommerce.product.service;

import java.time.LocalDateTime;
import java.util.List;
import com.ecommerce.common.entity.Category;

public interface CategoryService {
    Category getCategoryById(Long id);
    List<Category> getAllActiveCategories();
    List<Category> searchCategories(String keyword);
    boolean existsByName(String name);
    Category createCategory(Category category);
    Category updateCategory(Long id, Category updated);
    void deactivateCategory(Long id);
    long countByActive(boolean active);
    List<Category> getCategoriesByUpdatedAtAfter(LocalDateTime dateTime);
}
