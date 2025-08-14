package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandResponseDtoTest {

    @Test
    void testBuilderAndGetters() {
        UUID id = UUID.randomUUID();
        String name = "Brand Test";
        String description = "Some Description";
        Boolean enabled = true;
        String createdBy = "creator";
        String updatedBy = "updater";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        ProductBrandResponseDto dto = ProductBrandResponseDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .enabled(enabled)
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        assertEquals(id, dto.getId());
        assertEquals(name, dto.getName());
        assertEquals(description, dto.getDescription());
        assertEquals(enabled, dto.getEnabled());
        assertEquals(createdBy, dto.getCreatedBy());
        assertEquals(updatedBy, dto.getUpdatedBy());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandResponseDto dto1 = ProductBrandResponseDto.builder()
                .id(id)
                .name("Brand A")
                .description("Description")
                .enabled(true)
                .createdBy("creator")
                .updatedBy("updater")
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductBrandResponseDto dto2 = ProductBrandResponseDto.builder()
                .id(id)
                .name("Brand A")
                .description("Description")
                .enabled(true)
                .createdBy("creator")
                .updatedBy("updater")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProductBrandResponseDto dto = ProductBrandResponseDto.builder()
                .id(UUID.randomUUID())
                .name("Brand")
                .description("Desc")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.contains("Brand"));
    }

    @Test
    void testToBuilder() {
        LocalDateTime now = LocalDateTime.now();
        ProductBrandResponseDto original = ProductBrandResponseDto.builder()
                .id(UUID.randomUUID())
                .name("Original")
                .description("Original Desc")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductBrandResponseDto copy = original.toBuilder()
                .name("Modified")
                .build();

        assertEquals("Modified", copy.getName());
        assertEquals(original.getId(), copy.getId());
        assertNotEquals(original.getName(), copy.getName());
    }

    @Test
    void builder_toString_covers_inner_builder() {
        LocalDateTime now = LocalDateTime.of(2024, 1, 2, 3, 4, 5);

        // Llamamos a toString() DEL BUILDER para cubrir ProductBrandResponseDto.ProductBrandResponseDtoBuilder.toString()
        String s = ProductBrandResponseDto.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("Brand")
                .description("Desc")
                .enabled(true)
                .createdBy("creator")
                .updatedBy("updater")
                .createdAt(now)
                .updatedAt(now)
                .toString();

        assertNotNull(s);
        assertFalse(s.isBlank());
        // si tu Lombok incluye el nombre del builder, esto también suma:
        // assertTrue(s.contains("ProductBrandResponseDtoBuilder"));
    }
}