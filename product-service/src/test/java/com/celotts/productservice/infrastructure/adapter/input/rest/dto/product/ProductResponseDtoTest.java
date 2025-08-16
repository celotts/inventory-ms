package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductResponseDtoTest {

    @Test
    void testEquals_withNullAndDifferentClass() {
        ProductResponseDto dto = ProductResponseDto.builder().build();
        assertNotEquals(dto, null);
        assertNotEquals(dto, "SomeString");
    }

    @Test
    void testEquals_sameReference() {
        ProductResponseDto dto = ProductResponseDto.builder().build();
        assertEquals(dto, dto);
    }

    @Test
    void testEquals_differentObjectsWithDifferentValues() {
        ProductResponseDto dto1 = ProductResponseDto.builder().code("ABC").build();
        ProductResponseDto dto2 = ProductResponseDto.builder().code("XYZ").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testEqualsAndHashCode_sameFields() {
        UUID id = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductResponseDto dto1 = ProductResponseDto.builder()
                .id(id)
                .code("CODE")
                .name("Name")
                .description("Desc")
                .categoryId(categoryId)
                .categoryName("Category")
                .unitCode("UNIT")
                .brandId(brandId)
                .brandName("Brand")
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .createdBy("admin")
                .updatedBy("user")
                .build();

        ProductResponseDto dto2 = ProductResponseDto.builder()
                .id(id)
                .code("CODE")
                .name("Name")
                .description("Desc")
                .categoryId(categoryId)
                .categoryName("Category")
                .unitCode("UNIT")
                .brandId(brandId)
                .brandName("Brand")
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .createdBy("admin")
                .updatedBy("user")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testHashCode_differentValues() {
        ProductResponseDto dto1 = ProductResponseDto.builder().code("AAA").build();
        ProductResponseDto dto2 = ProductResponseDto.builder().code("BBB").build();
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString_containsClassName() {
        ProductResponseDto dto = ProductResponseDto.builder()
                .code("TOSTRING")
                .name("ProductX")
                .build();
        String result = dto.toString();
        assertTrue(result.contains("ProductResponseDto"));
        assertTrue(result.contains("code=TOSTRING"));
    }

    @Test
    @DisplayName("Builder: usa todos los setters y build()")
    void builder_setsEveryField() {
        UUID id = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.of(2024, 1, 2, 3, 4, 5);
        LocalDateTime updated = LocalDateTime.of(2024, 6, 7, 8, 9, 10);

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(id)
                .code("P-001")
                .name("Prod")
                .description("Desc")
                .categoryId(categoryId)
                .categoryName("Bebidas")
                .unitCode("KG")
                .brandId(brandId)
                .brandName("Acme")
                .minimumStock(10)
                .currentStock(7)
                .unitPrice(new BigDecimal("12.34"))
                .enabled(Boolean.TRUE)
                .createdAt(created)
                .updatedAt(updated)
                .createdBy("admin")
                .updatedBy("user")
                .lowStock(Boolean.TRUE)
                .build();

        assertEquals(id, dto.getId());
        assertEquals("P-001", dto.getCode());
        assertEquals("Prod", dto.getName());
        assertEquals("Desc", dto.getDescription());
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals("Bebidas", dto.getCategoryName());
        assertEquals("KG", dto.getUnitCode());
        assertEquals(brandId, dto.getBrandId());
        assertEquals("Acme", dto.getBrandName());
        assertEquals(10, dto.getMinimumStock());
        assertEquals(7, dto.getCurrentStock());
        assertEquals(new BigDecimal("12.34"), dto.getUnitPrice());
        assertTrue(dto.getEnabled());
        assertEquals(created, dto.getCreatedAt());
        assertEquals(updated, dto.getUpdatedAt());
        assertEquals("admin", dto.getCreatedBy());
        assertEquals("user", dto.getUpdatedBy());
        assertEquals(Boolean.TRUE, dto.getLowStock());
    }

    @Test
    @DisplayName("equals/hashCode: cambia un campo a la vez")
    void equals_hashCode_changeWhenFieldDiffers() {
        UUID id = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductResponseDto base = ProductResponseDto.builder()
                .id(id).code("C").name("N").description("D")
                .categoryId(categoryId).categoryName("Cat")
                .unitCode("U")
                .brandId(brandId).brandName("B")
                .minimumStock(10).currentStock(20)
                .unitPrice(new BigDecimal("19.90"))
                .enabled(true)
                .createdAt(LocalDateTime.of(2024,1,1,1,1,1))
                .updatedAt(LocalDateTime.of(2024,1,1,1,1,2))
                .createdBy("cb").updatedBy("ub")
                .lowStock(false)
                .build();

        assertNotEquals(base, baseToBuilder(base).id(UUID.randomUUID()).build());
        assertNotEquals(base, baseToBuilder(base).code("C2").build());
        assertNotEquals(base, baseToBuilder(base).name("N2").build());
        assertNotEquals(base, baseToBuilder(base).description("D2").build());
        assertNotEquals(base, baseToBuilder(base).categoryId(UUID.randomUUID()).build());
        assertNotEquals(base, baseToBuilder(base).categoryName("Cat2").build());
        assertNotEquals(base, baseToBuilder(base).unitCode("U2").build());
        assertNotEquals(base, baseToBuilder(base).brandId(UUID.randomUUID()).build());
        assertNotEquals(base, baseToBuilder(base).brandName("B2").build());
        assertNotEquals(base, baseToBuilder(base).minimumStock(11).build());
        assertNotEquals(base, baseToBuilder(base).currentStock(21).build());
        assertNotEquals(base, baseToBuilder(base).unitPrice(new BigDecimal("20.00")).build());
        assertNotEquals(base, baseToBuilder(base).enabled(false).build());
        assertNotEquals(base, baseToBuilder(base).createdAt(LocalDateTime.of(2024,1,1,1,1,3)).build());
        assertNotEquals(base, baseToBuilder(base).updatedAt(LocalDateTime.of(2024,1,1,1,1,4)).build());
        assertNotEquals(base, baseToBuilder(base).createdBy("cb2").build());
        assertNotEquals(base, baseToBuilder(base).updatedBy("ub2").build());
        assertNotEquals(base, baseToBuilder(base).lowStock(true).build());

        int baseHash = base.hashCode();
        int changedHash = baseToBuilder(base).code("C-hash").build().hashCode();
        assertNotEquals(baseHash, changedHash);
    }

    private ProductResponseDto.ProductResponseDtoBuilder baseToBuilder(ProductResponseDto b) {
        return ProductResponseDto.builder()
                .id(b.getId())
                .code(b.getCode())
                .name(b.getName())
                .description(b.getDescription())
                .categoryId(b.getCategoryId())
                .categoryName(b.getCategoryName())
                .unitCode(b.getUnitCode())
                .brandId(b.getBrandId())
                .brandName(b.getBrandName())
                .minimumStock(b.getMinimumStock())
                .currentStock(b.getCurrentStock())
                .unitPrice(b.getUnitPrice())
                .enabled(b.getEnabled())
                .createdAt(b.getCreatedAt())
                .updatedAt(b.getUpdatedAt())
                .createdBy(b.getCreatedBy())
                .updatedBy(b.getUpdatedBy())
                .lowStock(b.getLowStock());
    }

    @Test
    @DisplayName("Builder.toString() (cubre la clase interna del builder)")
    void builder_toString_covers_inner_builder() {
        String s = ProductResponseDto.builder()
                .id(UUID.randomUUID())
                .code("X")
                .name("Y")
                .description("Z")
                .categoryId(UUID.randomUUID())
                .categoryName("Cat")
                .unitCode("U")
                .brandId(UUID.randomUUID())
                .brandName("Brand")
                .minimumStock(1)
                .currentStock(2)
                .unitPrice(new BigDecimal("1.23"))
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("cb")
                .updatedBy("ub")
                .lowStock(false)
                .toString();

        assertNotNull(s);
        assertFalse(s.isBlank());
    }

    @Test
    @DisplayName("JSON: createdAt/updatedAt siguen el patrón yyyy-MM-dd HH:mm:ss")
    void json_format_for_dates() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .code("P")
                .name("Prod")
                .createdAt(LocalDateTime.of(2024, 1, 2, 3, 4, 5))
                .updatedAt(LocalDateTime.of(2024, 6, 7, 8, 9, 10))
                .build();

        String json = mapper.writeValueAsString(dto);
        JsonNode root = mapper.readTree(json);

        assertEquals("2024-01-02 03:04:05", root.get("createdAt").asText());
        assertEquals("2024-06-07 08:09:10", root.get("updatedAt").asText());
        assertEquals("P", root.get("code").asText());
        assertEquals("Prod", root.get("name").asText());
    }
}