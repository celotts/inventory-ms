package com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductResponseMapperTest {

    private ProductResponseMapper mapper;
    private ProductModel productModel;

    @BeforeEach
    void setUp() {
        mapper = new ProductResponseMapper();

        productModel = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("P001")
                .name("Test Product")
                .description("Test Description")
                .categoryId(UUID.randomUUID())
                .unitCode("UNIT")
                .brandId(UUID.randomUUID())
                .minimumStock(10)
                .currentStock(5)
                .unitPrice(new BigDecimal("19.99"))
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("creator")
                .updatedBy("editor")
                .build();
    }

    @Test
    void toDto_shouldReturnDto_whenModelIsValid() {
        ProductResponseDto dto = mapper.toDto(productModel);

        assertNotNull(dto);
        assertEquals(productModel.getId(), dto.getId());
        assertEquals(productModel.getCode(), dto.getCode());
        assertEquals(productModel.getCategoryId(), dto.getCategoryId());
        assertEquals(productModel.getBrandId(), dto.getBrandId());
        assertEquals(productModel.lowStock(), dto.getLowStock());
    }

    @Test
    void toDto_shouldReturnNull_whenModelIsNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void toResponseDto_shouldDelegateTo_toDto() {
        ProductResponseDto dto = mapper.toResponseDto(productModel);
        assertNotNull(dto);
        assertEquals(productModel.getCode(), dto.getCode());
    }

    @Test
    void toDtoWithCategoryName_shouldReturnDtoWithCategoryName() {
        String categoryName = "Snacks";
        ProductResponseDto dto = mapper.toDtoWithCategoryName(productModel, categoryName);

        assertNotNull(dto);
        assertEquals(categoryName, dto.getCategoryName());
        assertEquals(productModel.getCode(), dto.getCode());
    }

    @Test
    void toDtoWithCategoryName_shouldReturnNull_whenModelIsNull() {
        assertNull(mapper.toDtoWithCategoryName(null, "test"));
    }

    @Test
    void toDtoList_shouldReturnMappedList() {
        List<ProductResponseDto> list = mapper.toDtoList(List.of(productModel));
        assertEquals(1, list.size());
        assertEquals(productModel.getId(), list.get(0).getId());
    }

    @Test
    void toDtoList_shouldReturnEmpty_whenInputIsNull() {
        List<ProductResponseDto> list = mapper.toDtoList(null);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void toDtoList_shouldReturnEmpty_whenInputIsEmpty() {
        List<ProductResponseDto> list = mapper.toDtoList(List.of());
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    void toResponseDtoList_shouldDelegateTo_toDtoList() {
        List<ProductResponseDto> list = mapper.toResponseDtoList(List.of(productModel));
        assertEquals(1, list.size());
        assertEquals(productModel.getCode(), list.get(0).getCode());
    }

    @Test
    void toDtoPage_shouldReturnDtoPage() {
        Page<ProductModel> page = new PageImpl<>(List.of(productModel), PageRequest.of(0, 1), 1);
        Page<ProductResponseDto> dtoPage = mapper.toDtoPage(page);

        assertEquals(1, dtoPage.getTotalElements());
        assertEquals(productModel.getId(), dtoPage.getContent().get(0).getId());
    }

    @Test
    void toDtoPage_shouldReturnEmpty_whenInputIsNull() {
        Page<ProductResponseDto> result = mapper.toDtoPage(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoPage_shouldReturnEmpty_whenPageIsEmpty() {
        Page<ProductModel> emptyPage = Page.empty(PageRequest.of(0, 1));
        Page<ProductResponseDto> result = mapper.toDtoPage(emptyPage);

        assertTrue(result.isEmpty());
    }
}