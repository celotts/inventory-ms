package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.product.root.input.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.advice.ProductControllerAdvice;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest {

    private MockMvc mockMvc;
    private ProductUseCase productUseCase;
    private ProductResponseMapper responseMapper;

    @BeforeEach
    void setup() {
        productUseCase = Mockito.mock(ProductUseCase.class);
        responseMapper = Mockito.mock(ProductResponseMapper.class);
        ProductRequestMapper productRequestMapper = Mockito.mock(ProductRequestMapper.class);

        ProductController productController = new ProductController(
                productUseCase,
                responseMapper,
                productRequestMapper
        );

        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ProductControllerAdvice())
                .build();
    }

    @Test
    @DisplayName("PATCH /api/v1/products/{id}/enable debe responder 200 y body correcto")
    void shouldEnableProduct() throws Exception {
        UUID id = UUID.randomUUID();

        ProductModel mockProduct = ProductModel.builder()
                .id(id)
                .code("CODE123")
                .enabled(true)
                .build();

        ProductResponseDTO mockResponseDto = ProductResponseDTO.builder()
                .id(id)
                .code("CODE123")
                .enabled(true)
                .build();

        given(productUseCase.enableProduct(id)).willReturn(mockProduct);
        given(responseMapper.toDto(mockProduct)).willReturn(mockResponseDto);

        mockMvc.perform(patch("/api/v1/products/{id}/enable", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.code", is("CODE123")))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    @Test
    @DisplayName("PATCH /api/v1/products/{id}/enable debe responder 404 si no existe el producto")
    void shouldReturn404WhenProductNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        given(productUseCase.enableProduct(id)).willThrow(new ProductNotFoundException(id));

        mockMvc.perform(patch("/api/v1/products/{id}/enable", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PATCH /api/v1/products/{id}/enable debe responder 400 si el ID es inválido")
    void shouldReturn400ForInvalidUUID() throws Exception {
        mockMvc.perform(patch("/api/v1/products/invalid-uuid/enable"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /api/v1/products/{id}/enable debe responder 500 en error inesperado")
    void shouldReturn500OnUnexpectedError() throws Exception {
        UUID id = UUID.randomUUID();

        given(productUseCase.enableProduct(id)).willThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(patch("/api/v1/products/{id}/enable", id))
                .andExpect(status().isInternalServerError());
    }
}