package com.ecommerce.review.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.ecommerce.review.entity.Review;
import com.ecommerce.review.repository.ReviewRepository;

@Service
public class ReviewService {
    private final ReviewRepository repo;
    public ReviewService(ReviewRepository repo) { this.repo = repo; }

    public Review addReview(Review review) { return repo.save(review); }
    public List<Review> getReviewsByProduct(Long productId) { return repo.findByProductId(productId); }
    public Double getAverageRating(Long productId) {
        Double avg = repo.findAverageRatingByProductId(productId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }
    public void deleteReview(Long id) { repo.deleteById(id); }
}
