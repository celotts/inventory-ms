package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductBrandService;
import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductBrandController.class)
@Import(ProductBrandControllerTest.MockBeans.class)
@ActiveProfiles("test")
class ProductBrandControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProductBrandUseCase productBrandUseCase;
    @Autowired private ProductBrandDtoMapper productBrandDtoMapper;

    private UUID brandId;
    private ProductBrandModel brandModel;
    private ProductBrandResponseDto responseDto;

    @BeforeEach
    void setUp() {
        brandId = UUID.randomUUID();
        brandModel = ProductBrandModel.builder()
                .id(brandId)
                .name("BrandX")
                .description("Some Description")
                .enabled(true)
                .createdBy("tester")
                .updatedBy("tester")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        responseDto = ProductBrandResponseDto.builder()
                .id(brandId)
                .name("BrandX")
                .description("Some Description")
                .enabled(true)
                .createdBy("tester")
                .updatedBy("tester")
                .createdAt(brandModel.getCreatedAt())
                .updatedAt(brandModel.getUpdatedAt())
                .build();
    }

    @Test
    void enableBrand_shouldReturnEnabledBrand() throws Exception {
        when(productBrandUseCase.enableBrand(brandId)).thenReturn(brandModel);
        when(productBrandDtoMapper.toResponseDto(brandModel)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/product-brands/{id}/enable", brandId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(brandId.toString()))
                .andExpect(jsonPath("$.name").value("BrandX"));
    }

    @TestConfiguration
    static class MockBeans {
        @Bean
        public ProductBrandService productBrandService() {
            return mock(ProductBrandService.class);
        }

        @Bean
        public ProductBrandUseCase productBrandUseCase() {
            return mock(ProductBrandUseCase.class);
        }

        @Bean
        public ProductBrandDtoMapper productBrandDtoMapper() {
            return mock(ProductBrandDtoMapper.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }
}