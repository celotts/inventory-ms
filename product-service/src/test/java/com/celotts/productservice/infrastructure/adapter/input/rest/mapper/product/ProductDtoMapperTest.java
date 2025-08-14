package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productserviceOld.domain.model.ProductModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductRequestDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.product.ProductDtoMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class ProductDtoMapperTest {

    @Test
    void toModel_shouldMapDtoToModelCorrectly() {
        ProductRequestDto dto = ProductRequestDto.builder()
                .code("P001")
                .name("Producto 1")
                .description("Descripción del producto")
                .unitCode("UNI")
                .brandId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(new BigDecimal("99.99"))
                .enabled(true)
                .createdBy("tester")
                .build();

        ProductModel model = ProductDtoMapper.toModel(dto);

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
        assertNotNull(model.getCreatedAt());
    }

    @Test
    void toModel_shouldSetDefaults_whenNullFields() {
        ProductRequestDto dto = ProductRequestDto.builder().build();

        ProductModel model = ProductDtoMapper.toModel(dto);

        assertNotNull(model);
        assertEquals("system", model.getCreatedBy());
        assertTrue(model.getEnabled());
        assertNotNull(model.getCreatedAt());
    }

    @Test
    void toUpdateDto_shouldMapCorrectly() {
        ProductRequestDto dto = ProductRequestDto.builder()
                .code("P002")
                .name("Producto 2")
                .description("Desc 2")
                .unitCode("CODE2")
                .brandId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .minimumStock(5)
                .currentStock(20)
                .unitPrice(new BigDecimal("29.99"))
                .enabled(false)
                .updatedBy("editor")
                .build();

        ProductUpdateDto updateDto = ProductDtoMapper.toUpdateDto(dto);

        assertNotNull(updateDto);
        assertEquals(dto.getCode(), updateDto.getCode());
        assertEquals(dto.getName(), updateDto.getName());
        assertEquals(dto.getDescription(), updateDto.getDescription());
        assertEquals(dto.getUnitCode(), updateDto.getUnitCode());
        assertEquals(dto.getBrandId(), updateDto.getBrandId());
        assertEquals(dto.getCategoryId(), updateDto.getCategoryId());
        assertEquals(dto.getMinimumStock(), updateDto.getMinimumStock());
        assertEquals(dto.getCurrentStock(), updateDto.getCurrentStock());
        assertEquals(dto.getUnitPrice(), updateDto.getUnitPrice());
        assertEquals(dto.getEnabled(), updateDto.getEnabled());
        assertEquals("editor", updateDto.getUpdatedBy());
    }

    @Test
    void toUpdateDto_shouldSetDefaultUpdatedBy_whenNull() {
        ProductRequestDto dto = ProductRequestDto.builder().updatedBy(null).build();

        ProductUpdateDto updateDto = ProductDtoMapper.toUpdateDto(dto);

        assertNotNull(updateDto);
        assertEquals("system", updateDto.getUpdatedBy());
    }

    @Test
    void toResponseDto_shouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductModel model = ProductModel.builder()
                .id(id)
                .code("CODE")
                .name("Producto")
                .description("Desc")
                .unitCode("UNIT")
                .brandId(brandId)
                .categoryId(categoryId)
                .minimumStock(1)
                .currentStock(100)
                .unitPrice(new BigDecimal("9.99"))
                .enabled(true)
                .createdBy("user")
                .updatedBy("admin")
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductResponseDto responseDto = ProductDtoMapper.toResponseDto(model);

        assertNotNull(responseDto);
        assertEquals(id, responseDto.getId());
        assertEquals("CODE", responseDto.getCode());
        assertEquals("Producto", responseDto.getName());
        assertEquals("Desc", responseDto.getDescription());
        assertEquals("UNIT", responseDto.getUnitCode());
        assertEquals(brandId, responseDto.getBrandId());
        assertEquals(categoryId, responseDto.getCategoryId());
        assertEquals(1, responseDto.getMinimumStock());
        assertEquals(100, responseDto.getCurrentStock());
        assertEquals(new BigDecimal("9.99"), responseDto.getUnitPrice());
        assertTrue(responseDto.getEnabled());
        assertEquals("user", responseDto.getCreatedBy());
        assertEquals("admin", responseDto.getUpdatedBy());
        assertEquals(now, responseDto.getCreatedAt());
        assertEquals(now, responseDto.getUpdatedAt());
    }

    @Test
    void toResponseDtoList_shouldMapListCorrectly() {
        ProductModel model1 = ProductModel.builder().id(UUID.randomUUID()).code("A").build();
        ProductModel model2 = ProductModel.builder().id(UUID.randomUUID()).code("B").build();

        List<ProductResponseDto> result = ProductDtoMapper.toResponseDtoList(List.of(model1, model2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getCode());
        assertEquals("B", result.get(1).getCode());
    }

    @Test
    void toResponseDtoList_shouldReturnNull_whenInputIsNull() {
        List<ProductResponseDto> result = ProductDtoMapper.toResponseDtoList(null);
        assertNull(result);
    }

    @Test
    void utilityClassConstructor_shouldThrowException() throws Exception {
        var constructor = ProductDtoMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        try {
            constructor.newInstance();
            fail("Se esperaba una UnsupportedOperationException");
        } catch (InvocationTargetException e) {
            // ✅ Verificamos que el verdadero error sea UnsupportedOperationException
            assertTrue(e.getCause() instanceof UnsupportedOperationException);
        }
    }

    @Test
    void toModel_shouldReturnNull_whenInputIsNull() {
        assertNull(ProductDtoMapper.toModel(null));
    }

    @Test
    void toUpdateDto_shouldReturnNull_whenInputIsNull() {
        assertNull(ProductDtoMapper.toUpdateDto(null));
    }

    @Test
    void toModel_shouldUseProvidedEnabledValue_whenEnabledIsNotNull() {
        ProductRequestDto dto = ProductRequestDto.builder()
                .code("P001")
                .name("Producto")
                .enabled(false)
                .createdBy("tester")
                .build();

        ProductModel model = ProductDtoMapper.toModel(dto);

        assertNotNull(model);
        assertFalse(model.getEnabled());
    }

    @Test
    void toModel_shouldDefaultToEnabledTrue_whenEnabledIsNull() {
        ProductRequestDto dto = ProductRequestDto.builder()
                .code("P001")
                .name("Producto")
                .enabled(null)
                .createdBy("tester")
                .build();

        ProductModel model = ProductDtoMapper.toModel(dto);

        assertNotNull(model);
        assertTrue(model.getEnabled()); // <- Esta es la parte no cubierta aún
    }

    @Test
    void toResponseDto_shouldReturnNull_whenModelIsNull() {
        ProductResponseDto response = ProductDtoMapper.toResponseDto(null);
        assertNull(response);
    }
}