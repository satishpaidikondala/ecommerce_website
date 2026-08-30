package com.ecommerce.product.service;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.Product;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(Product product, Long categoryId) {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
        product.setCategory(cat);
        return productRepository.save(product);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    @Override
    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Cacheable(value = "products", key = "'cat:' + #categoryId")
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public Product updateProduct(Long id, Product updated) {
        Product existing = getProductById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setUnitsInStock(updated.getUnitsInStock());
        existing.setImageUrl(updated.getImageUrl());
        return productRepository.save(existing);
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void deactivateProduct(Long id) {
        Product p = getProductById(id);
        p.setActive(false);
        productRepository.save(p);
    }
}
