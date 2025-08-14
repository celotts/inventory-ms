package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.category;

import com.celotts.productserviceOld.domain.model.CategoryModel;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.adapter.category.CategoryRepositoryAdapter;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.category.CategoryEntityMapper;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        var modelPage = new PageImpl<>(List.of(createModel()));

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