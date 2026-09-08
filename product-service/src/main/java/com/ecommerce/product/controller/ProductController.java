package com.ecommerce.product.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.common.entity.Product;
import com.ecommerce.product.dto.CreateProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.dto.UpdateProductRequest;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.service.ProductService;
import com.ecommerce.common.entity.Category;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest req) {
        Product p = Product.builder()
                .name(req.getName()).description(req.getDescription())
                .price(req.getPrice()).sku(req.getSku())
                .imageUrl(req.getImageUrl()).unitsInStock(req.getUnitsInStock())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductMapper.toResponse(productService.createProduct(p, req.getCategoryId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ProductMapper.toResponse(productService.getProductById(id)));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll(@RequestParam(required = false) String search,
                                                        @RequestParam(required = false) Long categoryId,
                                                        @RequestParam(required = false) java.math.BigDecimal minPrice,
                                                        @RequestParam(required = false) java.math.BigDecimal maxPrice) {
        List<ProductResponse> list;
        if (search != null) list = productService.searchProducts(search).stream().map(ProductMapper::toResponse).toList();
        else if (categoryId != null) list = productService.getProductsByCategory(categoryId).stream().map(ProductMapper::toResponse).toList();
        else if (minPrice != null && maxPrice != null) list = productService.getProductsByPriceRange(minPrice, maxPrice).stream().map(ProductMapper::toResponse).toList();
        else list = productService.getAllProducts().stream().map(ProductMapper::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<ProductResponse>> topRated() {
        return ResponseEntity.ok(productService.getTopRatedProducts().stream().map(ProductMapper::toResponse).toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest req) {
        Product p = new Product();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setImageUrl(req.getImageUrl());
        p.setUnitsInStock(req.getUnitsInStock());
        p.setSku(req.getSku());
        if (req.getCategoryId() != null) {
            Category cat = new Category();
            cat.setId(req.getCategoryId());
            p.setCategory(cat);
        }
        return ResponseEntity.ok(ProductMapper.toResponse(productService.updateProduct(id, p)));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
