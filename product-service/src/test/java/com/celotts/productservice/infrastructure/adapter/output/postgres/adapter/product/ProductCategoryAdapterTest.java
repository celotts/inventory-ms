package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product.ProductCategoryAdapter;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductCategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductCategoryAdapterTest {

    @Mock
    private ProductCategoryRepository repository;

    @Mock
    private ProductCategoryEntityMapper mapper;

    @InjectMocks
    private ProductCategoryAdapter adapter;

    private ProductCategoryEntity entity;
    private ProductCategoryModel model;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity = ProductCategoryEntity.builder()
                .id(id)
                .productId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .assignedAt(now)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        model = ProductCategoryModel.builder()
                .id(id)
                .productId(entity.getProductId())
                .categoryId(entity.getCategoryId())
                .assignedAt(entity.getAssignedAt())
                .enabled(entity.getEnabled())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    @Test
    void testSave() {
        when(mapper.toEntity(model)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toModel(entity)).thenReturn(model);

        ProductCategoryModel saved = adapter.save(model);

        assertThat(saved).isEqualTo(model);
        verify(repository).save(entity);
    }

    @Test
    void testGetByIdFound() {
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        ProductCategoryModel found = adapter.getById(id);

        assertThat(found).isEqualTo(model);
    }

    @Test
    void testGetByIdNotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.getById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ProductCategory not found");
    }

    @Test
    void testGetAll() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        List<ProductCategoryModel> result = adapter.getAll();

        assertThat(result).hasSize(1).contains(model);
    }

    @Test
    void testDeleteById() {
        doNothing().when(repository).deleteById(id);

        adapter.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void testDisableByIdFound() {
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(any(ProductCategoryEntity.class))).thenReturn(entity);

        adapter.disableById(id);

        assertThat(entity.getEnabled()).isFalse();
        verify(repository).save(entity);
    }

    @Test
    void testDisableByIdNotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.disableById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ProductCategory not found");
    }
}