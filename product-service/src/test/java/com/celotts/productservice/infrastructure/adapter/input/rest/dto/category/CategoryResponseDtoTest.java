package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryResponseDtoTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        String name = "Test Category";
        String description = "Description";
        Boolean active = true;
        String createdBy = "user1";
        String updatedBy = "user2";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        CategoryResponseDto dto = new CategoryResponseDto(id, name, description, active, createdBy, updatedBy, createdAt, updatedAt);

        assertEquals(id, dto.getId());
        assertEquals(name, dto.getName());
        assertEquals(description, dto.getDescription());
        assertEquals(active, dto.getActive());
        assertEquals(createdBy, dto.getCreatedBy());
        assertEquals(updatedBy, dto.getUpdatedBy());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        CategoryResponseDto dto = new CategoryResponseDto();

        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(id);
        dto.setName("New Name");
        dto.setDescription("New Description");
        dto.setActive(false);
        dto.setCreatedBy("creator");
        dto.setUpdatedBy("updater");
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        assertEquals(id, dto.getId());
        assertEquals("New Name", dto.getName());
        assertEquals("New Description", dto.getDescription());
        assertFalse(dto.getActive());
        assertEquals("creator", dto.getCreatedBy());
        assertEquals("updater", dto.getUpdatedBy());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode_sameValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryResponseDto dto1 = new CategoryResponseDto(id, "name", "desc", true, "user1", "user2", now, now);
        CategoryResponseDto dto2 = new CategoryResponseDto(id, "name", "desc", true, "user1", "user2", now, now);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_differentValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryResponseDto dto1 = new CategoryResponseDto(id, "A", "descA", true, "creatorA", "updaterA", now, now);
        CategoryResponseDto dto2 = new CategoryResponseDto(id, "B", "descB", false, "creatorB", "updaterB", now.minusDays(1), now.plusDays(1));

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        CategoryResponseDto dto = new CategoryResponseDto();
        assertNotEquals(dto, null);
        assertNotEquals(dto, "not a dto");
    }

    @Test
    void testEqualsSameInstance() {
        CategoryResponseDto dto = new CategoryResponseDto();
        assertEquals(dto, dto);
    }
}