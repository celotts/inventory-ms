package com.celotts.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"spring.config.import=optional:classpath:/empty.yml",
		"spring.cloud.config.enabled=false"
})
class ProductServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
