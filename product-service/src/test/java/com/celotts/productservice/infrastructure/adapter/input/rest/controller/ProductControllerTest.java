package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.product.port.usecase.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductUseCase productUseCase;

    @Mock
    private ProductResponseMapper responseMapper;

    @Mock
    private ProductRequestMapper productRequestMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getInactiveProducts_shouldReturn200WhenProductsExist() throws Exception {
        ProductModel mockProduct = new ProductModel();
        com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto mockDto =
                com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto.builder()
                        .id(UUID.randomUUID())
                        .code("P001")
                        .name("Mock Product")
                        .description("Mock Description")
                        .enabled(false)
                        .currentStock(0)
                        .minimumStock(5)
                        .unitPrice(java.math.BigDecimal.valueOf(99.99))
                        .createdAt(java.time.LocalDateTime.now())
                        .updatedAt(java.time.LocalDateTime.now())
                        .build();

        Mockito.when(productUseCase.getInactiveProducts()).thenReturn(List.of(mockProduct));
        Mockito.when(responseMapper.toResponseDtoList(List.of(mockProduct))).thenReturn(List.of(mockDto));

        mockMvc.perform(get("/api/v1/products/inactive"))
                .andExpect(status().isOk());
    }

    @Test
    void getInactiveProducts_shouldReturn204WhenNoProductsExist() throws Exception {
        Mockito.when(productUseCase.getInactiveProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/products/inactive"))
                .andExpect(status().isNoContent());
    }

    /*@Test
    void getProductsByCategory_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.getProductsByCategory(Mockito.any()))
                .thenReturn(List.of(new ProductModel()));

        mockMvc.perform(get("/api/v1/products/category/" + UUID.randomUUID()))
                .andExpect(status().isOk());
    } */

    @Test
    void getProductsByCategory_shouldReturnOk() throws Exception {
        UUID categoryId = UUID.randomUUID();
        ProductModel product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("CODE1")
                .name("Product 1")
                .enabled(true)
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .enabled(true)
                .build();

        Mockito.when(productUseCase.getProductsByCategory(categoryId)).thenReturn(List.of(product));
        Mockito.when(responseMapper.toResponseDtoList(List.of(product))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/products/category/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("CODE1"))
                .andExpect(jsonPath("$[0].enabled").value(true));
    }

    @Test
    void getLowStockByCategory_shouldReturnOk() throws Exception {
        UUID categoryId = UUID.randomUUID();
        ProductModel product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("LOW1")
                .currentStock(2)
                .minimumStock(5)
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .currentStock(2)
                .minimumStock(5)
                .build();

        Mockito.when(productUseCase.getLowStockByCategory(categoryId)).thenReturn(List.of(product));
        Mockito.when(responseMapper.toResponseDtoList(List.of(product))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/products/category/" + categoryId + "/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("LOW1"))
                .andExpect(jsonPath("$[0].currentStock").value(2))
                .andExpect(jsonPath("$[0].minimumStock").value(5));
    }

    @Test
    void getLowStockProducts_shouldReturnOk() throws Exception {
        ProductModel product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("LOW2")
                .currentStock(1)
                .minimumStock(4)
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .currentStock(1)
                .minimumStock(4)
                .build();

        Mockito.when(productUseCase.getLowStockProducts()).thenReturn(List.of(product));
        Mockito.when(responseMapper.toResponseDtoList(List.of(product))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/products/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("LOW2"))
                .andExpect(jsonPath("$[0].currentStock").value(1));
    }

    @Test
    void getProductsByBrand_shouldReturnOk() throws Exception {
        UUID brandId = UUID.randomUUID();
        ProductModel product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("BRAND1")
                .name("Brand Product")
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(product.getId())
                .code("BRAND1")
                .name("Brand Product")
                .build();

        Mockito.when(productUseCase.getProductsByBrand(brandId)).thenReturn(List.of(product));
        Mockito.when(responseMapper.toResponseDtoList(List.of(product))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/products/brand/" + brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("BRAND1"))
                .andExpect(jsonPath("$[0].name").value("Brand Product"));
    }

    @Test
    void countProducts_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.countProducts()).thenReturn(5L);

        mockMvc.perform(get("/api/v1/products/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void countActiveProducts_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.countActiveProducts()).thenReturn(3L);

        mockMvc.perform(get("/api/v1/products/count/active"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void validateUnitCode_shouldReturnValid() throws Exception {
        Mockito.when(productUseCase.validateUnitCode("UNIT1"))
                .thenReturn(Optional.of("Unit 1"));

        mockMvc.perform(get("/api/v1/products/validate-unit/UNIT1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.unitName").value("Unit 1"));
    }

    @Test
    void updateStock_shouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        ProductModel product = ProductModel.builder().id(id).currentStock(10).build();

        Mockito.when(productUseCase.updateStock(Mockito.eq(id), Mockito.anyInt()))
                .thenReturn(product);

        mockMvc.perform(patch("/api/v1/products/" + id + "/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("15"))
                .andExpect(status().isOk());
    }

    @Test
    void updateStock_shouldReturnBadRequest_whenInvalidStockValue() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(patch("/api/v1/products/" + id + "/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"invalid\"")) // sin comillas, causa error de deserialización
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProductByCode_shouldReturnOk() throws Exception {
        ProductModel product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("CODE123")
                .name("Product X")
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(product.getId())
                .code("CODE123")
                .name("Product X")
                .build();

        Mockito.when(productUseCase.getProductByCode("CODE123")).thenReturn(product);
        Mockito.when(responseMapper.toResponseDto(product)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/products/code/CODE123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("CODE123"))
                .andExpect(jsonPath("$.name").value("Product X"));
    }
}