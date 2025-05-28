package com.celotts.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "eureka.client.enabled=false",
    "spring.cloud.discovery.enabled=false"
})
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
        // Test básico que verifica que el contexto se carga correctamente
    }
}
