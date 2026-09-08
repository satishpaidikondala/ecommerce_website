package com.ecommerce.admin.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AdminService {

    private final RestClient restClient;

    public AdminService(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("users", fetchCount("http://user-service/api/users?active=true", "users"));
        stats.put("products", fetchCount("http://product-service/api/products", "products"));
        stats.put("orders", fetchCount("http://order-service/api/orders?status=PENDING", "orders"));
        stats.put("cartsActive", fetchCount("http://cart-service/api/carts/active-count", "cartsActive"));
        stats.put("reviews", fetchCount("http://review-service/api/reviews/product/1", "reviews"));
        stats.put("status", "GramSetu operational");
        return stats;
    }

    private Object fetchCount(String url, String key) {
        try {
            // discovery via loadbalancer – http://service will resolve via Eureka
            return restClient.get().uri(url).retrieve().body(Object.class);
        } catch (Exception e) {
            return "unavailable (" + e.getMessage() + ")";
        }
    }
}
