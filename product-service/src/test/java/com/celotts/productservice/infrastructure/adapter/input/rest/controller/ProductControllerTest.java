package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.ProductRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal") // Suprime el warning de deprecación
    @MockBean
    private ProductRepositoryPort productRepositoryPort;

    @Test
    @DisplayName("GET /products devuelve lista de productos")
    void shouldReturnListOfProducts() throws Exception {
        UUID id = UUID.randomUUID();

        ProductModel product = ProductModel.builder()
                .id(id)
                .code("TACO001")
                .description("Taco al pastor")
                .productTypeCode("FOOD")
                .unitCode("UNIT")
                .brandId(UUID.randomUUID())
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(new BigDecimal("25.50"))
                .enabled(true)
                .build();

        Mockito.when(productRepositoryPort.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].code", is("TACO001")))
                .andExpect(jsonPath("$[0].description", is("Taco al pastor")))
                .andExpect(jsonPath("$[0].minimumStock", is(10)))
                .andExpect(jsonPath("$[0].currentStock", is(50)))
                .andExpect(jsonPath("$[0].unitPrice", is(25.50)))
                .andExpect(jsonPath("$[0].enabled", is(true)));
    }
}
