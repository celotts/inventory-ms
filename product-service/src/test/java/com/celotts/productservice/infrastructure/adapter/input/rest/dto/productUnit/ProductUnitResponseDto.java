package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitResponseDtoTest {

    @Test
    void builder_shouldSetAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductUnitResponseDto dto = ProductUnitResponseDto.builder()
                .id(id)
                .code("PU001")
                .name("Unidad")
                .description("Descripción")
                .symbol("kg")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(id, dto.getId());
        assertEquals("PU001", dto.getCode());
        assertEquals("Unidad", dto.getName());
        assertEquals("Descripción", dto.getDescription());
        assertEquals("kg", dto.getSymbol());
        assertTrue(dto.isEnabled());
        assertEquals("admin", dto.getCreatedBy());
        assertEquals("admin", dto.getUpdatedBy());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void gettersAndSetters_shouldWorkCorrectly() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductUnitResponseDto dto = new ProductUnitResponseDto();
        dto.setId(id);
        dto.setCode("PU002");
        dto.setName("Caja");
        dto.setDescription("Caja grande");
        dto.setSymbol("cj");
        dto.setEnabled(false);
        dto.setCreatedBy("tester");
        dto.setUpdatedBy("tester");
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);

        assertEquals(id, dto.getId());
        assertEquals("PU002", dto.getCode());
        assertEquals("Caja", dto.getName());
        assertEquals("Caja grande", dto.getDescription());
        assertEquals("cj", dto.getSymbol());
        assertFalse(dto.isEnabled());
        assertEquals("tester", dto.getCreatedBy());
        assertEquals("tester", dto.getUpdatedBy());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void equalsAndHashCode_shouldWorkCorrectly() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductUnitResponseDto dto1 = ProductUnitResponseDto.builder()
                .id(id)
                .code("PU003")
                .name("Unidad")
                .symbol("u")
                .createdAt(now)
                .build();

        ProductUnitResponseDto dto2 = ProductUnitResponseDto.builder()
                .id(id)
                .code("PU003")
                .name("Unidad")
                .symbol("u")
                .createdAt(now)
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_shouldIncludeFields() {
        ProductUnitResponseDto dto = ProductUnitResponseDto.builder()
                .code("PU004")
                .name("Nombre")
                .symbol("s")
                .build();

        String str = dto.toString();

        assertTrue(str.contains("PU004"));
        assertTrue(str.contains("Nombre"));
        assertTrue(str.contains("s"));
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyObject() {
        ProductUnitResponseDto dto = new ProductUnitResponseDto();
        assertNotNull(dto);
    }
}