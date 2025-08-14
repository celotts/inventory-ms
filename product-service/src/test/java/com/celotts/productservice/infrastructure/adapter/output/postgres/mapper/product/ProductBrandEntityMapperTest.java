package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productserviceOld.domain.model.ProductBrandModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.product.ProductBrandEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductBrandEntityMapperTest {

    private ProductBrandEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductBrandEntityMapper();
    }

    @Test
    void testToEntity() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandModel model = ProductBrandModel.builder()
                .id(id)
                .name("Marca X")
                .description("Marca de prueba")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("editor")
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductBrandEntity entity = mapper.toEntity(model);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Marca X");
        assertThat(entity.getDescription()).isEqualTo("Marca de prueba");
        assertThat(entity.getEnabled()).isTrue();
        assertThat(entity.getCreatedBy()).isEqualTo("admin");
        assertThat(entity.getUpdatedBy()).isEqualTo("editor");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testToModel() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandEntity entity = ProductBrandEntity.builder()
                .id(id)
                .name("Marca Y")
                .description("Descripción Y")
                .enabled(false)
                .createdBy("user1")
                .updatedBy("user2")
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductBrandModel model = mapper.toModel(entity);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getName()).isEqualTo("Marca Y");
        assertThat(model.getDescription()).isEqualTo("Descripción Y");
        assertThat(model.getEnabled()).isFalse();
        assertThat(model.getCreatedBy()).isEqualTo("user1");
        assertThat(model.getUpdatedBy()).isEqualTo("user2");
        assertThat(model.getCreatedAt()).isEqualTo(now);
        assertThat(model.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testToResponseDto() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductBrandEntity entity = ProductBrandEntity.builder()
                .id(id)
                .name("Marca DTO")
                .description("Descripción DTO")
                .enabled(true)
                .createdBy("userDTO")
                .updatedBy("editorDTO")
                .createdAt(now)
                .updatedAt(now)
                .build();

        ProductBrandResponseDto dto = mapper.toResponseDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo("Marca DTO");
        assertThat(dto.getDescription()).isEqualTo("Descripción DTO");
        assertThat(dto.getEnabled()).isTrue();
        assertThat(dto.getCreatedBy()).isEqualTo("userDTO");
        assertThat(dto.getUpdatedBy()).isEqualTo("editorDTO");
        assertThat(dto.getCreatedAt()).isEqualTo(now);
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testNullInputs() {
        assertThat(mapper.toEntity(null)).isNull();
        assertThat(mapper.toModel(null)).isNull();
        assertThat(mapper.toResponseDto(null)).isNull();
    }
}