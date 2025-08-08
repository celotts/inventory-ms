package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductResponseDtoTest {

    @Test
    void testEquals_withNullAndDifferentClass() {
        ProductResponseDto dto = ProductResponseDto.builder().build();
        assertNotEquals(dto, null);
        assertNotEquals(dto, "SomeString");
    }

    @Test
    void testEquals_sameReference() {
        ProductResponseDto dto = ProductResponseDto.builder().build();
        assertEquals(dto, dto);
    }

    @Test
    void testEquals_differentObjectsWithDifferentValues() {
        ProductResponseDto dto1 = ProductResponseDto.builder().code("ABC").build();
        ProductResponseDto dto2 = ProductResponseDto.builder().code("XYZ").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testEqualsAndHashCode_sameFields() {
        UUID id = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductResponseDto dto1 = ProductResponseDto.builder()
                .id(id)
                .code("CODE")
                .name("Name")
                .description("Desc")
                .categoryId(categoryId)
                .categoryName("Category")
                .unitCode("UNIT")
                .brandId(brandId)
                .brandName("Brand")
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .createdBy("admin")
                .updatedBy("user")
                .build();

        ProductResponseDto dto2 = ProductResponseDto.builder()
                .id(id)
                .code("CODE")
                .name("Name")
                .description("Desc")
                .categoryId(categoryId)
                .categoryName("Category")
                .unitCode("UNIT")
                .brandId(brandId)
                .brandName("Brand")
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .createdBy("admin")
                .updatedBy("user")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testHashCode_differentValues() {
        ProductResponseDto dto1 = ProductResponseDto.builder().code("AAA").build();
        ProductResponseDto dto2 = ProductResponseDto.builder().code("BBB").build();
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }



    @Test
    void testToString_containsClassName() {
        ProductResponseDto dto = ProductResponseDto.builder()
                .code("TOSTRING")
                .name("ProductX")
                .build();
        String result = dto.toString();
        assertTrue(result.contains("ProductResponseDto"));
        assertTrue(result.contains("code=TOSTRING"));
    }
}