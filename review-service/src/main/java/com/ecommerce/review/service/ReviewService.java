package com.ecommerce.review.service;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.ecommerce.review.entity.Review;
import com.ecommerce.review.repository.ReviewRepository;

@Service
public class ReviewService {
    private final ReviewRepository repo;
    public ReviewService(ReviewRepository repo) { this.repo = repo; }

    @CacheEvict(value = "reviews", allEntries = true)
    public Review addReview(Review review) { return repo.save(review); }
    @Cacheable(value = "reviews", key = "#productId")
    public List<Review> getReviewsByProduct(Long productId) { return repo.findByProductId(productId); }
    @Cacheable(value = "ratings", key = "#productId")
    public Double getAverageRating(Long productId) {
        Double avg = repo.findAverageRatingByProductId(productId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }
    @CacheEvict(value = {"reviews", "ratings"}, allEntries = true)
    public Review updateReview(Long id, Review updated) {
        Review existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Review not found: " + id));
        existing.setRating(updated.getRating());
        existing.setComment(updated.getComment());
        return repo.save(existing);
    }
    public java.util.List<Review> getReviewsByUser(Long userId) { return repo.findByUserId(userId); }
    @CacheEvict(value = {"reviews", "ratings"}, allEntries = true)
    public void deleteReview(Long id) { repo.deleteById(id); }
}
