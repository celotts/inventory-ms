package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productserviceOld.domain.model.ProductModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.product.ProductModelUpdateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductModelUpdateMapperTest {

    private ProductModel existingModel;

    @BeforeEach
    void setUp() {
        existingModel = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("OLD_CODE")
                .name("Taco viejo")
                .description("Vieja descripción")
                .categoryId(UUID.randomUUID())
                .unitCode("UNI_OLD")
                .brandId(UUID.randomUUID())
                .minimumStock(10)
                .currentStock(30)
                .unitPrice(new BigDecimal("50.0"))
                .enabled(true)
                .updatedBy("old-user")
                .build();
    }

    @Test
    void apply_shouldUpdateModelFields_whenDtoHasValues() {
        UUID newCategoryId = UUID.randomUUID();
        UUID newBrandId = UUID.randomUUID();
        ProductUpdateDto dto = ProductUpdateDto.builder()
                .code("NEW_CODE")
                .description("Nueva descripción")
                .categoryId(newCategoryId)
                .unitCode("UNI_NEW")
                .brandId(newBrandId)
                .minimumStock(5)
                .currentStock(100)
                .unitPrice(new BigDecimal("99.99"))
                .enabled(false)
                .updatedBy("editor-user")
                .build();

        LocalDateTime beforeUpdate = LocalDateTime.now().minusSeconds(1);

        ProductModelUpdateMapper.apply(existingModel, dto);

        assertEquals("NEW_CODE", existingModel.getCode());
        assertEquals("Nueva descripción", existingModel.getDescription());
        assertEquals(newCategoryId, existingModel.getCategoryId());
        assertEquals("UNI_NEW", existingModel.getUnitCode());
        assertEquals(newBrandId, existingModel.getBrandId());
        assertEquals(5, existingModel.getMinimumStock());
        assertEquals(100, existingModel.getCurrentStock());
        assertEquals(new BigDecimal("99.99"), existingModel.getUnitPrice());
        assertFalse(existingModel.getEnabled());
        assertEquals("editor-user", existingModel.getUpdatedBy());
        assertTrue(existingModel.getUpdatedAt().isAfter(beforeUpdate));
    }

    @Test
    void apply_shouldSetDefaultUpdatedBy_whenUpdatedByIsNull() {
        ProductUpdateDto dto = ProductUpdateDto.builder()
                .code("X")
                .updatedBy(null) // deliberately null
                .build();

        ProductModelUpdateMapper.apply(existingModel, dto);

        assertEquals("X", existingModel.getCode());
        assertEquals("system", existingModel.getUpdatedBy());
    }

    @Test
    void apply_shouldNotUpdateFields_whenDtoFieldsAreNull() {
        ProductUpdateDto dto = ProductUpdateDto.builder().build(); // all fields null

        String oldCode = existingModel.getCode();
        String oldDescription = existingModel.getDescription();
        UUID oldCategoryId = existingModel.getCategoryId();
        String oldUnitCode = existingModel.getUnitCode();
        UUID oldBrandId = existingModel.getBrandId();
        Integer oldMinStock = existingModel.getMinimumStock();
        Integer oldCurrentStock = existingModel.getCurrentStock();
        BigDecimal oldUnitPrice = existingModel.getUnitPrice();
        Boolean oldEnabled = existingModel.getEnabled();
        String oldUpdatedBy = existingModel.getUpdatedBy();

        ProductModelUpdateMapper.apply(existingModel, dto);

        assertEquals(oldCode, existingModel.getCode());
        assertEquals(oldDescription, existingModel.getDescription());
        assertEquals(oldCategoryId, existingModel.getCategoryId());
        assertEquals(oldUnitCode, existingModel.getUnitCode());
        assertEquals(oldBrandId, existingModel.getBrandId());
        assertEquals(oldMinStock, existingModel.getMinimumStock());
        assertEquals(oldCurrentStock, existingModel.getCurrentStock());
        assertEquals(oldUnitPrice, existingModel.getUnitPrice());
        assertEquals(oldEnabled, existingModel.getEnabled());
        assertEquals("system", existingModel.getUpdatedBy());
        assertNotNull(existingModel.getUpdatedAt());
    }

    @Test
    void constructor_shouldThrowException() throws Exception {
        var constructor = ProductModelUpdateMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Exception exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertTrue(exception.getCause() instanceof UnsupportedOperationException);
        assertEquals("Utility class - instantiation not allowed", exception.getCause().getMessage());
    }

}