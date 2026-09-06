package com.ecommerce.product.service;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.ecommerce.common.entity.Category;
import com.ecommerce.product.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category already exists: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    @CacheEvict(value = "categories", allEntries = true)
    public Category updateCategory(Long id, Category updated) {
        Category existing = getCategoryById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setImageUrl(updated.getImageUrl());
        return categoryRepository.save(existing);
    }

    @Override
    @Cacheable(value = "categories", key = "#id")
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
    }

    @Override
    @Cacheable(value = "categories", key = "'allActive'")
    public List<Category> getAllActiveCategories() {
        return categoryRepository.findByActiveTrue();
    }

    @Override
    public List<Category> searchCategories(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }
}
