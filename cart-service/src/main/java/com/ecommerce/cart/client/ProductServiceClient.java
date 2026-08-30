package com.ecommerce.cart.client;

import com.ecommerce.common.entity.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductServiceClient {

    private final RestClient restClient;

    public ProductServiceClient(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("http://product-service").build();
    }

    public Product getProduct(Long productId) {
        return restClient.get()
                .uri("/api/products/{id}", productId)
                .retrieve()
                .body(Product.class);
    }
}
