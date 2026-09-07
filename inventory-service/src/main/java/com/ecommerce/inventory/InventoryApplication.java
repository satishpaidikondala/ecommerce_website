package com.ecommerce.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.ecommerce.inventory", "com.ecommerce.common"})
@EnableDiscoveryClient
@EntityScan(basePackages = {"com.ecommerce.common.entity", "com.ecommerce.inventory.entity"})
@EnableJpaRepositories(basePackages = {"com.ecommerce.inventory.repository"})
public class InventoryApplication {
    public static void main(String[] args) { SpringApplication.run(InventoryApplication.class, args); }
}
