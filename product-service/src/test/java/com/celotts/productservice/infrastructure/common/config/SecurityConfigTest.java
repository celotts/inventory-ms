package com.celotts.productservice.infrastructure.common.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("integration") // Asegúrate de que este perfil no sea "test"
class SecurityConfigTest {

    @Autowired
    @Qualifier("filterChain")  // Esto buscará el bean definido en SecurityConfig
    private SecurityFilterChain securityFilterChain;

    @Test
    void shouldLoadSecurityFilterChainBean() {
        assertThat(securityFilterChain).isNotNull();
    }
}