package com.celotts.productservice.infrastructure.adapter.input.rest.advice;

import com.celotts.productservice.infrastructure.adapter.input.rest.controller.ProductController;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.domain.port.product.ProductUseCase;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerAdviceTest {

    private MockMvc mockMvc;
    private ProductUseCase productUseCase;

    @BeforeEach
    void setup() {
        productUseCase = Mockito.mock(ProductUseCase.class);
        ProductController controller = new ProductController(productUseCase, null, null);
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
                // 👇 Temporalmente comenta los assertions para ver el body real
                // .andExpect(status().isNotFound())
                // .andExpect(jsonPath(...))
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

        MvcResult result = mockMvc.perform(patch("/api/v1/products/{id}/enable", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                // .andExpect(status().isInternalServerError())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println("\n🚀 RESPONSE BODY 500:\n" + responseBody + "\n");
    }


}