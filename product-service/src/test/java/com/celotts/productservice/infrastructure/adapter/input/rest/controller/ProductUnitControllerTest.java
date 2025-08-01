package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductUnitService;
import com.celotts.productservice.domain.port.product.port.usecase.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitResponseMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductUnitController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductUnitService productUnitService;

    @Autowired
    private ProductUnitResponseMapper productUnitResponseMapper;

    @Autowired
    private ProductUseCase productUseCase;

    private UUID unitId;

    @BeforeEach
    void setUp() {
        unitId = UUID.randomUUID();
    }

    @Test
    void create_shouldReturnCreatedProductUnit() throws Exception {
        ProductUnitResponseDto responseDto = ProductUnitResponseDto.builder()
                .id(unitId)
                .code("UNI02")
                .name("Unidad 02")
                .description("Descripción de prueba")
                .enabled(true)
                .createdBy("test-user")
                .updatedBy("test-user")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(productUnitService.create(any(ProductUnitCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/product-unit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "code": "UNI02",
                            "name": "Unidad 02",
                            "enabled": true
                        }
                        """))
                .andDo(result -> {
                    System.out.println("STATUS: " + result.getResponse().getStatus());
                    System.out.println("BODY: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(unitId.toString()))
                .andExpect(jsonPath("$.code").value("UNI02"))
                .andExpect(jsonPath("$.name").value("Unidad 02"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    // ... (los demás tests igual)

    @Configuration
    static class MockConfig {
        @Bean
        public ProductUnitService productUnitService() {
            return Mockito.mock(ProductUnitService.class);
        }

        @Bean
        public ProductUnitResponseMapper productUnitResponseMapper() {
            return Mockito.mock(ProductUnitResponseMapper.class);
        }

        @Bean
        public ProductUseCase productUseCase() {
            return Mockito.mock(ProductUseCase.class);
        }
    }
}