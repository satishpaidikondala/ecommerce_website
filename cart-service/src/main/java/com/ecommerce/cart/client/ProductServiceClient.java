package com.ecommerce.cart.client;

import com.ecommerce.common.entity.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class ProductServiceClient {

    private final RestClient restClient;

    public ProductServiceClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://product-service").build();
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProduct")
    @Retry(name = "productService")
    @RateLimiter(name = "productService")
    @Bulkhead(name = "productService", type = Bulkhead.Type.SEMAPHORE)
    public Product getProduct(Long productId) {
        return restClient.get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .body(Product.class);
    }

    @SuppressWarnings("unused")
    private Product fallbackProduct(Long productId, Throwable t) {
        // Preserve original fallback behavior but avoid mapping to 404; log and throw service-unavailable
        org.slf4j.LoggerFactory.getLogger(ProductServiceClient.class)
                .warn("Product service fallback for id {}: {}", productId, t.toString());
        if (t instanceof io.github.resilience4j.ratelimiter.RequestNotPermitted) {
            throw new IllegalStateException("Rate limit exceeded for product service, try again later");
        }
        throw new IllegalStateException(
                "Product service unavailable for id " + productId + " — please try again");
    }
}
