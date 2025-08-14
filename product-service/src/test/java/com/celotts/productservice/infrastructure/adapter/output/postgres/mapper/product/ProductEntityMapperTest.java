package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productserviceOld.domain.model.ProductModel;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.product.ProductEntityMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductEntityMapperTest {

    @Test
    void testToEntity() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductModel model = ProductModel.builder()
                .id(id)
                .code("P001")
                .name("Taco")
                .description("Taco al pastor")
                .categoryId(categoryId)
                .unitCode("u")
                .brandId(brandId)
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(new BigDecimal("29.99"))
                .enabled(true)
                .createdAt(now.minusDays(2))
                .updatedAt(now)
                .createdBy("carlos")
                .updatedBy("admin")
                .build();

        ProductEntity entity = ProductEntityMapper.toEntity(model);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getCode()).isEqualTo("P001");
        assertThat(entity.getName()).isEqualTo("Taco");
        assertThat(entity.getDescription()).isEqualTo("Taco al pastor");
        assertThat(entity.getCategoryId()).isEqualTo(categoryId);
        assertThat(entity.getUnitCode()).isEqualTo("u");
        assertThat(entity.getBrandId()).isEqualTo(brandId);
        assertThat(entity.getMinimumStock()).isEqualTo(10);
        assertThat(entity.getCurrentStock()).isEqualTo(50);
        assertThat(entity.getUnitPrice()).isEqualByComparingTo("29.99");
        assertThat(entity.getEnabled()).isTrue();
        assertThat(entity.getCreatedAt()).isEqualTo(now.minusDays(2));
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
        assertThat(entity.getCreatedBy()).isEqualTo("carlos");
        assertThat(entity.getUpdatedBy()).isEqualTo("admin");
    }

    @Test
    void testToModel() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductEntity entity = ProductEntity.builder()
                .id(id)
                .code("P002")
                .name("Burrito")
                .description("Burrito de carne")
                .categoryId(categoryId)
                .unitCode("kg")
                .brandId(brandId)
                .minimumStock(5)
                .currentStock(20)
                .unitPrice(new BigDecimal("39.99"))
                .enabled(false)
                .createdAt(now.minusDays(5))
                .updatedAt(now)
                .createdBy("juan")
                .updatedBy("maria")
                .build();

        ProductModel model = new ProductEntityMapper().toModel(entity);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getCode()).isEqualTo("P002");
        assertThat(model.getName()).isEqualTo("Burrito");
        assertThat(model.getDescription()).isEqualTo("Burrito de carne");
        assertThat(model.getCategoryId()).isEqualTo(categoryId);
        assertThat(model.getUnitCode()).isEqualTo("kg");
        assertThat(model.getBrandId()).isEqualTo(brandId);
        assertThat(model.getMinimumStock()).isEqualTo(5);
        assertThat(model.getCurrentStock()).isEqualTo(20);
        assertThat(model.getUnitPrice()).isEqualByComparingTo("39.99");
        assertThat(model.getEnabled()).isFalse();
        assertThat(model.getCreatedAt()).isEqualTo(now.minusDays(5));
        assertThat(model.getUpdatedAt()).isEqualTo(now);
        assertThat(model.getCreatedBy()).isEqualTo("juan");
        assertThat(model.getUpdatedBy()).isEqualTo("maria");
    }

    @Test
    void testToEntity_Null() {
        ProductEntity entity = ProductEntityMapper.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    void testToModel_Null() {
        ProductModel model = new ProductEntityMapper().toModel(null);
        assertThat(model).isNull();
    }
}