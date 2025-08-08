package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductCreateDtoTest {

    @Test
    void testEquals_withNullAndDifferentClass() {
        ProductCreateDto dto = ProductCreateDto.builder().build();
        assertNotEquals(dto, null); // null comparison
        assertNotEquals(dto, "Not a DTO"); // other class
    }

    @Test
    void testEquals_sameReference() {
        ProductCreateDto dto = ProductCreateDto.builder().build();
        assertEquals(dto, dto);
    }

    @Test
    void testEquals_differentObjectsWithDifferentValues() {
        ProductCreateDto dto1 = ProductCreateDto.builder().code("X").build();
        ProductCreateDto dto2 = ProductCreateDto.builder().code("Y").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testEquals_and_HashCode_sameFields() {
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductCreateDto dto1 = ProductCreateDto.builder()
                .code("PRD001")
                .name("Product A")
                .description("Description")
                .categoryId(categoryId)
                .unitCode("KG")
                .brandId(brandId)
                .minimumStock(10)
                .currentStock(20)
                .unitPrice(new BigDecimal("15.99"))
                .enabled(true)
                .createdBy("admin")
                .updatedBy("user")
                .build();

        ProductCreateDto dto2 = ProductCreateDto.builder()
                .code("PRD001")
                .name("Product A")
                .description("Description")
                .categoryId(categoryId)
                .unitCode("KG")
                .brandId(brandId)
                .minimumStock(10)
                .currentStock(20)
                .unitPrice(new BigDecimal("15.99"))
                .enabled(true)
                .createdBy("admin")
                .updatedBy("user")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testHashCode_differentValues() {
        ProductCreateDto dto1 = ProductCreateDto.builder().code("A").build();
        ProductCreateDto dto2 = ProductCreateDto.builder().code("B").build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testCanEqual() {
        ProductCreateDto dto = ProductCreateDto.builder().build();
        assertTrue(dto.canEqual(ProductCreateDto.builder().build()));
    }

    @Test
    void testDefaultEnabledIsTrue() {
        ProductCreateDto dto = ProductCreateDto.builder().build();
        assertTrue(dto.getEnabled());
    }
}