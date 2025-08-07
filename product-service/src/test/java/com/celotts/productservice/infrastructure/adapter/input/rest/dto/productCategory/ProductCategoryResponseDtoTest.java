package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryResponseDtoTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        boolean enabled = true;
        String createdBy = "creator";
        String updatedBy = "updater";
        LocalDateTime now = LocalDateTime.now();

        ProductCategoryResponseDto dto = new ProductCategoryResponseDto(
                id, productId, categoryId, enabled, updatedBy, createdBy, now, now, now
        );

        assertEquals(id, dto.getId());
        assertEquals(productId, dto.getProductId());
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals(enabled, dto.getEnabled());
        assertEquals(createdBy, dto.getCreatedBy());
        assertEquals(updatedBy, dto.getUpdatedBy());
        assertEquals(now, dto.getAssignedAt());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void testSettersAndToStringAndEquality() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductCategoryResponseDto dto1 = new ProductCategoryResponseDto();
        dto1.setId(id);
        dto1.setProductId(productId);
        dto1.setCategoryId(categoryId);
        dto1.setEnabled(false);
        dto1.setCreatedBy("admin");
        dto1.setUpdatedBy("admin");
        dto1.setAssignedAt(now);
        dto1.setCreatedAt(now);
        dto1.setUpdatedAt(now);

        ProductCategoryResponseDto dto2 = ProductCategoryResponseDto.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .enabled(false)
                .createdBy("admin")
                .updatedBy("admin")
                .assignedAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        String toString = dto1.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("admin"));
        assertTrue(toString.contains("false"));
    }
}