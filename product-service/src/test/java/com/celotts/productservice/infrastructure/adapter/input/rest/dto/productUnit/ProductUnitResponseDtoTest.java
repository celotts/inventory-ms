package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitResponseDtoTest {

    @Test
    @DisplayName("@Builder y @Data: getters, equals/hashCode y toString")
    void builder_and_dataMethods() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        ProductUnitResponseDto a = ProductUnitResponseDto.builder()
                .id(id)
                .code("U01")
                .name("Unidad")
                .description("Desc")
                .symbol("kg")
                .enabled(true)
                .createdBy("creator")
                .updatedBy("upd")
                .createdAt(created)
                .updatedAt(updated)
                .build();

        ProductUnitResponseDto b = ProductUnitResponseDto.builder()
                .id(id)
                .code("U01")
                .name("Unidad")
                .description("Desc")
                .symbol("kg")
                .enabled(true)
                .createdBy("creator")
                .updatedBy("upd")
                .createdAt(created)
                .updatedAt(updated)
                .build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertTrue(a.toString().contains("ProductUnitResponseDto"));
        assertEquals("kg", a.getSymbol());
        assertTrue(a.isEnabled());
    }

    @Test
    @DisplayName("enabled por defecto es false cuando no se establece en el builder")
    void defaultEnabled_isFalse() {
        ProductUnitResponseDto dto = ProductUnitResponseDto.builder()
                .id(UUID.randomUUID())
                .code("U02")
                .name("Unidad 2")
                .description("Desc2")
                .symbol("g")
                .createdBy("c")
                .updatedBy("u")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        assertFalse(dto.isEnabled());
    }

    @Test
    @DisplayName("JSON: incluye 'symbol' y fechas se serializan como string por @JsonFormat")
    void jsonSerialization_includesSymbol_andFormatsDatesAsString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ProductUnitResponseDto dto = ProductUnitResponseDto.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .code("U03")
                .name("Unidad 3")
                .description("Desc3")
                .symbol("kg")
                .enabled(true)
                .createdBy("c")
                .updatedBy("u")
                .createdAt(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
                .updatedAt(LocalDateTime.of(2024, 6, 7, 8, 9, 10))
                .build();

        String json = mapper.writeValueAsString(dto);
        JsonNode root = mapper.readTree(json);

        assertEquals("kg", root.get("symbol").asText());
        assertTrue(root.get("createdAt").isTextual(), "createdAt debe ser string");
        assertTrue(root.get("updatedAt").isTextual(), "updatedAt debe ser string");
        assertTrue(root.get("createdAt").asText().startsWith("2024-"));
        assertTrue(root.get("updatedAt").asText().startsWith("2024-"));
    }

    @Test
    @DisplayName("Constructores no-args y all-args disponibles")
    void constructors_exist() {
        ProductUnitResponseDto empty = new ProductUnitResponseDto();
        assertNotNull(empty);

        ProductUnitResponseDto full = new ProductUnitResponseDto(
                UUID.randomUUID(), "C", "N", "D", "s", true,
                "cBy", "uBy", LocalDateTime.now(), LocalDateTime.now()
        );
        assertNotNull(full);
        assertEquals("C", full.getCode());
        assertEquals("s", full.getSymbol());
        assertTrue(full.isEnabled());
    }
}