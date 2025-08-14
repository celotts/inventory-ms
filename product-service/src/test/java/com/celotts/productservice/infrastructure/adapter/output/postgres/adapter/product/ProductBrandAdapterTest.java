package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productserviceOld.domain.model.ProductBrandModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.adapter.product.ProductBrandAdapter;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.product.ProductBrandEntityMapper;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository.product.ProductBrandRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductBrandAdapterTest {

    private ProductBrandRepository productBrandRepository;
    private ProductBrandEntityMapper mapper;
    private ProductBrandAdapter adapter;

    @BeforeEach
    void setUp() {
        productBrandRepository = mock(ProductBrandRepository.class);
        mapper = mock(ProductBrandEntityMapper.class);
        adapter = new ProductBrandAdapter(productBrandRepository, mapper);
    }

    @Test
    void testSave() {
        var model = createModel();
        var entity = createEntity();

        when(mapper.toEntity(model)).thenReturn(entity);
        when(productBrandRepository.save(entity)).thenReturn(entity);
        when(mapper.toModel(entity)).thenReturn(model);

        var result = adapter.save(model);
        assertThat(result).isEqualTo(model);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        var entity = createEntity();
        var model = createModel();

        when(productBrandRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        var result = adapter.findById(id);
        assertThat(result).isPresent().contains(model);
    }

    @Test
    void testFindByName() {
        var entity = createEntity();
        var model = createModel();
        when(productBrandRepository.findByName("Test")).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        var result = adapter.findByName("Test");

        assertThat(result).isPresent().contains(model);
    }

    @Test
    void testFindNameById() {
        UUID id = UUID.randomUUID();
        when(productBrandRepository.findNameById(id)).thenReturn(Optional.of("Brand Name"));

        var result = adapter.findNameById(id);

        assertThat(result).isPresent().contains("Brand Name");
    }

    @Test
    void testFindAll() {
        var entities = List.of(createEntity());
        var models = List.of(createModel());

        when(productBrandRepository.findAll()).thenReturn(entities);
        when(mapper.toModel(entities.get(0))).thenReturn(models.get(0));

        var result = adapter.findAll();

        assertThat(result).isEqualTo(models);
    }

    @Test
    void testFindAllIds() {
        UUID id = UUID.randomUUID();
        ProductBrandEntity entity = createEntity();
        entity.setId(id);

        when(productBrandRepository.findAll()).thenReturn(List.of(entity));

        var result = adapter.findAllIds();

        assertThat(result).containsExactly(id);
    }

    @Test
    void testExistsByName() {
        when(productBrandRepository.existsByName("Brand")).thenReturn(true);

        boolean exists = adapter.existsByName("Brand");

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsById() {
        UUID id = UUID.randomUUID();
        when(productBrandRepository.existsById(id)).thenReturn(true);

        boolean exists = adapter.existsById(id);

        assertThat(exists).isTrue();
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();
        adapter.deleteById(id);
        verify(productBrandRepository).deleteById(id);
    }

    @Test
    void testEnableBrand() {
        UUID id = UUID.randomUUID();
        ProductBrandEntity entity = createEntity();

        ProductBrandResponseDto responseDto = ProductBrandResponseDto.builder()
                .id(id)
                .name("Test")
                .description("Description")
                .enabled(true)
                .createdBy(null)
                .updatedBy(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        doNothing().when(productBrandRepository).enableBrandById(id);
        when(productBrandRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDto(entity)).thenReturn(responseDto);

        var result = adapter.enableBrand(id);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void testDisableBrand() {
        UUID id = UUID.randomUUID();
        ProductBrandEntity entity = createEntity();

        ProductBrandResponseDto responseDto = ProductBrandResponseDto.builder()
                .id(id)
                .name("Test")
                .description("Description")
                .enabled(false)
                .createdBy(null)
                .updatedBy(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        doNothing().when(productBrandRepository).disableBrandById(id);
        when(productBrandRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDto(entity)).thenReturn(responseDto);

        var result = adapter.disableBrand(id);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    void testEnableBrand_throwsIfNotFound() {
        UUID id = UUID.randomUUID();
        doNothing().when(productBrandRepository).enableBrandById(id);
        when(productBrandRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.enableBrand(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product brand not found");
    }

    @Test
    void testDisableBrand_throwsIfNotFound() {
        UUID id = UUID.randomUUID();
        doNothing().when(productBrandRepository).disableBrandById(id);
        when(productBrandRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.disableBrand(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product brand not found");
    }

    // ==== Helpers ====

    private ProductBrandModel createModel() {
        return ProductBrandModel.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .description("Description")
                .enabled(true) // ← Este es el nombre correcto
                .build();
    }

    private ProductBrandEntity createEntity() {
        ProductBrandEntity entity = new ProductBrandEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Test");
        entity.setDescription("Description");
        entity.setEnabled(true);
        return entity;
    }
}