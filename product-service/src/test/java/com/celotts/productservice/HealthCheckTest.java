package com.celotts.productservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.config.import=optional:classpath:/empty.yml",
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false"
})
class HealthCheckTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Health endpoint debe estar disponible")
    void shouldHaveHealthEndpoint() {
        String url = "http://localhost:" + port + "/actuator/health";

        System.out.println("🏥 Probando health endpoint: " + url);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        System.out.println("✅ Health Status: " + response.getStatusCode());
        System.out.println("📄 Health Body: " + response.getBody());

        assertThat(response.getStatusCode().value()).isLessThan(400);
    }
}