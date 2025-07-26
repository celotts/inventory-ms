package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductUnitEntityMapperTest {

    private final ProductUnitEntityMapper mapper = new ProductUnitEntityMapper();

    @Test
    void testToEntity() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductUnitModel model = ProductUnitModel.builder()
                .id(id)
                .code("GR")
                .name("Gramos")
                .description("Unidad de masa")
                .symbol("g")
                .enabled(true)
                .createdAt(now.minusDays(2))
                .updatedAt(now)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        ProductUnitEntity entity = mapper.toEntity(model);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getCode()).isEqualTo("GR");
        assertThat(entity.getName()).isEqualTo("Gramos");
        assertThat(entity.getDescription()).isEqualTo("Unidad de masa");
        assertThat(entity.getSymbol()).isEqualTo("g");
        assertThat(entity.getEnabled()).isTrue();
        assertThat(entity.getCreatedAt()).isEqualTo(now.minusDays(2));
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
        assertThat(entity.getCreatedBy()).isEqualTo("admin");
        assertThat(entity.getUpdatedBy()).isEqualTo("admin");
    }

    @Test
    void testToModel() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductUnitEntity entity = ProductUnitEntity.builder()
                .id(id)
                .code("KG")
                .name("Kilogramo")
                .description("Unidad de peso")
                .symbol("kg")
                .enabled(false)
                .createdAt(now.minusDays(5))
                .updatedAt(now)
                .createdBy("system")
                .updatedBy("mod")
                .build();

        ProductUnitModel model = mapper.toModel(entity);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getCode()).isEqualTo("KG");
        assertThat(model.getName()).isEqualTo("Kilogramo");
        assertThat(model.getDescription()).isEqualTo("Unidad de peso");
        assertThat(model.getSymbol()).isEqualTo("kg");
        assertThat(model.getEnabled()).isFalse();
        assertThat(model.getCreatedAt()).isEqualTo(now.minusDays(5));
        assertThat(model.getUpdatedAt()).isEqualTo(now);
        assertThat(model.getCreatedBy()).isEqualTo("system");
        assertThat(model.getUpdatedBy()).isEqualTo("mod");
    }

    @Test
    void testToEntity_NullInput() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void testToModel_NullInput() {
        assertThat(mapper.toModel(null)).isNull();
    }
}