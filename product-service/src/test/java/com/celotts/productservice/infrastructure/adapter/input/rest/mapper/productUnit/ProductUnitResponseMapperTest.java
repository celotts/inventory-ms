package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductUnitResponseMapperTest {

    private final ProductUnitResponseMapper mapper = new ProductUnitResponseMapper();

    @Test
    @DisplayName("toDto(null) debe retornar null")
    void toDto_null_returnsNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    @DisplayName("toDto mapea id, code, name, symbol y enabled; y deja null los no mapeados")
    void toDto_mapsSelectedFields() {
        UUID id = UUID.randomUUID();

        ProductUnitModel model = mock(ProductUnitModel.class);
        when(model.getId()).thenReturn(id);
        when(model.getCode()).thenReturn("U001");
        when(model.getName()).thenReturn("Unidad");
        when(model.getSymbol()).thenReturn("kg");
        when(model.getEnabled()).thenReturn(true);

        ProductUnitResponseDto dto = mapper.toDto(model);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("U001", dto.getCode());
        assertEquals("Unidad", dto.getName());
        assertEquals("kg", dto.getSymbol());
        assertTrue(dto.isEnabled());

        // Campos que este mapper no setea:
        assertNull(dto.getDescription());
        assertNull(dto.getCreatedBy());
        assertNull(dto.getUpdatedBy());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    @DisplayName("toDtoList(null) y toDtoList([]) devuelven lista vacía")
    void toDtoList_nullOrEmpty_returnsEmptyList() {
        assertTrue(mapper.toDtoList(null).isEmpty());
        assertTrue(mapper.toDtoList(List.of()).isEmpty());
    }

    @Test
    @DisplayName("toDtoList mapea todos los elementos")
    void toDtoList_mapsAll() {
        ProductUnitModel m1 = mock(ProductUnitModel.class);
        when(m1.getId()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        when(m1.getCode()).thenReturn("U1");
        when(m1.getName()).thenReturn("Uno");
        when(m1.getSymbol()).thenReturn("u");
        when(m1.getEnabled()).thenReturn(true);

        ProductUnitModel m2 = mock(ProductUnitModel.class);
        when(m2.getId()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        when(m2.getCode()).thenReturn("U2");
        when(m2.getName()).thenReturn("Dos");
        when(m2.getSymbol()).thenReturn("d");
        when(m2.getEnabled()).thenReturn(false);

        List<ProductUnitResponseDto> list = mapper.toDtoList(List.of(m1, m2));

        assertEquals(2, list.size());

        ProductUnitResponseDto d1 = list.get(0);
        assertEquals("U1", d1.getCode());
        assertEquals("Uno", d1.getName());
        assertEquals("u", d1.getSymbol());
        assertTrue(d1.isEnabled());

        ProductUnitResponseDto d2 = list.get(1);
        assertEquals("U2", d2.getCode());
        assertEquals("Dos", d2.getName());
        assertEquals("d", d2.getSymbol());
        assertFalse(d2.isEnabled());
    }
}