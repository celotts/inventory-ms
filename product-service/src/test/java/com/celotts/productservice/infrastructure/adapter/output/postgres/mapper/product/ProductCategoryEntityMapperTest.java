package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryResponseDto;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductCategoryEntityMapperTest {

    private ProductCategoryEntityMapper mapper;

    private UUID id;
    private UUID productId;
    private UUID categoryId;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        mapper = new ProductCategoryEntityMapper();
        id = UUID.randomUUID();
        productId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        now = LocalDateTime.now();
    }

    @Test
    void testToEntity() {
        ProductCategoryModel model = ProductCategoryModel.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(now)
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .build();

        ProductCategoryEntity entity = mapper.toEntity(model);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getProductId()).isEqualTo(productId);
        assertThat(entity.getCategoryId()).isEqualTo(categoryId);
        assertThat(entity.getEnabled()).isTrue();
        assertThat(entity.getCreatedBy()).isEqualTo("admin");
        assertThat(entity.getUpdatedBy()).isEqualTo("editor");
    }

    @Test
    void testToModel() {
        ProductCategoryEntity entity = ProductCategoryEntity.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(now)
                .enabled(false)
                .createdBy("creator")
                .updatedBy("updater")
                .createdAt(now.minusDays(2))
                .updatedAt(now)
                .build();

        ProductCategoryModel model = mapper.toModel(entity);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getProductId()).isEqualTo(productId);
        assertThat(model.getCategoryId()).isEqualTo(categoryId);
        assertThat(model.getEnabled()).isFalse();
        assertThat(model.getCreatedBy()).isEqualTo("creator");
        assertThat(model.getUpdatedBy()).isEqualTo("updater");
    }

    @Test
    void testToResponseDto() {
        ProductCategoryEntity entity = ProductCategoryEntity.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .assignedAt(now)
                .enabled(true)
                .createdBy("a")
                .updatedBy("b")
                .createdAt(now.minusDays(5))
                .updatedAt(now)
                .build();

        ProductCategoryResponseDto dto = mapper.toResponseDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getProductId()).isEqualTo(productId);
        assertThat(dto.getCategoryId()).isEqualTo(categoryId);
        assertThat(dto.getEnabled()).isTrue();
        assertThat(dto.getCreatedBy()).isEqualTo("a");
        assertThat(dto.getUpdatedBy()).isEqualTo("b");
    }

    @Test
    void testToEntity_Null() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void testToModel_Null() {
        assertThat(mapper.toModel(null)).isNull();
    }

    @Test
    void testToResponseDto_Null() {
        assertThat(mapper.toResponseDto(null)).isNull();
    }
}