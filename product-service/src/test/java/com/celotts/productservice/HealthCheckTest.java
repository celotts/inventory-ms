package com.celotts.productservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.celotts.productservice.domain.port.product.type.usecase.ProductTypeUseCase;
import com.celotts.productservice.ProductServiceApplication;
import org.mockito.Mockito;

@SpringBootTest(
        classes = ProductServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import(HealthCheckTest.MockConfig.class)
@ActiveProfiles("test")
class HealthCheckTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ProductTypeUseCase productTypeUseCase() {
            return Mockito.mock(ProductTypeUseCase.class);
        }
    }

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    @DisplayName("Health endpoint debe estar disponible")
    void shouldHaveHealthEndpoint() {
        String url = "http://localhost:" + port + "/actuator/health";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode().value()).isLessThan(400);
    }
}