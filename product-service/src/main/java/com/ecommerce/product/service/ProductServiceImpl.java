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
import com.ecommerce.product.search.ProductDocument;
import com.ecommerce.product.search.ProductSearchRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSearchRepository searchRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              ProductSearchRepository searchRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.searchRepository = searchRepository;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProductServiceImpl.class);

    private void indexProduct(Product p) {
        try {
            ProductDocument doc = new ProductDocument(
                    String.valueOf(p.getId()), p.getName(), p.getDescription(),
                    p.getSku(), p.getPrice(),
                    p.getCategory() != null ? p.getCategory().getName() : null,
                    p.isActive());
            searchRepository.save(doc);
            log.debug("ES indexed product {}", p.getId());
        } catch (Exception e) {
            log.warn("ES indexing failed for product {} — will retry via fallback DB search: {}", p.getId(), e.getMessage());
            // Real prod: publish to outbox table + async retry job or RabbitMQ product.index.retry queue
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(Product product, Long categoryId) {
        Category cat = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
        product.setCategory(cat);
        Product saved = productRepository.save(product);
        indexProduct(saved);
        return saved;
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
        try {
            var docs = searchRepository.findByNameContainingOrDescriptionContaining(keyword, keyword);
            if (!docs.isEmpty()) {
                log.debug("ES hit for '{}' — {} results", keyword, docs.size());
                return docs.stream().map(d -> {
                    Product p = new Product();
                    p.setId(Long.valueOf(d.getId()));
                    p.setName(d.getName());
                    p.setDescription(d.getDescription());
                    p.setSku(d.getSku());
                    p.setPrice(d.getPrice());
                    p.setActive(d.isActive());
                    return p;
                }).toList();
            }
        } catch (Exception e) {
            log.warn("ES search failed for '{}' — fallback to DB LIKE: {}", keyword, e.getMessage());
        }
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
        Product saved = productRepository.save(existing);
        indexProduct(saved);
        return saved;
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void deactivateProduct(Long id) {
        Product p = getProductById(id);
        p.setActive(false);
        Product saved = productRepository.save(p);
        indexProduct(saved);
    }
}
