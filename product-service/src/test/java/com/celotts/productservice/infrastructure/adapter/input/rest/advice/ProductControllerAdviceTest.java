package com.celotts.productservice.infrastructure.adapter.input.rest.advice;

import com.celotts.productservice.infrastructure.config.PaginationProperties;
import org.springframework.test.context.ActiveProfiles;

import com.celotts.productservice.infrastructure.adapter.input.rest.controller.ProductController;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.domain.port.product.port.usecase.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
class ProductControllerAdviceTest {

    private MockMvc mockMvc;
    private ProductUseCase productUseCase;
    private PaginationProperties paginationProperties;

    @BeforeEach
    void setup() {
        productUseCase = Mockito.mock(ProductUseCase.class);
        ProductResponseMapper responseMapper = Mockito.mock(ProductResponseMapper.class);

        ProductController controller = new ProductController(productUseCase, responseMapper, paginationProperties);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ProductControllerAdvice())
                .build();
    }

    @Test
    @DisplayName("PATCH /api/v1/products/{id}/enable debe responder 404 si producto no existe")
    void shouldReturn404WhenProductNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(productUseCase.enableProduct(id))
                .thenThrow(new ProductNotFoundException(id));

        MvcResult result = mockMvc.perform(patch("/api/v1/products/{id}/enable", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString(id.toString())))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("\n🚀 RESPONSE BODY 404:\n" + responseBody + "\n");
    }

    @Test
    @DisplayName("PATCH /api/v1/products/{id}/enable debe responder 500 si ocurre error inesperado")
    void shouldReturn500OnUnexpectedError() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(productUseCase.enableProduct(id))
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(patch("/api/v1/products/{id}/enable", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Unexpected error"))
                .andExpect(jsonPath("$.path").value("/api/v1/products/" + id + "/enable"));
    }

    @Test
    @DisplayName("GET /api/v1/products/{id} debe responder 400 si el id no es UUID")
    void shouldReturn400WhenInvalidUUID() throws Exception {
        mockMvc.perform(patch("/api/v1/products/abc/enable") // ⚠️ "abc" no es UUID válido
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Invalid parameter")))
                .andExpect(jsonPath("$.path").value("/api/v1/products/abc/enable"));
    }

    @Test
    @DisplayName("PATCH /api/v1/products/{id}/enable debe responder 400 si se lanza IllegalArgumentException")
    void shouldReturn400WhenIllegalArgumentException() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(productUseCase.enableProduct(id))
                .thenThrow(new IllegalArgumentException("Producto inválido"));

        mockMvc.perform(patch("/api/v1/products/{id}/enable", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Producto inválido"))
                .andExpect(jsonPath("$.path").value("/api/v1/products/" + id + "/enable"));
    }


}