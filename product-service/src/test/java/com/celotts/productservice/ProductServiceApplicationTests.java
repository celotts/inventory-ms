package com.celotts.productservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"spring.config.import=optional:classpath:/empty.yml",
		"spring.cloud.config.enabled=false",
		"eureka.client.enabled=false",
		// Configuración H2 explícita
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
class ProductServiceApplicationTests {

	@Test
	@DisplayName("El contexto de Spring debe cargar correctamente con H2")
	void contextLoads() {
		// Test que verifica que el contexto carga sin errores
	}
}