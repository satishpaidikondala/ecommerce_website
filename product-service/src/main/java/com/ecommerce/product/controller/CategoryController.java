package com.ecommerce.product.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.common.entity.Category;
import com.ecommerce.product.dto.CategoryRequest;
import com.ecommerce.product.dto.CategoryResponse;
import com.ecommerce.product.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest req) {
        Category c = Category.builder().name(req.getName()).description(req.getDescription()).imageUrl(req.getImageUrl()).build();
        Category saved = categoryService.createCategory(c);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CategoryResponse(saved.getId(), saved.getName(), saved.getDescription(), saved.getImageUrl(), saved.isActive()));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(@RequestParam(required = false) String search) {
        List<Category> list = search != null ? categoryService.searchCategories(search) : categoryService.getAllActiveCategories();
        return ResponseEntity.ok(list.stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getDescription(), c.getImageUrl(), c.isActive())).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        Category c = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new CategoryResponse(c.getId(), c.getName(), c.getDescription(), c.getImageUrl(), c.isActive()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest req) {
        Category c = Category.builder().name(req.getName()).description(req.getDescription()).imageUrl(req.getImageUrl()).build();
        Category updated = categoryService.updateCategory(id, c);
        return ResponseEntity.ok(new CategoryResponse(updated.getId(), updated.getName(), updated.getDescription(), updated.getImageUrl(), updated.isActive()));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        categoryService.deactivateCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countByActive(@RequestParam boolean active) {
        return ResponseEntity.ok(categoryService.countByActive(active));
    }

    @GetMapping("/updated-after")
    public ResponseEntity<List<CategoryResponse>> getUpdatedAfter(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime dateTime) {
        java.time.LocalDateTime dt = dateTime;
        List<Category> list = categoryService.getCategoriesByUpdatedAtAfter(dt);
        return ResponseEntity.ok(list.stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getDescription(), c.getImageUrl(), c.isActive())).toList());
    }
}
