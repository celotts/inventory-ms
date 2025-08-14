package com.celotts.productservice;

import com.celotts.productservice.infrastructure.common.config.TestBeanConfig;
import com.celotts.productserviceOld.ProductServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.context.annotation.Import;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = {
				ProductServiceApplication.class,  // ← tu clase principal con @SpringBootApplication
				TestBeanConfig.class              // ← mocks personalizados
		}
)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
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
@Import(TestBeanConfig.class) // ✅ Importar configuración con mocks manuales
class ProductServiceApplicationTests {

	@Test
	void contextLoads() {
		// Validación del contexto
	}
}