package com.celotts.productservice.infrastructure.adapter.input.rest.advice;

import com.celotts.productservice.infrastructure.adapter.input.rest.controller.ProductController;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.domain.port.product.ProductUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerAdviceTest {

    private MockMvc mockMvc;
    private ProductUseCase productUseCase;

    @BeforeEach
    void setup() {
        productUseCase = Mockito.mock(ProductUseCase.class);
        ObjectMapper objectMapper = new ObjectMapper();

        ProductController productController = new ProductController(productUseCase, null, null); // Ajusta si tienes más dependencias

        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ProductControllerAdvice())
                .build();
    }

    @Test
    @DisplayName("PATCH /api/v1/products/{id}/enable debe responder 404 si producto no existe")
    void shouldReturn404WhenProductNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(productUseCase.enableProduct(id))
                .thenThrow(new ProductNotFoundException("No existe el producto con ID: " + id));

        mockMvc.perform(patch("/api/v1/products/{id}/enable", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No existe el producto con ID: " + id))
                .andExpect(jsonPath("$.path").value("/api/v1/products/" + id + "/enable"));
    }

    @Test
    @DisplayName("PATCH /api/v1/products/{id}/enable debe responder 500 si ocurre error inesperado")
    void shouldReturn500OnUnexpectedError() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(productUseCase.enableProduct(id))
                .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(patch("/api/v1/products/{id}/enable", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Error inesperado"))
                .andExpect(jsonPath("$.path").value("/api/v1/products/" + id + "/enable"));
    }
}