package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.config.import=optional:classpath:/empty.yml",
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false"
})
class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("GET /api/v1/products endpoint debe funcionar")
    void shouldHaveProductsEndpoint() {
        // Test usando la ruta correcta del controlador
        String url = "http://localhost:" + port + "/api/v1/products";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        // Verificar que el endpoint responde correctamente
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("GET /api/v1/products/test endpoint debe funcionar")
    void shouldHaveTestEndpoint() {
        // Test del endpoint de prueba
        String url = "http://localhost:" + port + "/api/v1/products/test";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Product Service funcionando correctamente");
    }
}