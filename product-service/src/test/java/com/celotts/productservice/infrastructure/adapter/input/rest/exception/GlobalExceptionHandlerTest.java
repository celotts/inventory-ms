package com.celotts.productservice.infrastructure.adapter.input.rest.exception;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handleProductNotFoundException() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/test/product-not-found/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Product not found with ID")));
    }

    @Test
    void handleProductAlreadyExistsException() throws Exception {
        mockMvc.perform(get("/test/product-already-exists"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", containsString("Ya existe un producto con el código")));
    }


    @Test
    void handleInvalidBrandIdException() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(get("/test/invalid-brand-id/" + id))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", containsString("ID de marca no es válido")));
    }

    @Test
    void handleIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/test/illegal-argument"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Este argumento no es válido"));
    }

    @Test
    void handleGlobalException() throws Exception {
        mockMvc.perform(get("/test/global-exception"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Fallo inesperado"));
    }


}