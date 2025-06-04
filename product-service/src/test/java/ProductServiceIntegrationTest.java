import com.celotts.productservice.applications.service.ProductService;
import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepositoryPort repository;

    @Test
    void updateProduct_shouldUpdateOnlyProvidedFields() {
        // Arrange: producto original
        ProductModel original = repository.save(ProductModel.builder()
                .code("CODE123")
                .name("Original Name")
                .description("Initial Description")
                .productTypeCode("TYPE01")
                .unitCode("UNIT01")
                .brandId(UUID.randomUUID())
                .minimumStock(5)
                .currentStock(100)
                .unitPrice(BigDecimal.valueOf(19.99))
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .createdBy("tester")
                .build());

        // Act: enviamos solo algunos campos para actualizar
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setCode("NEWCODE999");
        dto.setUpdatedBy("admin");

        ProductModel updated = productService.updateProduct(original.getId(), dto);

        // Assert: solo campos provistos cambian
        assertEquals("NEWCODE999", updated.getCode());
        assertEquals("Original Name", updated.getName()); // No cambia
        assertEquals("admin", updated.getUpdatedBy());
        assertEquals(original.getCreatedBy(), updated.getCreatedBy()); // No cambia
    }
}