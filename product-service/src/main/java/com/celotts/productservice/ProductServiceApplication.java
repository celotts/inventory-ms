package com.celotts.productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.celotts.productservice.infrastructure.adapter.output.postgres.repository")
@EntityScan(basePackages = "com.celotts.productservice.infrastructure.adapter.output.postgres.entity")
@EnableConfigurationProperties
@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
}