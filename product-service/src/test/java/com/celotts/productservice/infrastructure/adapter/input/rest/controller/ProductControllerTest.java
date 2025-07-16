package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.product.port.usecase.ProductUseCase;
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
    void getInactiveProducts_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.getInactiveProducts()).thenReturn(List.of(new ProductModel()));

        mockMvc.perform(get("/api/v1/products/inactive"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByCategory_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.getProductsByCategory(Mockito.any()))
                .thenReturn(List.of(new ProductModel()));

        mockMvc.perform(get("/api/v1/products/category/" + UUID.randomUUID()))
                .andExpect(status().isOk());
    }

    @Test
    void getLowStockByCategory_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.getLowStockByCategory(Mockito.any()))
                .thenReturn(List.of(new ProductModel()));

        mockMvc.perform(get("/api/v1/products/category/" + UUID.randomUUID() + "/low-stock"))
                .andExpect(status().isOk());
    }

    @Test
    void getLowStockProducts_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.getLowStockProducts())
                .thenReturn(List.of(new ProductModel()));

        mockMvc.perform(get("/api/v1/products/low-stock"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductsByBrand_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.getProductsByBrand(Mockito.any()))
                .thenReturn(List.of(new ProductModel()));

        mockMvc.perform(get("/api/v1/products/brand/" + UUID.randomUUID()))
                .andExpect(status().isOk());
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
    void getProductByCode_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.getProductByCode("CODE123"))
                .thenReturn(ProductModel.builder().code("CODE123").build());

        mockMvc.perform(get("/api/v1/products/code/CODE123"))
                .andExpect(status().isOk());
    }
}