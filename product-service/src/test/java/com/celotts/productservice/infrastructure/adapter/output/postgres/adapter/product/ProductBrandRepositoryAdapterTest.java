package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductBrandEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductBrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductBrandRepositoryAdapterTest {

    @Mock
    private ProductBrandRepository repository;

    @Mock
    private ProductBrandEntityMapper mapper;

    @InjectMocks
    private ProductBrandRepositoryAdapter adapter;

    private ProductBrandModel model;
    private ProductBrandEntity entity;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();

        model = ProductBrandModel.builder()
                .id(id)
                .name("Brand")
                .description("Desc")
                .enabled(true)
                .createdBy("system")
                .updatedBy("system")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        entity = ProductBrandEntity.builder()
                .id(id)
                .name("Brand")
                .description("Desc")
                .enabled(true)
                .createdBy("system")
                .updatedBy("system")
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    @Test
    void testSave() {
        when(mapper.toEntity(model)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toModel(entity)).thenReturn(model);

        ProductBrandModel saved = adapter.save(model);

        assertThat(saved).isEqualTo(model);
    }

    @Test
    void testFindById() {
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        Optional<ProductBrandModel> result = adapter.findById(id);

        assertThat(result).isPresent().contains(model);
    }

    @Test
    void testFindByName() {
        when(repository.findByName("Brand")).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        Optional<ProductBrandModel> result = adapter.findByName("Brand");

        assertThat(result).isPresent().contains(model);
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        List<ProductBrandModel> result = adapter.findAll();

        assertThat(result).hasSize(1).contains(model);
    }

    @Test
    void testExistsByName() {
        when(repository.existsByName("Brand")).thenReturn(true);

        boolean exists = adapter.existsByName("Brand");

        assertThat(exists).isTrue();
    }

    @Test
    void testExistsById() {
        when(repository.existsById(id)).thenReturn(true);

        boolean exists = adapter.existsById(id);

        assertThat(exists).isTrue();
    }

    @Test
    void testDeleteById() {
        doNothing().when(repository).deleteById(id);

        adapter.deleteById(id);

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testFindNameById() {
        when(repository.findNameById(id)).thenReturn(Optional.of("Brand"));

        Optional<String> name = adapter.findNameById(id);

        assertThat(name).isPresent().contains("Brand");
    }

    @Test
    void testFindAllIds() {
        when(repository.findAllIds()).thenReturn(List.of(id));

        List<UUID> ids = adapter.findAllIds();

        assertThat(ids).hasSize(1).contains(id);
    }
}