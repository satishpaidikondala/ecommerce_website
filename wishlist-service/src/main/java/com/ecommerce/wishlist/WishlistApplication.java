package com.ecommerce.wishlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.ecommerce.wishlist", "com.ecommerce.common"})
@EnableDiscoveryClient
public class WishlistApplication {
    public static void main(String[] args) { SpringApplication.run(WishlistApplication.class, args); }
}
