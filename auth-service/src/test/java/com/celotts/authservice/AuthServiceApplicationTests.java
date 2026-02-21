package com.celotts.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        // Estas son las que tu JwtUtils.java está pidiendo a gritos:
        "app.jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970",
        "app.jwt.expirationMs=86400000",

        // Configuraciones de seguridad para que el test no busque servicios externos
        "spring.cloud.config.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "eureka.client.enabled=false",
        "jasypt.encryptor.password=password",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "server.port=0"
})
@ActiveProfiles("test")
class AuthServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}