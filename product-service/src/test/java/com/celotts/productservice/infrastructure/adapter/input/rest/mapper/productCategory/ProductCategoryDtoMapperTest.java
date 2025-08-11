package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productCategory;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryDtoMapperTest {

    private final ProductCategoryDtoMapper mapper = new ProductCategoryDtoMapper();

    @Test
    void toModel_shouldMapCorrectly() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime assignedAt = LocalDateTime.now();
        String createdBy = "admin";
        String updatedBy = "editor";

        ProductCategoryCreateDto dto = ProductCategoryCreateDto.builder()
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(assignedAt)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .build();

        ProductCategoryModel model = mapper.toModel(dto);

        assertNotNull(model);
        assertEquals(productId, model.getProductId());
        assertEquals(categoryId, model.getCategoryId());
        assertEquals(assignedAt, model.getAssignedAt());
        assertEquals(createdBy, model.getCreatedBy());
        assertEquals(updatedBy, model.getUpdatedBy());
        assertNotNull(model.getCreatedAt());
        assertNull(model.getUpdatedAt());
    }

    @Test
    void toModel_shouldReturnNull_whenInputIsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toDto_shouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime assignedAt = LocalDateTime.now();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();
        String createdBy = "admin";
        String updatedBy = "editor";

        ProductCategoryModel model = ProductCategoryModel.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(assignedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .build();

        ProductCategoryResponseDto dto = mapper.toDto(model);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(productId, dto.getProductId());
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals(assignedAt, dto.getAssignedAt());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
        assertEquals(createdBy, dto.getCreatedBy());
        assertEquals(updatedBy, dto.getUpdatedBy());
    }

    @Test
    void toDto_shouldReturnNull_whenInputIsNull() {
        assertNull(mapper.toDto(null));
    }
}