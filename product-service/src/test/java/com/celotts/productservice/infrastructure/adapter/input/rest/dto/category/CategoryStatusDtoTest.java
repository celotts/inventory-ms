package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryStatusDtoTest {

    @Test
    void noArgsConstructor_andSetters_shouldWork() {
        CategoryStatusDto dto = new CategoryStatusDto();

        dto.setTotalCategories(100L);
        dto.setActiveCategories(70L);
        dto.setInactiveCategories(25L);
        dto.setDeletedCategories(5L);

        dto.setActivePercentage(70.0);
        dto.setInactivePercentage(25.0);
        dto.setDeletedPercentage(5.0);

        assertEquals(100L, dto.getTotalCategories());
        assertEquals(70L, dto.getActiveCategories());
        assertEquals(25L, dto.getInactiveCategories());
        assertEquals(5L, dto.getDeletedCategories());

        assertEquals(70.0, dto.getActivePercentage(), 1e-9);
        assertEquals(25.0, dto.getInactivePercentage(), 1e-9);
        assertEquals(5.0, dto.getDeletedPercentage(), 1e-9);
    }

    @Test
    void allArgsConstructor_andGetters_shouldWork() {
        CategoryStatusDto dto = new CategoryStatusDto(
                120L, 80L, 30L, 10L,
                66.6667, 25.0, 8.3333
        );

        assertEquals(120L, dto.getTotalCategories());
        assertEquals(80L, dto.getActiveCategories());
        assertEquals(30L, dto.getInactiveCategories());
        assertEquals(10L, dto.getDeletedCategories());

        assertEquals(66.6667, dto.getActivePercentage(), 1e-9);
        assertEquals(25.0, dto.getInactivePercentage(), 1e-9);
        assertEquals(8.3333, dto.getDeletedPercentage(), 1e-9);
    }

    @Test
    void builder_shouldSetAllFields() {
        CategoryStatusDto dto = CategoryStatusDto.builder()
                .totalCategories(50L)
                .activeCategories(35L)
                .inactiveCategories(10L)
                .deletedCategories(5L)
                .activePercentage(70.0)
                .inactivePercentage(20.0)
                .deletedPercentage(10.0)
                .build();

        assertEquals(50L, dto.getTotalCategories());
        assertEquals(35L, dto.getActiveCategories());
        assertEquals(10L, dto.getInactiveCategories());
        assertEquals(5L, dto.getDeletedCategories());
        assertEquals(70.0, dto.getActivePercentage(), 1e-9);
        assertEquals(20.0, dto.getInactivePercentage(), 1e-9);
        assertEquals(10.0, dto.getDeletedPercentage(), 1e-9);
    }

    @Test
    void equalsAndHashCode_sameValues_shouldBeEqual() {
        CategoryStatusDto a = CategoryStatusDto.builder()
                .totalCategories(10L).activeCategories(6L).inactiveCategories(3L).deletedCategories(1L)
                .activePercentage(60.0).inactivePercentage(30.0).deletedPercentage(10.0)
                .build();

        CategoryStatusDto b = CategoryStatusDto.builder()
                .totalCategories(10L).activeCategories(6L).inactiveCategories(3L).deletedCategories(1L)
                .activePercentage(60.0).inactivePercentage(30.0).deletedPercentage(10.0)
                .build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equalsAndHashCode_differentValues_shouldNotBeEqual() {
        CategoryStatusDto a = CategoryStatusDto.builder()
                .totalCategories(10L).activeCategories(6L).inactiveCategories(3L).deletedCategories(1L)
                .activePercentage(60.0).inactivePercentage(30.0).deletedPercentage(10.0)
                .build();

        CategoryStatusDto c = CategoryStatusDto.builder()
                .totalCategories(12L).activeCategories(7L).inactiveCategories(4L).deletedCategories(1L)
                .activePercentage(58.3333).inactivePercentage(33.3333).deletedPercentage(8.3333)
                .build();

        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    void equals_shouldHandleNullAndDifferentClass() {
        CategoryStatusDto dto = new CategoryStatusDto();
        assertNotEquals(dto, null);
        assertNotEquals(dto, "string");
    }

    @Test
    void toString_shouldContainKeyValues() {
        CategoryStatusDto dto = CategoryStatusDto.builder()
                .totalCategories(5L)
                .activeCategories(3L)
                .inactiveCategories(1L)
                .deletedCategories(1L)
                .activePercentage(60.0)
                .inactivePercentage(20.0)
                .deletedPercentage(20.0)
                .build();

        String s = dto.toString();
        assertNotNull(s);
        assertTrue(s.contains("totalCategories=5"));
        assertTrue(s.contains("activeCategories=3"));
        assertTrue(s.contains("inactiveCategories=1"));
        assertTrue(s.contains("deletedCategories=1"));
        // no forzamos formato exacto de double, solo presencia del texto clave
        assertTrue(s.contains("activePercentage"));
        assertTrue(s.contains("inactivePercentage"));
        assertTrue(s.contains("deletedPercentage"));
    }
}