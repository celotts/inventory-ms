package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoTest {

    @Test
    void testCategoryCreateDto() {
        CategoryCreateDto dto = new CategoryCreateDto("Test Name", "Test Description");

        assertEquals("Test Name", dto.getName());
        assertEquals("Test Description", dto.getDescription());

        dto.setName("Updated Name");
        dto.setDescription("Updated Description");

        assertEquals("Updated Name", dto.getName());
        assertEquals("Updated Description", dto.getDescription());
    }

    @Test
    void testCategoryDeleteDto() {
        UUID id = UUID.randomUUID();
        CategoryDeleteDto dto = new CategoryDeleteDto(id);
        assertEquals(id, dto.getId());

        UUID newId = UUID.randomUUID();
        dto.setId(newId);
        assertEquals(newId, dto.getId());
    }

    @Test
    void testCategoryRequestDto() {
        CategoryRequestDto dto = CategoryRequestDto.builder()
                .name("Test Name")
                .description("Test Description")
                .active(true)
                .createdBy("admin")
                .updatedBy("editor")
                .build();

        assertEquals("Test Name", dto.getName());
        assertEquals("Test Description", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals("admin", dto.getCreatedBy());
        assertEquals("editor", dto.getUpdatedBy());
    }

    @Test
    void testCategoryResponseDto() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryResponseDto dto = CategoryResponseDto.builder()
                .id(id)
                .name("Response Name")
                .description("Response Description")
                .active(false)
                .createdBy("admin")
                .updatedBy("editor")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(id, dto.getId());
        assertEquals("Response Name", dto.getName());
        assertEquals("Response Description", dto.getDescription());
        assertFalse(dto.getActive());
        assertEquals("admin", dto.getCreatedBy());
        assertEquals("editor", dto.getUpdatedBy());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void testCategoryStatsDto() {
        CategoryStatusDto dto = CategoryStatusDto.builder()
                .totalCategories(10)
                .activeCategories(7)
                .inactiveCategories(2)
                .deletedCategories(1)
                .activePercentage(70.0)
                .inactivePercentage(20.0)
                .deletedPercentage(10.0)
                .build();

        assertEquals(10, dto.getTotalCategories());
        assertEquals(7, dto.getActiveCategories());
        assertEquals(2, dto.getInactiveCategories());
        assertEquals(1, dto.getDeletedCategories());
        assertEquals(70.0, dto.getActivePercentage());
        assertEquals(20.0, dto.getInactivePercentage());
        assertEquals(10.0, dto.getDeletedPercentage());
    }

    @Test
    void testCategoryUpdateDto() {
        CategoryUpdateDto dto = CategoryUpdateDto.builder()
                .name("Updated Category")
                .description("Updated Description")
                .active(true)
                .updatedBy("editor")
                .build();

        assertEquals("Updated Category", dto.getName());
        assertEquals("Updated Description", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals("editor", dto.getUpdatedBy());

        dto.setActive(false);
        assertFalse(dto.getActive());
    }
}