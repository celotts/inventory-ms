package com.celotts.supplierservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories(basePackages =
        "com.celotts.supplierservice.infrastructure.adapter.output.postgres.repository")
@EntityScan(basePackages =
        "com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity")
public class SupplierServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupplierServiceApplication.class, args);
    }
}