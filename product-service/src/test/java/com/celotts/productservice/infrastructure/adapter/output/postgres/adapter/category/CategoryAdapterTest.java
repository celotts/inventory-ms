package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.category.CategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryAdapterTest {

    private CategoryRepository categoryRepository;
    private CategoryEntityMapper categoryEntityMapper;
    private EntityManager entityManager;

    private CategoryAdapter categoryAdapter;

    private UUID categoryId;
    private CategoryModel categoryModel;
    private CategoryEntity categoryEntity;


    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        categoryEntityMapper = mock(CategoryEntityMapper.class);
        entityManager = mock(EntityManager.class);

        categoryAdapter = new CategoryAdapter(categoryRepository, categoryEntityMapper, entityManager);

        categoryId = UUID.randomUUID();
        categoryModel = CategoryModel.builder()
                .id(categoryId)
                .name("Bebidas")
                .description("Bebidas frías y calientes")
                .active(true)
                .createdBy("admin")
                .createdAt(LocalDateTime.now())
                .build();

        categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryId);
        categoryEntity.setName("Bebidas");
        categoryEntity.setDescription("Bebidas frías y calientes");
        categoryEntity.setActive(true);
        categoryEntity.setCreatedBy("admin");
        categoryEntity.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testFindByActive() {
        List<CategoryEntity> entities = List.of(new CategoryEntity());
        List<CategoryModel> models = List.of(new CategoryModel());

        when(categoryRepository.findByActive(true)).thenReturn(entities);
        when(categoryEntityMapper.toDomainList(entities)).thenReturn(models);

        List<CategoryModel> result = categoryAdapter.findByActive(true);

        assertEquals(models, result);
    }

    @Test
    void testFindByActiveWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryEntity> entities = new PageImpl<>(List.of(new CategoryEntity()));
        Page<CategoryModel> expected = new PageImpl<>(List.of(new CategoryModel()));

        when(categoryRepository.findByActive(true, pageable)).thenReturn(entities);
        when(categoryEntityMapper.toDomain(any())).thenReturn(new CategoryModel());

        Page<CategoryModel> result = categoryAdapter.findByActive(true, pageable);

        assertEquals(expected.getContent().size(), result.getContent().size());
    }

    @Test
    void testFindByNameContainingWithPageable() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<CategoryEntity> entities = new PageImpl<>(List.of(new CategoryEntity()));

        when(categoryRepository.findByNameContaining("test", pageable)).thenReturn(entities);
        when(categoryEntityMapper.toDomain(any())).thenReturn(new CategoryModel());

        Page<CategoryModel> result = categoryAdapter.findByNameContaining("test", pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindByNameContainingAndActiveWithPageable() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<CategoryEntity> entities = new PageImpl<>(List.of(new CategoryEntity()));

        when(categoryRepository.findByNameContainingAndActive("test", true, pageable)).thenReturn(entities);
        when(categoryEntityMapper.toDomain(any())).thenReturn(new CategoryModel());

        Page<CategoryModel> result = categoryAdapter.findByNameContainingAndActive("test", true, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindAllPageable() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<CategoryEntity> entities = new PageImpl<>(List.of(new CategoryEntity()));

        when(categoryRepository.findAll(pageable)).thenReturn(entities);
        when(categoryEntityMapper.toDomain(any())).thenReturn(new CategoryModel());

        Page<CategoryModel> result = categoryAdapter.findAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testFindAllById() {
        List<UUID> ids = List.of(UUID.randomUUID());
        List<CategoryEntity> entities = List.of(new CategoryEntity());
        List<CategoryModel> models = List.of(new CategoryModel());

        when(categoryRepository.findAllById(ids)).thenReturn(entities);
        when(categoryEntityMapper.toDomainList(entities)).thenReturn(models);

        List<CategoryModel> result = categoryAdapter.findAllById(ids);

        assertEquals(models, result);
    }

    @Test
    void testFindByNameOrDescription() {
        String term = "abc";
        CategoryEntity entity = new CategoryEntity();
        List<CategoryEntity> entities = List.of(entity);
        List<CategoryModel> models = List.of(new CategoryModel());

        TypedQuery<CategoryEntity> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(CategoryEntity.class))).thenReturn(query);
        when(query.setParameter(eq("term"), anyString())).thenReturn(query);
        when(query.setMaxResults(anyInt())).thenReturn(query);
        when(query.getResultList()).thenReturn(entities);
        when(categoryEntityMapper.toDomainList(entities)).thenReturn(models);

        List<CategoryModel> result = categoryAdapter.findByNameOrDescription(term, 5);

        assertEquals(models, result);
    }

    @Test
    void testFindAllPaginated() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<CategoryEntity> entities = new PageImpl<>(List.of(new CategoryEntity()));
        when(categoryEntityMapper.toDomain(any())).thenReturn(new CategoryModel());

        // Case 1: name + active
        when(categoryRepository.findByNameContainingAndActive("name", true, pageable)).thenReturn(entities);
        Page<CategoryModel> result1 = categoryAdapter.findAllPaginated("name", true, pageable);
        assertEquals(1, result1.getTotalElements());

        // Case 2: name only
        when(categoryRepository.findByNameContaining("name", pageable)).thenReturn(entities);
        Page<CategoryModel> result2 = categoryAdapter.findAllPaginated("name", null, pageable);
        assertEquals(1, result2.getTotalElements());

        // Case 3: active only
        when(categoryRepository.findByActive(true, pageable)).thenReturn(entities);
        Page<CategoryModel> result3 = categoryAdapter.findAllPaginated(null, true, pageable);
        assertEquals(1, result3.getTotalElements());

        // Case 4: neither
        when(categoryRepository.findAll(pageable)).thenReturn(entities);
        Page<CategoryModel> result4 = categoryAdapter.findAllPaginated(null, null, pageable);
        assertEquals(1, result4.getTotalElements());
    }

    @Test
    void testCountByActive() {
        when(categoryRepository.countByActive(true)).thenReturn(3L);

        long result = categoryAdapter.countByActive(true);

        assertEquals(3L, result);
    }

    @Test
    void testSave() {
        when(categoryEntityMapper.toEntity(categoryModel)).thenReturn(categoryEntity);
        when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
        when(categoryEntityMapper.toDomain(categoryEntity)).thenReturn(categoryModel);

        CategoryModel result = categoryAdapter.save(categoryModel);

        assertThat(result).isEqualTo(categoryModel);
        verify(categoryRepository).save(categoryEntity);
    }

    @Test
    void testFindById() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryEntity));
        when(categoryEntityMapper.toDomain(categoryEntity)).thenReturn(categoryModel);

        Optional<CategoryModel> result = categoryAdapter.findById(categoryId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(categoryModel);
    }

    @Test
    void testFindByName() {
        when(categoryRepository.findByName("Bebidas")).thenReturn(Optional.of(categoryEntity));
        when(categoryEntityMapper.toDomain(categoryEntity)).thenReturn(categoryModel);

        Optional<CategoryModel> result = categoryAdapter.findByName("Bebidas");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Bebidas");
    }

    @Test
    void testFindAll() {
        List<CategoryEntity> entityList = List.of(categoryEntity);
        List<CategoryModel> modelList = List.of(categoryModel);

        when(categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))).thenReturn(entityList);
        when(categoryEntityMapper.toDomainList(entityList)).thenReturn(modelList);

        List<CategoryModel> result = categoryAdapter.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Bebidas");
    }

    @Test
    void testFindByNameContaining() {
        List<CategoryEntity> entityList = List.of(categoryEntity);
        List<CategoryModel> modelList = List.of(categoryModel);

        when(categoryRepository.findByNameContaining("Bebi")).thenReturn(entityList);
        when(categoryEntityMapper.toDomainList(entityList)).thenReturn(modelList);

        List<CategoryModel> result = categoryAdapter.findByNameContaining("Bebi");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).contains("Bebi");
    }

    @Test
    void testExistsByName() {
        when(categoryRepository.existsByName("Bebidas")).thenReturn(true);

        boolean exists = categoryAdapter.existsByName("Bebidas");

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsById() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        boolean exists = categoryAdapter.existsById(categoryId);

        assertThat(exists).isTrue();
    }

    @Test
    void testDeleteById() {
        categoryAdapter.deleteById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }
}