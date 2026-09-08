package com.ecommerce.product.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.common.entity.Category;
import com.ecommerce.product.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Cacheable(value = "categories", key = "'cat:' + #id")
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
        return categoryRepository.searchCategories(keyword);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public Category createCategory(Category c) {
        if (categoryRepository.existsByName(c.getName())) {
            throw new IllegalArgumentException("Category exists: " + c.getName());
        }
        c.setActive(true);
        try { return categoryRepository.save(c); } catch (org.springframework.dao.DataIntegrityViolationException e) { throw new IllegalArgumentException("Category exists: " + c.getName()); }
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public Category updateCategory(Long id, Category updated) {
        Category existing = getCategoryById(id);
        if (updated.getName() != null) existing.setName(updated.getName());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        if (updated.getImageUrl() != null) existing.setImageUrl(updated.getImageUrl());
        return categoryRepository.save(existing);
    }

    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public void deactivateCategory(Long id) {
        Category cat = getCategoryById(id);
        cat.setActive(false);
        categoryRepository.save(cat);
    }

    @Override
    public long countByActive(boolean active) {
        return categoryRepository.countByActive(active);
    }

    @Override
    public List<Category> getCategoriesByUpdatedAtAfter(LocalDateTime dateTime) {
        return categoryRepository.findByUpdatedAtAfter(dateTime);
    }
}
