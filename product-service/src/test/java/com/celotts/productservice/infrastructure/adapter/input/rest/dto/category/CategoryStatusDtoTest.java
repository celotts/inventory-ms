package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryStatusDtoTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        CategoryStatusDto dto = new CategoryStatusDto(
                50, 40, 5, 5,
                80.0, 10.0, 10.0
        );

        assertEquals(50, dto.getTotalCategories());
        assertEquals(40, dto.getActiveCategories());
        assertEquals(5, dto.getInactiveCategories());
        assertEquals(5, dto.getDeletedCategories());
        assertEquals(80.0, dto.getActivePercentage());
        assertEquals(10.0, dto.getInactivePercentage());
        assertEquals(10.0, dto.getDeletedPercentage());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        CategoryStatusDto dto = new CategoryStatusDto();

        dto.setTotalCategories(100);
        dto.setActiveCategories(90);
        dto.setInactiveCategories(5);
        dto.setDeletedCategories(5);
        dto.setActivePercentage(90.0);
        dto.setInactivePercentage(5.0);
        dto.setDeletedPercentage(5.0);

        assertEquals(100, dto.getTotalCategories());
        assertEquals(90, dto.getActiveCategories());
        assertEquals(5, dto.getInactiveCategories());
        assertEquals(5, dto.getDeletedCategories());
        assertEquals(90.0, dto.getActivePercentage());
        assertEquals(5.0, dto.getInactivePercentage());
        assertEquals(5.0, dto.getDeletedPercentage());
    }

    @Test
    void testBuilder() {
        CategoryStatusDto dto = CategoryStatusDto.builder()
                .totalCategories(200)
                .activeCategories(150)
                .inactiveCategories(30)
                .deletedCategories(20)
                .activePercentage(75.0)
                .inactivePercentage(15.0)
                .deletedPercentage(10.0)
                .build();

        assertEquals(200, dto.getTotalCategories());
        assertEquals(150, dto.getActiveCategories());
        assertEquals(15.0, dto.getInactivePercentage());
    }

    @Test
    void testEqualsAndHashCode_sameValues() {
        CategoryStatusDto dto1 = CategoryStatusDto.builder()
                .totalCategories(10)
                .activeCategories(5)
                .inactiveCategories(3)
                .deletedCategories(2)
                .activePercentage(50.0)
                .inactivePercentage(30.0)
                .deletedPercentage(20.0)
                .build();

        CategoryStatusDto dto2 = CategoryStatusDto.builder()
                .totalCategories(10)
                .activeCategories(5)
                .inactiveCategories(3)
                .deletedCategories(2)
                .activePercentage(50.0)
                .inactivePercentage(30.0)
                .deletedPercentage(20.0)
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_differentValues() {
        CategoryStatusDto dto1 = CategoryStatusDto.builder().totalCategories(1).build();
        CategoryStatusDto dto2 = CategoryStatusDto.builder().totalCategories(99).build();

        assertNotEquals(dto1, dto2);
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        CategoryStatusDto dto = new CategoryStatusDto();
        assertNotEquals(dto, null);
        assertNotEquals(dto, "not a dto");
    }

    @Test
    void testEqualsSameInstance() {
        CategoryStatusDto dto = new CategoryStatusDto();
        assertEquals(dto, dto);
    }

    @Test
    void testToStringIsNotNull() {
        CategoryStatusDto dto = CategoryStatusDto.builder()
                .totalCategories(1)
                .activeCategories(1)
                .inactiveCategories(0)
                .deletedCategories(0)
                .activePercentage(100.0)
                .inactivePercentage(0.0)
                .deletedPercentage(0.0)
                .build();

        assertNotNull(dto.toString());
        assertTrue(dto.toString().contains("totalCategories=1"));
    }
}