package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductRepositoryAdapterTest {

    private ProductRepository productRepository;
    private ProductEntityMapper mapper;
    private ProductRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        mapper = mock(ProductEntityMapper.class);
        adapter = new ProductRepositoryAdapter(productRepository, mapper);
    }

    @Test
    void testExistsById() {
        UUID id = UUID.randomUUID();
        when(productRepository.existsById(id)).thenReturn(true);
        assertThat(adapter.existsById(id)).isTrue();
    }

    @Test
    void testExistsByCode() {
        when(productRepository.existsByCode("P001")).thenReturn(true);
        assertThat(adapter.existsByCode("P001")).isTrue();
    }

    @Test
    void testFindByCode() {
        var entity = createEntity();
        var model = createModel();
        when(productRepository.findByCode("P001")).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);
        var result = adapter.findByCode("P001");
        assertThat(result).contains(model);
    }

    @Test
    void testFindByCategoryId() {
        UUID categoryId = UUID.randomUUID();
        var entity = createEntity();
        var model = createModel();
        when(productRepository.findByCategoryId(categoryId)).thenReturn(List.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);
        var result = adapter.findByCategoryId(categoryId);
        assertThat(result).containsExactly(model);
    }

    @Test
    void testFindByBrandId() {
        UUID brandId = UUID.randomUUID();
        var entity = createEntity();
        var model = createModel();
        when(productRepository.findByBrandId(brandId)).thenReturn(List.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);
        var result = adapter.findByBrandId(brandId);
        assertThat(result).containsExactly(model);
    }

    @Test
    void testFindByEnabled() {
        var pageable = PageRequest.of(0, 10);
        var entity = createEntity();
        var model = createModel();
        var page = new PageImpl<>(List.of(entity));
        when(productRepository.findByEnabled(true, pageable)).thenReturn(page);
        when(mapper.toModel(entity)).thenReturn(model);
        var result = adapter.findByEnabled(true, pageable);
        assertThat(result.getContent()).containsExactly(model);
    }

    @Test
    void testSave() {
        var model = createModel();
        var entity = createEntity();
        when(productRepository.save(any())).thenReturn(entity);
        when(mapper.toModel(entity)).thenReturn(model);
        var result = adapter.save(model);
        assertThat(result).isEqualTo(model);
    }

    @Test
    void testFindAll() {
        var entity = createEntity();
        var model = createModel();
        when(productRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);
        var result = adapter.findAll();
        assertThat(result).containsExactly(model);
    }

    @Test
    void testFindAllPageable() {
        var pageable = PageRequest.of(0, 5);
        var entity = createEntity();
        var model = createModel();
        var page = new PageImpl<>(List.of(entity));
        when(productRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toModel(entity)).thenReturn(model);
        var result = adapter.findAll(pageable);
        assertThat(result.getContent()).containsExactly(model);
    }

    @Test
    void testFindAllWithFilters() {
        var pageable = PageRequest.of(0, 5);
        var entity = createEntity();
        var model = createModel();
        var page = new PageImpl<>(List.of(entity));
        when(productRepository.findAllWithFilters(pageable, "P001", "Product", "Desc")).thenReturn(page);
        when(mapper.toModel(entity)).thenReturn(model);
        var result = adapter.findAllWithFilters(pageable, "P001", "Product", "Desc");
        assertThat(result.getContent()).containsExactly(model);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        var entity = createEntity();
        var model = createModel();
        when(productRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);
        var result = adapter.findById(id);
        assertThat(result).contains(model);
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(productRepository).deleteById(id);
    }

    // ==== Helpers ====

    private ProductModel createModel() {
        return ProductModel.builder()
                .id(UUID.randomUUID())
                .code("P001")
                .name("Product")
                .description("Description")
                .enabled(true)
                .build();
    }

    private ProductEntity createEntity() {
        ProductEntity entity = new ProductEntity();
        entity.setId(UUID.randomUUID());
        entity.setCode("P001");
        entity.setName("Product");
        entity.setDescription("Description");
        entity.setEnabled(true);
        return entity;
    }
}