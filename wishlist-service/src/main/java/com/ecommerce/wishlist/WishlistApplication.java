package com.ecommerce.wishlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ecommerce.wishlist", "com.ecommerce.common"})
@EnableDiscoveryClient
@EnableCaching
@EntityScan(basePackages = {"com.ecommerce.common.entity", "com.ecommerce.wishlist.entity"})
@EnableJpaRepositories(basePackages = {"com.ecommerce.wishlist.repository"})
public class WishlistApplication {
    public static void main(String[] args) { SpringApplication.run(WishlistApplication.class, args); }
}
