package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandResponseDtoEqualsHashCodeTest {

    private ProductBrandResponseDto buildDto(UUID id, String name) {
        LocalDateTime now = LocalDateTime.of(2025,1,1,1,1,1); // determinista
        return ProductBrandResponseDto.builder()
                .id(id)
                .name(name)
                .description("desc")
                .enabled(true)
                .createdBy("user1")
                .updatedBy("user2")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void equals_true_forSameInstance() {
        ProductBrandResponseDto dto = buildDto(UUID.randomUUID(), "Brand1");
        assertTrue(dto.equals(dto));
    }

    @Test
    void equals_false_forNullOrDifferentType() {
        ProductBrandResponseDto dto = buildDto(UUID.randomUUID(), "Brand1");
        assertFalse(dto.equals(null));
        assertFalse(dto.equals("not-a-dto"));
    }

    @Test
    void equals_true_and_hashCode_equal_whenAllFieldsEqual() {
        UUID id = UUID.randomUUID();
        ProductBrandResponseDto a = buildDto(id, "Brand1");
        ProductBrandResponseDto b = buildDto(id, "Brand1");

        assertNotSame(a, b);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        // consistencia
        assertEquals(a.hashCode(), a.hashCode());
    }

    @Test
    void equals_false_and_hashCode_diff_whenFieldsDiffer() {
        ProductBrandResponseDto a = buildDto(UUID.randomUUID(), "Brand1");
        ProductBrandResponseDto b = buildDto(UUID.randomUUID(), "Brand2");

        assertNotEquals(a, b);
        // No es obligatorio que el hash difiera, pero suele hacerlo con Lombok si cambian campos usados
        // No lo afirmamos para evitar falsos rojos en caso de colisiones
    }
}