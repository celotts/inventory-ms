package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductRequestMapperTest {

    private ProductRequestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductRequestMapper();
    }

    @Test
    void toModel_shouldReturnModel_whenCreateDtoIsValid() {
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductCreateDto dto = ProductCreateDto.builder()
                .code("PROD-001")
                .name("Taco al Pastor")
                .description("Delicioso taco tradicional mexicano")
                .categoryId(UUID.randomUUID())
                .unitCode("UNIT001")
                .brandId(UUID.randomUUID())
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(new BigDecimal("15.99"))
                .enabled(true)
                .createdBy("test-user")
                .updatedBy("test-user")
                .build();

        ProductModel model = mapper.toModel(dto);

        assertNotNull(model);
        assertEquals(dto.getCode(), model.getCode());
        assertEquals(dto.getName(), model.getName());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getCategoryId(), model.getCategoryId());
        assertEquals(dto.getUnitCode(), model.getUnitCode());
        assertEquals(dto.getBrandId(), model.getBrandId());
        assertEquals(dto.getMinimumStock(), model.getMinimumStock());
        assertEquals(dto.getCurrentStock(), model.getCurrentStock());
        assertEquals(dto.getUnitPrice(), model.getUnitPrice());
        assertEquals(dto.getEnabled(), model.getEnabled());
        assertEquals(dto.getCreatedBy(), model.getCreatedBy());
    }

    @Test
    void toModel_shouldReturnModel_whenUpdateDtoIsValid() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .id(id)
                .code("P002")
                .name("Producto B")
                .description("Descripción B")
                .categoryId(categoryId)
                .unitCode("LTS")
                .brandId(brandId)
                .minimumStock(20)
                .currentStock(100)
                .unitPrice(BigDecimal.valueOf(456.78))
                .createdBy("admin")
                .enabled(false)
                .updatedBy("editor")
                .build();

        ProductModel model = mapper.toModel(dto);

        assertNotNull(model);
        assertEquals(dto.getId(), model.getId());
        assertEquals(dto.getCode(), model.getCode());
        assertEquals(dto.getName(), model.getName());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getCategoryId(), model.getCategoryId());
        assertEquals(dto.getUnitCode(), model.getUnitCode());
        assertEquals(dto.getBrandId(), model.getBrandId());
        assertEquals(dto.getMinimumStock(), model.getMinimumStock());
        assertEquals(dto.getCurrentStock(), model.getCurrentStock());
        assertEquals(dto.getUnitPrice(), model.getUnitPrice());
        assertEquals(dto.getEnabled(), model.getEnabled());
        assertEquals(dto.getCreatedBy(), model.getCreatedBy());
        assertEquals(dto.getUpdatedBy(), model.getUpdatedBy());
    }

    @Test
    void toModel_shouldReturnNull_whenCreateDtoIsNull() {
        assertNull(mapper.toModel((ProductCreateDto) null));
    }

    @Test
    void toModel_shouldReturnNull_whenUpdateDtoIsNull() {
        assertNull(mapper.toModel((ProductUpdateDto) null));
    }

    @Test
    void updateModelFromDto_shouldUpdateFields_whenDtoHasValues() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductModel existing = ProductModel.builder()
                .id(id)
                .code("OLD_CODE")
                .name("Old Name")
                .description("Old Desc")
                .categoryId(UUID.randomUUID())
                .unitCode("OLD_UNIT")
                .brandId(UUID.randomUUID())
                .minimumStock(5)
                .currentStock(10)
                .unitPrice(BigDecimal.valueOf(10.00))
                .enabled(true)
                .updatedBy("old-user")
                .build();

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .id(id)
                .code("P002")
                .name("Producto B")
                .description("Descripción B")
                .categoryId(categoryId)
                .unitCode("LTS")
                .brandId(brandId)
                .minimumStock(20)
                .currentStock(100)
                .unitPrice(BigDecimal.valueOf(456.78))
                .createdBy("admin")
                .enabled(false)
                .updatedBy("editor")
                .build();

        mapper.updateModelFromDto(existing, dto);

        assertEquals("P002", existing.getCode());
        assertEquals("Producto B", existing.getName());
        assertEquals("Descripción B", existing.getDescription());
        assertEquals("LTS", existing.getUnitCode());
        assertEquals(brandId, existing.getBrandId());
        assertEquals(categoryId, existing.getCategoryId());
        assertEquals(20, existing.getMinimumStock());
        assertEquals(100, existing.getCurrentStock());
        assertEquals(BigDecimal.valueOf(456.78), existing.getUnitPrice());
        assertEquals(false, existing.getEnabled());
        assertEquals("editor", existing.getUpdatedBy());
        assertNotNull(existing.getUpdatedAt());
    }

    @Test
    void updateModelFromDto_shouldNotUpdateAnything_whenDtoIsNull() {
        ProductModel existing = ProductModel.builder().code("initial").build();
        mapper.updateModelFromDto(existing, null);
        assertEquals("initial", existing.getCode()); // unchanged
    }

    @Test
    void updateModelFromDto_shouldDoNothing_whenModelIsNull() {
        ProductUpdateDto dto = ProductUpdateDto.builder().code("new").build();
        mapper.updateModelFromDto(null, dto); // no exception should be thrown
    }
}