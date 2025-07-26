package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRequestMapperTest {

    private final ProductRequestMapper mapper = new ProductRequestMapper();

    @Test
    void testToModel_FromCreateDto() {
        ProductCreateDto dto = ProductCreateDto.builder()
                .code("P001")
                .name("Taco")
                .description("Taco al pastor")
                .categoryId(UUID.randomUUID())
                .unitCode("KG")
                .brandId(UUID.randomUUID())
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(BigDecimal.valueOf(25.99))
                .enabled(true)
                .createdBy("admin")
                .build();

        ProductModel model = mapper.toModel(dto);

        assertThat(model).isNotNull();
        assertThat(model.getCode()).isEqualTo("P001");
        assertThat(model.getName()).isEqualTo("Taco");
        assertThat(model.getDescription()).isEqualTo("Taco al pastor");
        assertThat(model.getCategoryId()).isEqualTo(dto.getCategoryId());
        assertThat(model.getUnitCode()).isEqualTo("KG");
        assertThat(model.getBrandId()).isEqualTo(dto.getBrandId());
        assertThat(model.getMinimumStock()).isEqualTo(10);
        assertThat(model.getCurrentStock()).isEqualTo(50);
        assertThat(model.getUnitPrice()).isEqualByComparingTo("25.99");
        assertThat(model.getEnabled()).isTrue();
        assertThat(model.getCreatedBy()).isEqualTo("admin");
        assertThat(model.getId()).isNull(); // no se asigna en CreateDto
    }

    @Test
    void testToModel_FromUpdateDto() {
        UUID id = UUID.randomUUID();
        ProductUpdateDto dto = ProductUpdateDto.builder()
                .id(id)
                .code("P002")
                .name("Torta")
                .description("Torta de milanesa")
                .categoryId(UUID.randomUUID())
                .unitCode("UNI")
                .brandId(UUID.randomUUID())
                .minimumStock(5)
                .currentStock(20)
                .unitPrice(BigDecimal.valueOf(35.50))
                .enabled(false)
                .createdBy("system")
                .updatedBy("manager")
                .build();

        ProductModel model = mapper.toModel(dto);

        assertThat(model).isNotNull();
        assertThat(model.getId()).isEqualTo(id);
        assertThat(model.getCode()).isEqualTo("P002");
        assertThat(model.getName()).isEqualTo("Torta");
        assertThat(model.getDescription()).isEqualTo("Torta de milanesa");
        assertThat(model.getCategoryId()).isEqualTo(dto.getCategoryId());
        assertThat(model.getUnitCode()).isEqualTo("UNI");
        assertThat(model.getBrandId()).isEqualTo(dto.getBrandId());
        assertThat(model.getMinimumStock()).isEqualTo(5);
        assertThat(model.getCurrentStock()).isEqualTo(20);
        assertThat(model.getUnitPrice()).isEqualByComparingTo("35.50");
        assertThat(model.getEnabled()).isFalse();
        assertThat(model.getCreatedBy()).isEqualTo("system");
        assertThat(model.getUpdatedBy()).isEqualTo("manager");
    }

    @Test
    void testToModel_NullCreateDto() {
        assertThat(mapper.toModel((ProductCreateDto) null)).isNull();
    }

    @Test
    void testToModel_NullUpdateDto() {
        assertThat(mapper.toModel((ProductUpdateDto) null)).isNull();
    }
}