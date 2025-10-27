package com.celotts.taxservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@ConfigurationPropertiesScan("com.celotts.taxservice.infrastructure.config") // si tienes AppProperties
@EnableJpaAuditing
@SpringBootApplication
public class TaxServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxServiceApplication.class, args);
    }
}