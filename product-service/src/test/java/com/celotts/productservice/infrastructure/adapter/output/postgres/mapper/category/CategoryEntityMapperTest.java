package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryEntityMapperTest {

    private CategoryEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CategoryEntityMapper();
    }

    @Test
    void toDomain_shouldMapCorrectly() {
        CategoryEntity entity = new CategoryEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setName("Alimentos");
        entity.setDescription("Categoría de comida");
        entity.setActive(true);
        entity.setCreatedBy("admin");
        entity.setUpdatedBy("admin2");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        CategoryModel model = mapper.toDomain(entity);

        assertNotNull(model);
        assertEquals(id, model.getId());
        assertEquals("Alimentos", model.getName());
        assertEquals("Categoría de comida", model.getDescription());
        assertTrue(model.getActive());
        assertEquals("admin", model.getCreatedBy());
        assertEquals("admin2", model.getUpdatedBy());
        assertEquals(now, model.getCreatedAt());
        assertEquals(now, model.getUpdatedAt());
    }

    @Test
    void toDomain_shouldReturnNull_whenEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toEntity_shouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryModel model = CategoryModel.builder()
                .id(id)
                .name("Electrónica")
                .description("Categoría tecnológica")
                .active(false)
                .createdBy("user")
                .updatedBy("mod")
                .createdAt(now)
                .updatedAt(now)
                .build();

        CategoryEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Electrónica", entity.getName());
        assertEquals("Categoría tecnológica", entity.getDescription());
        assertFalse(entity.getActive());
        assertEquals("user", entity.getCreatedBy());
        assertEquals("mod", entity.getUpdatedBy());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void toEntity_shouldReturnNull_whenModelIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDomainList_shouldMapListCorrectly() {
        CategoryEntity entity1 = new CategoryEntity();
        entity1.setId(UUID.randomUUID());
        entity1.setName("Ropa");

        CategoryEntity entity2 = new CategoryEntity();
        entity2.setId(UUID.randomUUID());
        entity2.setName("Calzado");

        List<CategoryModel> result = mapper.toDomainList(List.of(entity1, entity2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ropa", result.get(0).getName());
        assertEquals("Calzado", result.get(1).getName());
    }

    @Test
    void toEntityList_shouldMapListCorrectly() {
        CategoryModel model1 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Deportes")
                .build();

        CategoryModel model2 = CategoryModel.builder()
                .id(UUID.randomUUID())
                .name("Música")
                .build();

        List<CategoryEntity> result = mapper.toEntityList(List.of(model1, model2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Deportes", result.get(0).getName());
        assertEquals("Música", result.get(1).getName());
    }

    @Test
    void toDomainList_shouldHandleEmptyList() {
        List<CategoryModel> result = mapper.toDomainList(List.of());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toEntityList_shouldHandleEmptyList() {
        List<CategoryEntity> result = mapper.toEntityList(List.of());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}