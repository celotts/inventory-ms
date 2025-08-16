package com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product;

import com.celotts.productserviceOld.domain.model.ProductModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductCreate;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.product.ProductRequestMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRequestMapperTest {

    private final ProductRequestMapper mapper = new ProductRequestMapper();

    // --------------------- toModel(ProductCreate) ---------------------

    @Test
    void toModel_fromCreate_shouldMapAllFields() {
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductCreate dto = ProductCreate.builder()
                .code("P-001")
                .name("Prod A")
                .description("Desc A")
                .categoryId(categoryId)
                .unitCode("UN")
                .brandId(brandId)
                .minimumStock(5)
                .currentStock(10)
                .unitPrice(new BigDecimal("12.34"))
                .enabled(true)
                .createdBy("creator")
                .build();

        ProductModel m = mapper.toModel(dto);

        assertThat(m).isNotNull();
        assertThat(m.getId()).isNull();
        assertThat(m.getCode()).isEqualTo("P-001");
        assertThat(m.getName()).isEqualTo("Prod A");
        assertThat(m.getDescription()).isEqualTo("Desc A");
        assertThat(m.getCategoryId()).isEqualTo(categoryId);
        assertThat(m.getUnitCode()).isEqualTo("UN");
        assertThat(m.getBrandId()).isEqualTo(brandId);
        assertThat(m.getMinimumStock()).isEqualTo(5);
        assertThat(m.getCurrentStock()).isEqualTo(10);
        assertThat(m.getUnitPrice()).isEqualByComparingTo("12.34");
        assertThat(m.getEnabled()).isTrue();
        assertThat(m.getCreatedBy()).isEqualTo("creator");
        assertThat(m.getUpdatedBy()).isNull();
    }

    @Test
    void toModel_fromCreate_shouldReturnNull_whenDtoIsNull() {
        assertThat(mapper.toModel((ProductCreate) null)).isNull();
    }

    // --------------------- toModel(ProductUpdateDto) ---------------------

    @Test
    void toModel_fromUpdate_shouldMapAllFields() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .id(id)
                .code("P-002")
                .name("Prod B")
                .description("Desc B")
                .categoryId(categoryId)
                .unitCode("KG")
                .brandId(brandId)
                .minimumStock(1)
                .currentStock(2)
                .unitPrice(new BigDecimal("99.99"))
                .enabled(false)
                .createdBy("creator2")
                .updatedBy("updater")
                .build();

        ProductModel m = mapper.toModel(dto);

        assertThat(m).isNotNull();
        assertThat(m.getId()).isEqualTo(id);
        assertThat(m.getCode()).isEqualTo("P-002");
        assertThat(m.getName()).isEqualTo("Prod B");
        assertThat(m.getDescription()).isEqualTo("Desc B");
        assertThat(m.getCategoryId()).isEqualTo(categoryId);
        assertThat(m.getUnitCode()).isEqualTo("KG");
        assertThat(m.getBrandId()).isEqualTo(brandId);
        assertThat(m.getMinimumStock()).isEqualTo(1);
        assertThat(m.getCurrentStock()).isEqualTo(2);
        assertThat(m.getUnitPrice()).isEqualByComparingTo("99.99");
        assertThat(m.getEnabled()).isFalse();
        assertThat(m.getCreatedBy()).isEqualTo("creator2");
        assertThat(m.getUpdatedBy()).isEqualTo("updater");
    }

    @Test
    void toModel_fromUpdate_shouldReturnNull_whenDtoIsNull() {
        assertThat(mapper.toModel((ProductUpdateDto) null)).isNull();
    }

    // --------------------- updateModelFromDto ---------------------

    @Test
    void updateModelFromDto_shouldBeNullSafe() {
        ProductModel existing = ProductModel.builder().build();
        mapper.updateModelFromDto(existing, null);                          // dto null
        mapper.updateModelFromDto(null, ProductUpdateDto.builder().build()); // existing null
    }

    @Test
    void updateModelFromDto_shouldIgnoreAllNullFields_noChanges() {
        // existing con datos
        ProductModel existing = baseExisting();

        // dto sin valores -> todas las condiciones false
        ProductUpdateDto empty = ProductUpdateDto.builder().build();

        // snapshot previo para comparar
        ProductModel before = copy(existing);

        mapper.updateModelFromDto(existing, empty);

        // no debió cambiar nada salvo updatedAt (que sí se setea siempre)
        assertThat(existing)
                .usingRecursiveComparison()
                .ignoringFields("updatedAt")
                .isEqualTo(before);

        assertThat(existing.getUpdatedAt()).isNotNull();
    }

    @Test
    void updateModelFromDto_shouldUpdateOnlyNonNullFields_andSetUpdatedAt() {
        ProductModel existing = baseExisting();

        UUID newBrand = UUID.randomUUID();
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .code("NEW-CODE")
                .name("New name")
                .description(null)                 // no tocar
                .unitCode("KG")
                .brandId(newBrand)
                .categoryId(null)                  // no tocar
                .minimumStock(9)
                .currentStock(null)                // no tocar
                .unitPrice(new BigDecimal("5.00"))
                .enabled(false)
                .updatedBy("updater")
                .build();

        mapper.updateModelFromDto(existing, dto);

        // campos actualizados (ramas true)
        assertThat(existing.getCode()).isEqualTo("NEW-CODE");
        assertThat(existing.getName()).isEqualTo("New name");
        assertThat(existing.getUnitCode()).isEqualTo("KG");
        assertThat(existing.getBrandId()).isEqualTo(newBrand);
        assertThat(existing.getMinimumStock()).isEqualTo(9);
        assertThat(existing.getUnitPrice()).isEqualByComparingTo("5.00");
        assertThat(existing.getEnabled()).isFalse();
        assertThat(existing.getUpdatedBy()).isEqualTo("updater");

        // campos no tocados (ramas false)
        assertThat(existing.getDescription()).isEqualTo("Initial desc");
        assertThat(existing.getCategoryId()).isNotNull();
        assertThat(existing.getCurrentStock()).isEqualTo(7);

        // updatedAt se setea al final
        assertThat(existing.getUpdatedAt()).isNotNull();
        assertThat(existing.getUpdatedAt()).isAfter(before);
    }

    @Test
    void updateModelFromDto_shouldUpdateAllFields_whenAllNonNull() {
        ProductModel existing = baseExisting();
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);

        UUID idCat = UUID.randomUUID();
        UUID idBrand = UUID.randomUUID();

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .code("C2")
                .name("Name2")
                .description("Desc2")
                .unitCode("LT")
                .brandId(idBrand)
                .categoryId(idCat)
                .minimumStock(50)
                .currentStock(75)
                .unitPrice(new BigDecimal("2.50"))
                .enabled(true)
                .updatedBy("user2")
                .build();

        mapper.updateModelFromDto(existing, dto);

        assertThat(existing.getCode()).isEqualTo("C2");
        assertThat(existing.getName()).isEqualTo("Name2");
        assertThat(existing.getDescription()).isEqualTo("Desc2");
        assertThat(existing.getUnitCode()).isEqualTo("LT");
        assertThat(existing.getBrandId()).isEqualTo(idBrand);
        assertThat(existing.getCategoryId()).isEqualTo(idCat);
        assertThat(existing.getMinimumStock()).isEqualTo(50);
        assertThat(existing.getCurrentStock()).isEqualTo(75);
        assertThat(existing.getUnitPrice()).isEqualByComparingTo("2.50");
        assertThat(existing.getEnabled()).isTrue();
        assertThat(existing.getUpdatedBy()).isEqualTo("user2");
        assertThat(existing.getUpdatedAt()).isAfter(before);
    }

    // -------- helpers --------

    private ProductModel baseExisting() {
        return ProductModel.builder()
                .id(UUID.randomUUID())
                .code("INIT")
                .name("Initial")
                .description("Initial desc")
                .categoryId(UUID.randomUUID())
                .unitCode("UN")
                .brandId(UUID.randomUUID())
                .minimumStock(3)
                .currentStock(7)
                .unitPrice(new BigDecimal("1.23"))
                .enabled(true)
                .createdBy("creator")
                .updatedBy(null)
                .updatedAt(null)
                .build();
    }

    private ProductModel copy(ProductModel m) {
        return ProductModel.builder()
                .id(m.getId())
                .code(m.getCode())
                .name(m.getName())
                .description(m.getDescription())
                .categoryId(m.getCategoryId())
                .unitCode(m.getUnitCode())
                .brandId(m.getBrandId())
                .minimumStock(m.getMinimumStock())
                .currentStock(m.getCurrentStock())
                .unitPrice(m.getUnitPrice())
                .enabled(m.getEnabled())
                .createdBy(m.getCreatedBy())
                .updatedBy(m.getUpdatedBy())
                .updatedAt(m.getUpdatedAt())
                .build();
    }
}