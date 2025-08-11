package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.category.CategoryUpdateDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoMapperTest {

    @Test
    void toModelFromCreate_shouldMapCorrectly() {
        CategoryCreateDto dto = new CategoryCreateDto();
        dto.setName("Tecnología");
        dto.setDescription("Productos electrónicos");

        CategoryModel model = CategoryDtoMapper.toModelFromCreate(dto);

        assertNotNull(model);
        assertEquals("Tecnología", model.getName());
        assertEquals("Productos electrónicos", model.getDescription());
        assertTrue(model.getActive());
        assertEquals("system", model.getCreatedBy());
        assertNotNull(model.getCreatedAt());
    }

    @Test
    void toModelFromCreate_shouldReturnNull_whenDtoIsNull() {
        assertNull(CategoryDtoMapper.toModelFromCreate(null));
    }

    @Test
    void toModelFromUpdate_shouldMapCorrectly() {
        CategoryUpdateDto dto = new CategoryUpdateDto();
        dto.setName("Hogar");
        dto.setDescription("Muebles");
        dto.setActive(false);
        dto.setUpdatedBy("admin");

        CategoryModel model = CategoryDtoMapper.toModelFromUpdate(dto);

        assertNotNull(model);
        assertEquals("Hogar", model.getName());
        assertEquals("Muebles", model.getDescription());
        assertFalse(model.getActive());
        assertEquals("admin", model.getUpdatedBy());
        assertNotNull(model.getUpdatedAt());
    }

    @Test
    void toModelFromUpdate_shouldDefaultToSystem_whenUpdatedByIsNull() {
        CategoryUpdateDto dto = new CategoryUpdateDto();
        dto.setName("Jardinería");
        dto.setDescription("Herramientas");
        dto.setActive(true);

        CategoryModel model = CategoryDtoMapper.toModelFromUpdate(dto);

        assertNotNull(model);
        assertEquals("system", model.getUpdatedBy());
        assertTrue(model.getActive());
    }

    @Test
    void toModelFromUpdate_shouldReturnNull_whenDtoIsNull() {
        assertNull(CategoryDtoMapper.toModelFromUpdate(null));
    }

    @Test
    void toResponseDto_shouldMapCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        UUID id = UUID.randomUUID();

        CategoryModel model = CategoryModel.builder()
                .id(id)
                .name("Libros")
                .description("Literatura")
                .active(true)
                .createdBy("admin")
                .updatedBy("user")
                .createdAt(now)
                .updatedAt(now)
                .build();

        CategoryResponseDto dto = CategoryDtoMapper.toResponseDto(model);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("Libros", dto.getName());
        assertEquals("Literatura", dto.getDescription());
        assertTrue(dto.getActive());
        assertEquals("admin", dto.getCreatedBy());
        assertEquals("user", dto.getUpdatedBy());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void toResponseDto_shouldReturnNull_whenModelIsNull() {
        assertNull(CategoryDtoMapper.toResponseDto(null));
    }

    @Test
    void toResponseDtoList_shouldMapListCorrectly() {
        CategoryModel model = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Juguetes")
                .description("Para niños")
                .active(true)
                .build();

        List<CategoryResponseDto> dtos = CategoryDtoMapper.toResponseDtoList(List.of(model));

        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals("Juguetes", dtos.get(0).getName());
    }

    @Test
    void toResponseDtoList_shouldReturnEmptyList_whenInputIsEmpty() {
        List<CategoryResponseDto> result = CategoryDtoMapper.toResponseDtoList(Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toResponseDtoList_shouldReturnNull_whenInputIsNull() {
        assertNull(CategoryDtoMapper.toResponseDtoList(null));
    }

    @Test
    void toModel_shouldHandleCategoryCreateDto() {
        CategoryCreateDto dto = new CategoryCreateDto();
        dto.setName("Vestimenta");
        dto.setDescription("Ropa y accesorios");

        CategoryModel model = CategoryDtoMapper.toModelFromCreate(dto);

        assertNotNull(model);
        assertEquals("Vestimenta", model.getName());
        assertTrue(model.getActive());
    }

    @Test
    void toModel_shouldHandleCategoryUpdateDto() {
        CategoryUpdateDto dto = new CategoryUpdateDto();
        dto.setName("Deportes");
        dto.setDescription("Artículos deportivos");
        dto.setActive(true);

        CategoryModel model = CategoryDtoMapper.toModelFromUpdate(dto);

        assertNotNull(model);
        assertEquals("Deportes", model.getName());
        assertTrue(model.getActive());
    }

    @SuppressWarnings("deprecation")
    @Test
    void toModel_shouldThrowException_whenUnsupportedDto() {
        String unsupported = "invalido";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CategoryDtoMapper.toModel(unsupported)
        );

        assertEquals("Unsupported DTO type: String", exception.getMessage());
    }
}