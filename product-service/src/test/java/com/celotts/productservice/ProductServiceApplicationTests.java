package com.celotts.productservice;

import com.celotts.productservice.domain.port.product.type.usecase.ProductTypeUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.mock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"spring.config.import=optional:classpath:/empty.yml",
		"spring.cloud.config.enabled=false",
		"eureka.client.enabled=false",
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
		"spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(ProductServiceApplicationTests.MockedDependencies.class)
class ProductServiceApplicationTests {

	@TestConfiguration
	static class MockedDependencies {

		@Bean
		@Primary // ← esto es clave para evitar conflicto
		public ProductTypeUseCase productTypeUseCase() {
			return mock(ProductTypeUseCase.class);
		}
	}

	@Test
	@DisplayName("El contexto de Spring debe cargar correctamente con H2")
	void contextLoads() {
		// validación del contexto
	}
}