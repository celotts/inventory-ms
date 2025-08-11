package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

        CategoryResponseDto dto = new CategoryResponseDto(
                id, name, description, active, createdBy, updatedBy, createdAt, updatedAt
        );

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
    void testBuilder_setsAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        CategoryResponseDto dto = CategoryResponseDto.builder()
                .id(id)
                .name("Bebidas")
                .description("Frías y calientes")
                .active(true)
                .createdBy("admin")
                .updatedBy("editor")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        assertEquals(id, dto.getId());
        assertEquals("Bebidas", dto.getName());
        assertEquals("Frías y calientes", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals("admin", dto.getCreatedBy());
        assertEquals("editor", dto.getUpdatedBy());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
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
        CategoryResponseDto dto2 = new CategoryResponseDto(UUID.randomUUID(), "B", "descB", false, "creatorB", "updaterB", now.minusDays(1), now.plusDays(1));

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

    @Test
    void testHashCode_worksInCollections() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryResponseDto a = new CategoryResponseDto(id, "A", "d", true, "c", "u", now, now);
        CategoryResponseDto b = new CategoryResponseDto(id, "A", "d", true, "c", "u", now, now);

        Set<CategoryResponseDto> set = new HashSet<>();
        set.add(a);
        // debe reconocer como duplicado por equals/hashCode
        assertFalse(set.add(b));
        assertEquals(1, set.size());
    }

    @Test
    void testToString_containsKeyFields() {
        UUID id = UUID.randomUUID();
        CategoryResponseDto dto = CategoryResponseDto.builder()
                .id(id)
                .name("Postres")
                .description("Dulces")
                .active(true)
                .createdBy("c")
                .updatedBy("u")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        String s = dto.toString();
        assertNotNull(s);
        assertTrue(s.contains("Postres"));
        assertTrue(s.contains("Dulces"));
        assertTrue(s.contains(id.toString()));
    }
}