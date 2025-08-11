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

    @Test
    void equals_returnsFalse_withNullAndDifferentClass() {
        var base = ProductUnitResponseDto.builder()
                .id(UUID.randomUUID())
                .code("C1")
                .name("N1")
                .description("D1")
                .symbol("kg")
                .enabled(true)
                .createdBy("c")
                .updatedBy("u")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotEquals(base, null);          // rama: other == null
        assertNotEquals(base, "no-dto");      // rama: other.getClass() distinto
    }

    @Test
    void equals_and_hashCode_change_whenAnyFieldDiffers() {
        var id  = UUID.randomUUID();
        var t1  = LocalDateTime.of(2024,1,1,1,1,1);
        var t2  = LocalDateTime.of(2024,1,1,1,1,2);

        var base = ProductUnitResponseDto.builder()
                .id(id).code("C").name("N").description("D").symbol("kg").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(t1).updatedAt(t1)
                .build();

        // Cambia uno por uno (sin toBuilder)
        assertNotEquals(base, ProductUnitResponseDto.builder()
                .id(UUID.randomUUID()).code("C").name("N").description("D").symbol("kg").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(t1).updatedAt(t1).build());

        assertNotEquals(base, ProductUnitResponseDto.builder()
                .id(id).code("C2").name("N").description("D").symbol("kg").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(t1).updatedAt(t1).build());

        assertNotEquals(base, ProductUnitResponseDto.builder()
                .id(id).code("C").name("N2").description("D").symbol("kg").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(t1).updatedAt(t1).build());

        assertNotEquals(base, ProductUnitResponseDto.builder()
                .id(id).code("C").name("N").description("D2").symbol("kg").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(t1).updatedAt(t1).build());

        assertNotEquals(base, ProductUnitResponseDto.builder()
                .id(id).code("C").name("N").description("D").symbol("g").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(t1).updatedAt(t1).build());

        assertNotEquals(base, ProductUnitResponseDto.builder()
                .id(id).code("C").name("N").description("D").symbol("kg").enabled(false)
                .createdBy("c").updatedBy("u").createdAt(t1).updatedAt(t1).build());

        assertNotEquals(base, ProductUnitResponseDto.builder()
                .id(id).code("C").name("N").description("D").symbol("kg").enabled(true)
                .createdBy("c2").updatedBy("u").createdAt(t1).updatedAt(t1).build());

        assertNotEquals(base, ProductUnitResponseDto.builder()
                .id(id).code("C").name("N").description("D").symbol("kg").enabled(true)
                .createdBy("c").updatedBy("u2").createdAt(t1).updatedAt(t1).build());

        assertNotEquals(base, ProductUnitResponseDto.builder()
                .id(id).code("C").name("N").description("D").symbol("kg").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(t2).updatedAt(t1).build());

        assertNotEquals(base, ProductUnitResponseDto.builder()
                .id(id).code("C").name("N").description("D").symbol("kg").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(t1).updatedAt(t2).build());

        // hashCode también cambia cuando cambia un campo relevante
        var changed = ProductUnitResponseDto.builder()
                .id(id).code("C2").name("N").description("D").symbol("kg").enabled(true)
                .createdBy("c").updatedBy("u").createdAt(t1).updatedAt(t1).build();
        assertNotEquals(base.hashCode(), changed.hashCode());
    }

    @Test
    @DisplayName("Setters establecen todos los campos correctamente")
    void setters_setAllFields() {
        var id = UUID.randomUUID();
        var created = LocalDateTime.of(2024, 1, 2, 3, 4, 5);
        var updated = LocalDateTime.of(2024, 6, 7, 8, 9, 10);

        // no-args + setters (cubre todos los setters que marcaban 0%)
        ProductUnitResponseDto dto = new ProductUnitResponseDto();
        dto.setId(id);
        dto.setCode("COD-01");
        dto.setName("Nombre");
        dto.setDescription("Descripción");
        dto.setSymbol("kg");
        dto.setEnabled(true);
        dto.setCreatedBy("creator");
        dto.setUpdatedBy("updater");
        dto.setCreatedAt(created);
        dto.setUpdatedAt(updated);

        // verificaciones con getters
        assertEquals(id, dto.getId());
        assertEquals("COD-01", dto.getCode());
        assertEquals("Nombre", dto.getName());
        assertEquals("Descripción", dto.getDescription());
        assertEquals("kg", dto.getSymbol());
        assertTrue(dto.isEnabled());
        assertEquals("creator", dto.getCreatedBy());
        assertEquals("updater", dto.getUpdatedBy());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
    }

    @Test
    @DisplayName("Builder.toString() se ejecuta (cubre clase interna del builder)")
    void builder_toString_isNotEmpty() {
        // no hace falta construir el DTO; solo invocar el toString del builder
        String s = ProductUnitResponseDto.builder()
                .code("C1")
                .name("N1")
                .description("D1")
                .symbol("kg")
                .toString(); // <-- cubre el método rojo

        assertNotNull(s);
        assertFalse(s.isBlank());
        // opcional: suele incluir el nombre de la clase interna
        assertTrue(s.contains("ProductUnitResponseDtoBuilder"));
    }
}