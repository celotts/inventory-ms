package com.celotts.purchaseservice; // Corregido a purchase

import com.celotts.purchaseservice.infrastructure.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {
        "com.celotts.purchaseservice.infrastructure.adapter.output.postgres.repository"
})
@EntityScan(basePackages = "com.celotts.purchaseservice.infrastructure.adapter.output.postgres.entity")
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class PurchaseServiceApplication { // Nombre consistente con el microservicio
    public static void main(String[] args) {
        SpringApplication.run(PurchaseServiceApplication.class, args);
    }
}