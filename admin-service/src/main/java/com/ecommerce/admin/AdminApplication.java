package com.ecommerce.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {"com.ecommerce.admin", "com.ecommerce.common"})
@EnableDiscoveryClient
public class AdminApplication {
    public static void main(String[] args) { SpringApplication.run(AdminApplication.class, args); }
}
