package com.ecommerce.wishlist.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.wishlist.entity.Wishlist;
import com.ecommerce.wishlist.repository.WishlistRepository;

@Service
public class WishlistService {
    private final WishlistRepository repo;
    public WishlistService(WishlistRepository repo) { this.repo = repo; }

    public List<Wishlist> getWishlist(Long userId) { return repo.findByUserId(userId); }

    @Transactional
    public Wishlist addToWishlist(Long userId, Long productId) {
        if (repo.existsByUserIdAndProductId(userId, productId))
            throw new IllegalArgumentException("Already in wishlist");
        return repo.save(Wishlist.builder().userId(userId).productId(productId).build());
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {
        repo.deleteByUserIdAndProductId(userId, productId);
    }
}
