package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTag;

import com.celotts.productservice.domain.model.ProductTagModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagUpdateDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTagRequestMapperTest {

    private final ProductTagRequestMapper mapper = new ProductTagRequestMapper();

    @Test
    void toModel_fromCreateDto_mapsAll_andDefaultsEnabledTrueWhenNull() {
        ProductTagCreateDto dto = ProductTagCreateDto.builder()
                .name("Tech")
                .description("High tech stuff")
                .enabled(null) // debe default a TRUE
                .createdBy("alice")
                .build();

        ProductTagModel m = mapper.toModel(dto);

        assertThat(m).isNotNull();
        assertThat(m.getName()).isEqualTo("Tech");
        assertThat(m.getDescription()).isEqualTo("High tech stuff");
        assertThat(m.getEnabled()).isTrue();
        assertThat(m.getCreatedBy()).isEqualTo("alice");
        // campos no seteados deben quedar null
        assertThat(m.getUpdatedBy()).isNull();
    }

    @Test
    void toModel_fromCreateDto_respectsExplicitEnabledFalse() {
        ProductTagCreateDto dto = ProductTagCreateDto.builder()
                .name("Deprecated")
                .description("no longer used")
                .enabled(false)
                .createdBy("bob")
                .build();

        ProductTagModel m = mapper.toModel(dto);

        assertThat(m.getEnabled()).isFalse();
    }

    @Test
    void toModel_fromUpdateDto_mapsAll() {
        ProductTagUpdateDto dto = ProductTagUpdateDto.builder()
                .name("Updated name")
                .description("Updated desc")
                .enabled(true)
                .updatedBy("charlie")
                .build();

        ProductTagModel m = mapper.toModel(dto);

        assertThat(m).isNotNull();
        assertThat(m.getName()).isEqualTo("Updated name");
        assertThat(m.getDescription()).isEqualTo("Updated desc");
        assertThat(m.getEnabled()).isTrue();
        assertThat(m.getUpdatedBy()).isEqualTo("charlie");
        // createdBy no lo setea el update mapper
        assertThat(m.getCreatedBy()).isNull();
    }

    @Test
    void toResponse_mapsAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        ProductTagModel model = ProductTagModel.builder()
                .id(id)
                .name("Gaming")
                .description("Gaming related")
                .enabled(true)
                .createdBy("diana")
                .updatedBy("edgar")
                .createdAt(now.minusDays(1))
                .updatedAt(now)
                .build();

        ProductTagResponseDto dto = mapper.toResponse(model);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getName()).isEqualTo("Gaming");
        assertThat(dto.getDescription()).isEqualTo("Gaming related");
        assertThat(dto.getEnabled()).isTrue();
        assertThat(dto.getCreatedBy()).isEqualTo("diana");
        assertThat(dto.getUpdatedBy()).isEqualTo("edgar");
        assertThat(dto.getCreatedAt()).isEqualTo(now.minusDays(1));
        assertThat(dto.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void nullInputs_returnNulls() {
        assertThat(mapper.toModel((ProductTagCreateDto) null)).isNull();
        assertThat(mapper.toModel((ProductTagUpdateDto) null)).isNull();
        assertThat(mapper.toResponse(null)).isNull();
    }
}