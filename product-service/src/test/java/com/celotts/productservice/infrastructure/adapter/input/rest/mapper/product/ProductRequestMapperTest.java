package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductRequestMapperTest {

    private ProductRequestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductRequestMapper();
    }

    @Test
    void toModel_fromCreateDto_shouldMapCorrectly() {
        ProductCreateDto dto = ProductCreateDto.builder()
                .code("P001")
                .name("Producto")
                .description("Descripción")
                .unitCode("UNI")
                .brandId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(new BigDecimal("99.99"))
                .enabled(true)
                .createdBy("tester")
                .build();

        ProductModel model = mapper.toModel(dto);

        assertNotNull(model);
        assertEquals(dto.getCode(), model.getCode());
        assertEquals(dto.getName(), model.getName());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getUnitCode(), model.getUnitCode());
        assertEquals(dto.getBrandId(), model.getBrandId());
        assertEquals(dto.getCategoryId(), model.getCategoryId());
        assertEquals(dto.getMinimumStock(), model.getMinimumStock());
        assertEquals(dto.getCurrentStock(), model.getCurrentStock());
        assertEquals(dto.getUnitPrice(), model.getUnitPrice());
        assertEquals(dto.getEnabled(), model.getEnabled());
        assertEquals(dto.getCreatedBy(), model.getCreatedBy());
    }

    @Test
    void toModel_fromCreateDto_shouldReturnNull_whenInputIsNull() {
        assertNull(mapper.toModel((ProductCreateDto) null));
    }

    @Test
    void toModel_fromUpdateDto_shouldMapCorrectly() {
        ProductUpdateDto dto = ProductUpdateDto.builder()
                .code("P002")
                .name("Producto Modificado")
                .description("Nueva descripción")
                .unitCode("UNI2")
                .brandId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .minimumStock(5)
                .currentStock(30)
                .unitPrice(new BigDecimal("79.99"))
                .enabled(false)
                .createdBy("admin")
                .updatedBy("tester")
                .build();

        ProductModel model = mapper.toModel(dto);

        assertNotNull(model);
        assertEquals(dto.getCode(), model.getCode());
        assertEquals(dto.getName(), model.getName());
        assertEquals(dto.getUpdatedBy(), model.getUpdatedBy());
    }

    @Test
    void toModel_fromUpdateDto_shouldReturnNull_whenInputIsNull() {
        assertNull(mapper.toModel((ProductUpdateDto) null));
    }

    @Test
    void updateModelFromDto_shouldUpdateOnlyNonNullFields() {
        ProductModel existingModel = ProductModel.builder()
                .code("INIT")
                .name("Nombre Original")
                .minimumStock(5)
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .name("Nombre Nuevo")
                .minimumStock(10)
                .updatedBy("usuarioX")
                .build();

        mapper.updateModelFromDto(existingModel, dto);

        assertEquals("Nombre Nuevo", existingModel.getName());
        assertEquals(10, existingModel.getMinimumStock());
        assertEquals("usuarioX", existingModel.getUpdatedBy());
        assertNotNull(existingModel.getUpdatedAt());
        assertTrue(existingModel.getUpdatedAt().isAfter(LocalDateTime.now().minusSeconds(2)));
    }

    @Test
    void updateModelFromDto_shouldNotUpdate_whenDtoIsNull() {
        ProductModel existingModel = ProductModel.builder().code("P123").build();
        mapper.updateModelFromDto(existingModel, null);
        assertEquals("P123", existingModel.getCode());
    }

    @Test
    void updateModelFromDto_shouldNotFail_whenModelIsNull() {
        ProductUpdateDto dto = ProductUpdateDto.builder().name("Nuevo").build();
        assertDoesNotThrow(() -> mapper.updateModelFromDto(null, dto));
    }

    @Test
    void updateModelFromDto_shouldNotUpdateFields_whenDtoFieldsAreNull() {
        ProductModel existingModel = ProductModel.builder()
                .code("ORIGINAL")
                .name("Original Name")
                .updatedBy("OriginalUser")
                .build();

        ProductUpdateDto dto = ProductUpdateDto.builder().build();

        mapper.updateModelFromDto(existingModel, dto);

        assertEquals("ORIGINAL", existingModel.getCode());
        assertEquals("Original Name", existingModel.getName());
        assertEquals("OriginalUser", existingModel.getUpdatedBy());
        assertNotNull(existingModel.getUpdatedAt());
    }

    @Test
    void updateModelFromDto_shouldUpdateAllFields_whenAllFieldsNonNull() {
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductModel existingModel = ProductModel.builder()
                .code("OLD-CODE")
                .name("Old Name")
                .description("Old Description")
                .unitCode("OLD-UNIT")
                .brandId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .minimumStock(5)
                .currentStock(10)
                .unitPrice(new BigDecimal("50.00"))
                .enabled(false)
                .updatedBy("oldUser")
                .updatedAt(LocalDateTime.now().minusDays(1))
                .build();

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .code("NEW-CODE")
                .name("New Product")
                .description("New Description")
                .unitCode("NEW-UNIT")
                .brandId(brandId)
                .categoryId(categoryId)
                .minimumStock(20)
                .currentStock(100)
                .unitPrice(new BigDecimal("199.99"))
                .enabled(true)
                .updatedBy("newUser")
                .build();

        mapper.updateModelFromDto(existingModel, dto);

        assertEquals("NEW-CODE", existingModel.getCode());
        assertEquals("New Product", existingModel.getName());
        assertEquals("New Description", existingModel.getDescription());
        assertEquals("NEW-UNIT", existingModel.getUnitCode());
        assertEquals(brandId, existingModel.getBrandId());
        assertEquals(categoryId, existingModel.getCategoryId());
        assertEquals(20, existingModel.getMinimumStock());
        assertEquals(100, existingModel.getCurrentStock());
        assertEquals(new BigDecimal("199.99"), existingModel.getUnitPrice());
        assertEquals(true, existingModel.getEnabled());
        assertEquals("newUser", existingModel.getUpdatedBy());
        assertNotNull(existingModel.getUpdatedAt());
    }
}