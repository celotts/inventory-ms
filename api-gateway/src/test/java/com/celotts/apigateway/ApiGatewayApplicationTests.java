package com.celotts.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "app.jwt.secret=test-secret-key-for-api-gateway-tests-1234567890"
})
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
        // Test básico que verifica que el contexto de Spring se carga correctamente
    }
}
