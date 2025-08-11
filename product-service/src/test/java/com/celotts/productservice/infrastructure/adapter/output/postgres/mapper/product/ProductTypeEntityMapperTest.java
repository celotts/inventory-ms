package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductTypeModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeEntityMapperTest {

    private final ProductTypeEntityMapper mapper = new ProductTypeEntityMapper();

    @Test
    void testToEntity() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductTypeModel model = ProductTypeModel.builder()
                .id(id)
                .code("TYPE-A")
                .name("Bebida")
                .descpiption("Bebidas frías")
                .enabled(true)
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .createdBy("system")
                .updateBy("admin")
                .build();

        ProductTypeEntity entity = mapper.toEntity(model);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getCode()).isEqualTo("TYPE-A");
        assertThat(entity.getName()).isEqualTo("Bebida");
        assertThat(entity.getDescription()).isEqualTo("Bebidas frías");
        assertThat(entity.getEnabled()).isTrue();
        assertThat(entity.getCreatedAt()).isEqualTo(now.minusDays(1));
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
        assertThat(entity.getCreatedBy()).isEqualTo("system");
        assertThat(entity.getUpdatedBy()).isEqualTo("admin");
    }

    @Test
    void testToModel() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductTypeEntity entity = ProductTypeEntity.builder()
                .id(id)
                .code("TYPE-B")
                .name("Postre")
                .description("Postres dulces")
                .enabled(false)
                .createdAt(now.minusDays(3))
                .updatedAt(now)
                .createdBy("auto")
                .updatedBy("mod-user")
                .build();

        ProductTypeModel model = mapper.toModel(entity);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getCode()).isEqualTo("TYPE-B");
        assertThat(model.getName()).isEqualTo("Postre");
        assertThat(model.getDescpiption()).isEqualTo("Postres dulces");
        assertThat(model.getEnabled()).isFalse();
        assertThat(model.getCreatedAt()).isEqualTo(now.minusDays(3));
        assertThat(model.getUpdatedAt()).isEqualTo(now);
        assertThat(model.getCreatedBy()).isEqualTo("auto");
        assertThat(model.getUpdateBy()).isEqualTo("mod-user");
    }

    @Test
    void testToEntity_NullInput() {
        ProductTypeEntity entity = mapper.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    void testToModel_NullInput() {
        ProductTypeModel model = mapper.toModel(null);
        assertThat(model).isNull();
    }
}