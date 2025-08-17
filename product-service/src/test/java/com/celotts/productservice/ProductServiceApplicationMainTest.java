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
        // Evitar que Hibernate/JPA/JDBC se inicialicen en este test de arranque del main
        env.put("spring.autoconfigure.exclude",
                String.join(",",
                    "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
                    "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
                    "org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration"
                ));
        // No necesitamos arrancar el servidor web para esta prueba
        env.put("spring.main.web-application-type", "none");
        env.forEach(System::setProperty);

        ProductServiceApplication.main(new String[]{});
    }
}