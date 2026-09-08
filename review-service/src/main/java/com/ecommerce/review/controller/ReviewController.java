package com.ecommerce.review.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.review.entity.Review;
import com.ecommerce.review.service.ReviewService;

@RestController @RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService service;
    public ReviewController(ReviewService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Review> add(@Valid @RequestBody Review review) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addReview(review));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getReviewsByProduct(productId));
    }

    @GetMapping("/product/{productId}/average")
    public ResponseEntity<Double> getAverage(@PathVariable Long productId) {
        return ResponseEntity.ok(service.getAverageRating(productId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable Long id, @Valid @RequestBody Review updated) {
        return ResponseEntity.ok(service.updateReview(id, updated));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getReviewsByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
