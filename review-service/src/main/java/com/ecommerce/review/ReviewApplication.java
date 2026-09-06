package com.ecommerce.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ecommerce.review", "com.ecommerce.common"})
@EnableDiscoveryClient
@EnableCaching
@EntityScan(basePackages = {"com.ecommerce.common.entity", "com.ecommerce.review.entity"})
@EnableJpaRepositories(basePackages = {"com.ecommerce.review.repository"})
public class ReviewApplication {
    public static void main(String[] args) { SpringApplication.run(ReviewApplication.class, args); }
}
