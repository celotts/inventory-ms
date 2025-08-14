package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductCreate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProductCreateDtoTest {

    @Test
    void testEquals_withNullAndDifferentClass() {
        ProductCreate dto = ProductCreate.builder().build();
        assertNotEquals(dto, null); // null comparison
        assertNotEquals(dto, "Not a DTO"); // other class
    }

    @Test
    void testEquals_sameReference() {
        ProductCreate dto = ProductCreate.builder().build();
        assertEquals(dto, dto);
    }

    @Test
    void testEquals_differentObjectsWithDifferentValues() {
        ProductCreate dto1 = ProductCreate.builder().code("X").build();
        ProductCreate dto2 = ProductCreate.builder().code("Y").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testEquals_and_HashCode_sameFields() {
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductCreate dto1 = ProductCreate.builder()
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

        ProductCreate dto2 = ProductCreate.builder()
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
        ProductCreate dto1 = ProductCreate.builder().code("A").build();
        ProductCreate dto2 = ProductCreate.builder().code("B").build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testCanEqual() {
        ProductCreate dto = ProductCreate.builder().build();
        assertTrue(dto.canEqual(ProductCreate.builder().build()));
    }

    @Test
    void testDefaultEnabledIsTrue() {
        ProductCreate dto = ProductCreate.builder().build();
        assertTrue(dto.getEnabled());
    }
}