package com.ecommerce.product.service;

import java.util.List;
import com.ecommerce.common.entity.Category;

public interface CategoryService {
    Category createCategory(Category category);
    Category getCategoryById(Long id);
    List<Category> getAllActiveCategories();
    List<Category> searchCategories(String keyword);
}
