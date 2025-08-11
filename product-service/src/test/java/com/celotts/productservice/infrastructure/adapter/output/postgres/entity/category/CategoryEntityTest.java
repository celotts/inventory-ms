package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.category.CategoryRepositoryAdapter;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category.CategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class CategoryEntityTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        CategoryEntity entity = new CategoryEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setName("Bebidas");
        entity.setDescription("Todo tipo de bebidas");
        entity.setActive(false);
        entity.setCreatedBy("admin");
        entity.setUpdatedBy("user1");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertEquals(id, entity.getId());
        assertEquals("Bebidas", entity.getName());
        assertEquals("Todo tipo de bebidas", entity.getDescription());
        assertFalse(entity.getActive());
        assertEquals("admin", entity.getCreatedBy());
        assertEquals("user1", entity.getUpdatedBy());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        CategoryEntity entity = new CategoryEntity(
                id,"Snacks","Comida rápida",true,"system","editor",createdAt,updatedAt
        );

        assertEquals(id, entity.getId());
        assertEquals("Snacks", entity.getName());
        assertEquals("Comida rápida", entity.getDescription());
        assertTrue(entity.getActive());
        assertEquals("system", entity.getCreatedBy());
        assertEquals("editor", entity.getUpdatedBy());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }

    @Test
    void testPrePersistSetsCreatedAtAndDefaultActive() {
        CategoryEntity entity = new CategoryEntity();
        entity.setActive(null);
        entity.setCreatedAt(null);

        entity.prePersist();

        assertNotNull(entity.getCreatedAt());
        assertTrue(entity.getActive());
    }

    @Test
    void testPreUpdateSetsUpdatedAt() {
        CategoryEntity entity = new CategoryEntity();
        entity.setUpdatedAt(null);

        entity.preUpdate();

        assertNotNull(entity.getUpdatedAt());
    }

    @Test
    void testPrePersistSetsCreatedAtAndActiveWhenBothNull() {
        CategoryEntity entity = new CategoryEntity();
        entity.setCreatedAt(null);
        entity.setActive(null);

        entity.prePersist();

        assertNotNull(entity.getCreatedAt());
        assertTrue(entity.getActive());
    }

    @Test
    void testPrePersist_WhenCreatedAtNotNull_ShouldNotChangeIt() {
        CategoryEntity entity = new CategoryEntity();
        LocalDateTime original = LocalDateTime.of(2023, 1, 1, 0, 0);
        entity.setCreatedAt(original);
        entity.setActive(null);

        entity.prePersist();

        assertEquals(original, entity.getCreatedAt());
    }

    @Test
    void testPrePersist_WhenActiveNotNull_ShouldNotChangeIt() {
        CategoryEntity entity = new CategoryEntity();
        entity.setCreatedAt(null);
        entity.setActive(false);

        entity.prePersist();

        assertFalse(entity.getActive());
        assertNotNull(entity.getCreatedAt());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        String name = "Test Category";
        String description = "Category description";
        Boolean active = false;
        String createdBy = "admin";
        String updatedBy = "user";
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        CategoryEntity category = new CategoryEntity(
                id, name, description, active, createdBy, updatedBy, createdAt, updatedAt
        );

        assertEquals(id, category.getId());
        assertEquals(name, category.getName());
        assertEquals(description, category.getDescription());
        assertFalse(category.getActive());
        assertEquals(createdBy, category.getCreatedBy());
        assertEquals(updatedBy, category.getUpdatedBy());
        assertEquals(createdAt, category.getCreatedAt());
        assertEquals(updatedAt, category.getUpdatedAt());
    }

    @Test
    void testBuilder() {
        CategoryEntity category = CategoryEntity.builder()
                .name("Built Category")
                .description("desc")
                .build();

        assertEquals("Built Category", category.getName());
        assertEquals("desc", category.getDescription());
        assertTrue(category.getActive()); // @Builder.Default
    }

    @Test
    void testToString() {
        CategoryEntity category = CategoryEntity.builder().name("Test").build();
        String result = category.toString();
        assertNotNull(result);
        assertTrue(result.contains("Test"));
    }

    @Test
    void testEqualsAndHashCode_sameValues() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryEntity c1 = new CategoryEntity(id, "A", "desc", true, "admin", "admin", now, now);
        CategoryEntity c2 = new CategoryEntity(id, "A", "desc", true, "admin", "admin", now, now);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testEqualsAndHashCode_differentValues() {
        CategoryEntity c1 = CategoryEntity.builder().name("One").build();
        CategoryEntity c2 = CategoryEntity.builder().name("Two").build();

        assertNotEquals(c1, c2);
    }

    @Test
    void testPrePersist_setsDefaultsIfNull() {
        CategoryEntity category = new CategoryEntity();
        category.prePersist();

        assertNotNull(category.getCreatedAt());
        assertTrue(category.getActive());
    }

    @Test
    void testPrePersist_doesNotOverrideExistingValues() {
        CategoryEntity category = new CategoryEntity();
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        category.setCreatedAt(now);
        category.setActive(false);

        category.prePersist();

        assertEquals(now, category.getCreatedAt());
        assertFalse(category.getActive());
    }

    @Test
    void testPreUpdate_setsUpdatedAt() {
        CategoryEntity category = new CategoryEntity();
        assertNull(category.getUpdatedAt());

        category.preUpdate();
        assertNotNull(category.getUpdatedAt());
    }

    // ===== Adapter tests (inner class) =====
    class CategoryRepositoryAdapterTest {

        private CategoryRepository categoryRepository;
        private CategoryEntityMapper categoryEntityMapper;
        private CategoryRepositoryAdapter adapter;

        @BeforeEach
        void setUp() {
            categoryRepository = mock(CategoryRepository.class);
            categoryEntityMapper = mock(CategoryEntityMapper.class);
            adapter = new CategoryRepositoryAdapter(categoryRepository, categoryEntityMapper);
        }

        @Test
        void testSave() {
            var model = createModel();
            var entity = createEntity();
            when(categoryEntityMapper.toEntity(model)).thenReturn(entity);
            when(categoryRepository.save(entity)).thenReturn(entity);
            when(categoryEntityMapper.toDomain(entity)).thenReturn(model);

            var result = adapter.save(model);

            assertThat(result).isEqualTo(model);
        }

        @Test
        void testFindById() {
            UUID id = UUID.randomUUID();
            var entity = createEntity();
            var model = createModel();

            when(categoryRepository.findById(id)).thenReturn(Optional.of(entity));
            when(categoryEntityMapper.toDomain(entity)).thenReturn(model);

            var result = adapter.findById(id);

            assertThat(result).isPresent().contains(model);
        }

        @Test
        void testFindAll() {
            var entities = List.of(createEntity());
            var models = List.of(createModel());

            when(categoryRepository.findAll()).thenReturn(entities);
            when(categoryEntityMapper.toDomainList(entities)).thenReturn(models);

            var result = adapter.findAll();

            assertThat(result).isEqualTo(models);
        }

        @Test
        void testDeleteById() {
            UUID id = UUID.randomUUID();
            adapter.deleteById(id);
            verify(categoryRepository).deleteById(id);
        }

        @Test
        void testExistsById() {
            UUID id = UUID.randomUUID();
            when(categoryRepository.existsById(id)).thenReturn(true);

            boolean exists = adapter.existsById(id);

            assertThat(exists).isTrue();
        }

        @Test
        void testFindByName() {
            String name = "test";
            var entity = createEntity();
            var model = createModel();

            when(categoryRepository.findByName(name)).thenReturn(Optional.of(entity));
            when(categoryEntityMapper.toDomain(entity)).thenReturn(model);

            var result = adapter.findByName(name);

            assertThat(result).isPresent().contains(model);
        }

        @Test
        void testFindByNameContaining() {
            String name = "cat";
            var entities = List.of(createEntity());
            var models = List.of(createModel());

            when(categoryRepository.findByNameContaining(name)).thenReturn(entities);
            when(categoryEntityMapper.toDomainList(entities)).thenReturn(models);

            var result = adapter.findByNameContaining(name);

            assertThat(result).isEqualTo(models);
        }

        @Test
        void testExistsByName() {
            String name = "cat";
            when(categoryRepository.existsByName(name)).thenReturn(true);

            boolean exists = adapter.existsByName(name);

            assertThat(exists).isTrue();
        }

        @Test
        void testFindByActive() {
            var entities = List.of(createEntity());
            var models = List.of(createModel());

            when(categoryRepository.findByActive(true)).thenReturn(entities);
            when(categoryEntityMapper.toDomainList(entities)).thenReturn(models);

            var result = adapter.findByActive(true);

            assertThat(result).isEqualTo(models);
        }

        @Test
        void testFindAllPageable() {
            Pageable pageable = PageRequest.of(0, 10);
            var entityPage = new PageImpl<>(List.of(createEntity()));

            when(categoryRepository.findAll(pageable)).thenReturn(entityPage);
            when(categoryEntityMapper.toDomain(any())).thenReturn(createModel());

            var result = adapter.findAll(pageable);

            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        void testFindByActivePageable() {
            Pageable pageable = PageRequest.of(0, 10);
            var entityPage = new PageImpl<>(List.of(createEntity()));

            when(categoryRepository.findByActive(true, pageable)).thenReturn(entityPage);
            when(categoryEntityMapper.toDomain(any())).thenReturn(createModel());

            var result = adapter.findByActive(true, pageable);

            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        void testFindByNameContainingPageable() {
            Pageable pageable = PageRequest.of(0, 10);
            String name = "cat";
            var entityPage = new PageImpl<>(List.of(createEntity()));

            when(categoryRepository.findByNameContaining(name, pageable)).thenReturn(entityPage);
            when(categoryEntityMapper.toDomain(any())).thenReturn(createModel());

            var result = adapter.findByNameContaining(name, pageable);

            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        void testFindAllById() {
            UUID id = UUID.randomUUID();
            var ids = List.of(id);
            var entities = List.of(createEntity());
            var models = List.of(createModel());

            when(categoryRepository.findAllById(ids)).thenReturn(entities);
            when(categoryEntityMapper.toDomainList(entities)).thenReturn(models);

            var result = adapter.findAllById(ids);

            assertThat(result).isEqualTo(models);
        }

        @Test
        void testFindByNameOrDescription() {
            String term = "test";
            int limit = 10;
            var entities = List.of(createEntity());
            var models = List.of(createModel());

            when(categoryRepository.findByNameOrDescription(term, limit)).thenReturn(entities);
            when(categoryEntityMapper.toDomainList(entities)).thenReturn(models);

            var result = adapter.findByNameOrDescription(term, limit);

            assertThat(result).isEqualTo(models);
        }

        @Test
        void testCountByActive() {
            when(categoryRepository.countByActive(true)).thenReturn(5L);

            long count = adapter.countByActive(true);

            assertThat(count).isEqualTo(5L);
        }

        @Test
        void testFindByNameContainingAndActive() {
            Pageable pageable = PageRequest.of(0, 10);
            var entityPage = new PageImpl<>(List.of(createEntity()));

            when(categoryRepository.findByNameContainingAndActive("cat", true, pageable)).thenReturn(entityPage);
            when(categoryEntityMapper.toDomain(any())).thenReturn(createModel());

            var result = adapter.findByNameContainingAndActive("cat", true, pageable);

            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        void testFindAllPaginated_combinedConditions() {
            Pageable pageable = PageRequest.of(0, 10);
            var entityPage = new PageImpl<>(List.of(createEntity()));

            when(categoryRepository.findByNameContainingAndActive("cat", true, pageable)).thenReturn(entityPage);
            when(categoryEntityMapper.toDomain(any())).thenReturn(createModel());

            var result = adapter.findAllPaginated("cat", true, pageable);

            assertThat(result.getContent()).hasSize(1);
        }

        private CategoryEntity createEntity() {
            return CategoryEntity.builder()
                    .id(UUID.randomUUID())
                    .name("Test")
                    .description("Desc")
                    .active(true)
                    .build();
        }

        private CategoryModel createModel() {
            return CategoryModel.builder()
                    .id(UUID.randomUUID())
                    .name("Test")
                    .description("Desc")
                    .active(true)
                    .build();
        }

        @Test
        void testFindAllPaginated_withNameOnly() {
            Pageable pageable = PageRequest.of(0, 10);
            String name = "cat";
            var entityPage = new PageImpl<>(List.of(createEntity()));

            when(categoryRepository.findByNameContaining(name, pageable)).thenReturn(entityPage);
            when(categoryEntityMapper.toDomain(any())).thenReturn(createModel());

            var result = adapter.findAllPaginated(name, null, pageable);

            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        void testFindAllPaginated_withActiveOnly() {
            Pageable pageable = PageRequest.of(0, 10);
            var entityPage = new PageImpl<>(List.of(createEntity()));

            when(categoryRepository.findByActive(true, pageable)).thenReturn(entityPage);
            when(categoryEntityMapper.toDomain(any())).thenReturn(createModel());

            var result = adapter.findAllPaginated(null, true, pageable);

            assertThat(result.getContent()).hasSize(1);
        }

        @Test
        void testFindAllPaginated_withoutFilters() {
            Pageable pageable = PageRequest.of(0, 10);
            var entityPage = new PageImpl<>(List.of(createEntity()));

            when(categoryRepository.findAll(pageable)).thenReturn(entityPage);
            when(categoryEntityMapper.toDomain(any())).thenReturn(createModel());

            var result = adapter.findAllPaginated(null, null, pageable);

            assertThat(result.getContent()).hasSize(1);
        }
    }

    @Test
    void equals_shouldBeReflexiveSymmetricAndTransitive() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        CategoryEntity a = new CategoryEntity(id, "N", "D", true, "c", "u", now, now);
        CategoryEntity b = new CategoryEntity(id, "N", "D", true, "c", "u", now, now);
        CategoryEntity c = new CategoryEntity(id, "N", "D", true, "c", "u", now, now);

        // reflexivo
        assertEquals(a, a);
        // simétrico
        assertEquals(a, b);
        assertEquals(b, a);
        // transitivo
        assertEquals(b, c);
        assertEquals(a, c);
        // null y clase distinta
        assertNotEquals(a, null);
        assertNotEquals(a, "otro");
    }

    @Test
    void equals_shouldReturnFalse_whenAnySingleFieldDiffers() {
        UUID id = UUID.randomUUID();
        LocalDateTime t = LocalDateTime.now();

        CategoryEntity base = new CategoryEntity(id, "N", "D", true, "c", "u", t, t);

        // Para cada campo, creamos una copia con solo ese campo distinto.
        assertNotEquals(base, new CategoryEntity(UUID.randomUUID(), "N", "D", true, "c", "u", t, t));               // id
        assertNotEquals(base, new CategoryEntity(id, "X", "D", true, "c", "u", t, t));                              // name
        assertNotEquals(base, new CategoryEntity(id, "N", "DX", true, "c", "u", t, t));                             // description
        assertNotEquals(base, new CategoryEntity(id, "N", "D", false, "c", "u", t, t));                             // active
        assertNotEquals(base, new CategoryEntity(id, "N", "D", true, "cx", "u", t, t));                             // createdBy
        assertNotEquals(base, new CategoryEntity(id, "N", "D", true, "c", "ux", t, t));                             // updatedBy
        assertNotEquals(base, new CategoryEntity(id, "N", "D", true, "c", "u", t.minusSeconds(1), t));             // createdAt
        assertNotEquals(base, new CategoryEntity(id, "N", "D", true, "c", "u", t, t.plusSeconds(1)));              // updatedAt
    }

    @Test
    void hashCode_shouldMatchWhenEqual_andDifferWhenFieldChanges() {
        UUID id = UUID.randomUUID();
        LocalDateTime t = LocalDateTime.now();

        CategoryEntity a = new CategoryEntity(id, "N", "D", true, "c", "u", t, t);
        CategoryEntity b = new CategoryEntity(id, "N", "D", true, "c", "u", t, t);
        CategoryEntity diff = new CategoryEntity(id, "N2", "D", true, "c", "u", t, t);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());   // consistencia con equals
        assertNotEquals(a.hashCode(), diff.hashCode()); // cambia si cambia un campo relevante
    }

    @Test
    void preUpdate_shouldOverwriteExistingUpdatedAt() throws InterruptedException {
        CategoryEntity entity = new CategoryEntity();
        // Simulamos que ya venía con fecha previa
        LocalDateTime previous = LocalDateTime.now().minusDays(1);
        entity.setUpdatedAt(previous);

        // Pausita mínima para asegurar comparación temporal si el reloj es muy rápido
        Thread.sleep(2);
        entity.preUpdate();

        assertNotNull(entity.getUpdatedAt());
        assertTrue(entity.getUpdatedAt().isAfter(previous));
    }
}