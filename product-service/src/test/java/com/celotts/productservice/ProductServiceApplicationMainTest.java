package com.celotts.productservice;

import com.celotts.productservice.ProductServiceApplication;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class ProductServiceApplicationMainTest {

    @Test
    void main_shouldRunWithoutExceptions() {
        // Simula un entorno limpio para que el main no falle por configuración externa
        Map<String, String> env = new HashMap<>();
        env.put("spring.profiles.active", "test");
        env.put("spring.cloud.config.enabled", "false");
        env.put("eureka.client.enabled", "false");
        env.forEach(System::setProperty);

        ProductServiceApplication.main(new String[]{});
    }
}