package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductBrandDtoMapperTest {

    private ProductBrandDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductBrandDtoMapper();
    }

    @Test
    void toModel_shouldMapFieldsCorrectly() {
        ProductBrandCreateDto dto = new ProductBrandCreateDto();
        dto.setName("Nike");
        dto.setDescription("Marca deportiva");
        dto.setEnabled(true);
        dto.setCreatedBy("admin");
        dto.setUpdatedBy("admin");

        ProductBrandModel model = mapper.toModel(dto);

        assertNotNull(model);
        assertEquals(dto.getName(), model.getName());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getEnabled(), model.getEnabled());
        assertEquals(dto.getCreatedBy(), model.getCreatedBy());
        assertEquals(dto.getUpdatedBy(), model.getUpdatedBy());
        assertNotNull(model.getCreatedAt());
        assertNull(model.getUpdatedAt());
    }

    @Test
    void toModel_shouldReturnNull_whenDtoIsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toResponseDto_shouldMapFieldsCorrectly() {
        ProductBrandModel model = ProductBrandModel.builder()
                .id(UUID.randomUUID())
                .name("Adidas")
                .description("Ropa deportiva")
                .enabled(true)
                .createdBy("user1")
                .updatedBy("user2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductBrandResponseDto dto = ProductBrandDtoMapper.toResponseDto(model);

        assertNotNull(dto);
        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getName(), dto.getName());
        assertEquals(model.getDescription(), dto.getDescription());
        assertEquals(model.getEnabled(), dto.getEnabled());
        assertEquals(model.getCreatedBy(), dto.getCreatedBy());
        assertEquals(model.getUpdatedBy(), dto.getUpdatedBy());
        assertEquals(model.getCreatedAt(), dto.getCreatedAt());
        assertEquals(model.getUpdatedAt(), dto.getUpdatedAt());
    }

    @Test
    void toResponseDto_shouldReturnNull_whenModelIsNull() {
        assertNull(ProductBrandDtoMapper.toResponseDto(null));
    }

    @Test
    void toCreateDto_shouldMapFieldsCorrectly() {
        ProductBrandModel model = ProductBrandModel.builder()
                .name("Puma")
                .description("Calzado")
                .build();

        ProductBrandCreateDto dto = mapper.toCreateDto(model);

        assertNotNull(dto);
        assertEquals(model.getName(), dto.getName());
        assertEquals(model.getDescription(), dto.getDescription());
    }

    @Test
    void toCreateDto_shouldReturnNull_whenModelIsNull() {
        assertNull(mapper.toCreateDto(null));
    }

    @Test
    void toResponseDtoList_shouldMapListCorrectly() {
        ProductBrandModel model1 = ProductBrandModel.builder()
                .id(UUID.randomUUID())
                .name("Apple")
                .description("Tecnología")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .build();

        ProductBrandModel model2 = ProductBrandModel.builder()
                .id(UUID.randomUUID())
                .name("Samsung")
                .description("Electrónica")
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .createdBy("admin")
                .build();

        List<ProductBrandResponseDto> dtoList = mapper.toResponseDtoList(List.of(model1, model2));

        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("Apple", dtoList.get(0).getName());
        assertEquals("Samsung", dtoList.get(1).getName());
    }

    @Test
    void toResponseDtoList_shouldReturnNull_whenListIsNull() {
        assertNull(mapper.toResponseDtoList(null));
    }
}