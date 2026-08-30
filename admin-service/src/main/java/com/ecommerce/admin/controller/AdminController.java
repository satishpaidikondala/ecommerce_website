package com.ecommerce.admin.controller;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        // Real: aggregate via RestClient from each service's /actuator/info or count endpoints
        return ResponseEntity.ok(Map.of(
            "users", "via user-service /api/users?active=true",
            "products", "via product-service /api/products",
            "orders", "via order-service /api/orders?status=PENDING",
            "cartsActive", "via cart-service /api/carts/active-count",
            "reviews", "via review-service",
            "status", "GramSetu operational"
        ));
    }
}
