package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductBrandService;
import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductBrandController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(ProductBrandControllerTest.MockConfig.class)
class ProductBrandControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProductBrandService productBrandService;
    @Autowired private ProductBrandUseCase productBrandUseCase;
    @Autowired private ProductBrandDtoMapper productBrandDtoMapper;

    private UUID brandId;

    @BeforeEach
    void setUp() {
        brandId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    }

    @Test
    void createBrand_shouldReturnCreatedBrand() throws Exception {
        ProductBrandResponseDto responseDto = ProductBrandResponseDto.builder()
                .id(brandId)
                .name("BrandX")
                .description("description")
                .enabled(true)
                .createdBy("tester")
                .updatedBy("tester")
                .createdAt(LocalDateTime.of(2023, 1, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
                .build();

        when(productBrandService.create(any(ProductBrandCreateDto.class))).thenReturn(responseDto);

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        System.out.println("EXPECTED JSON: " + mapper.writeValueAsString(responseDto));

        mockMvc.perform(post("/api/v1/product-brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
    {
      "name": "BrandX",
      "description": "description",
      "enabled": true,
      "createdBy": "tester",
      "updatedBy": "tester"
    }
    """))
                .andDo(result -> {
                    int actualStatus = result.getResponse().getStatus();
                    String responseBody = result.getResponse().getContentAsString();
                    System.out.println(">>> ACTUAL STATUS: " + actualStatus);
                    System.out.println(">>> RESPONSE BODY: " + responseBody);
                });
    }

    @Test
    void getAllBrands_shouldReturnList() throws Exception {
        List<ProductBrandResponseDto> brands = List.of(
                ProductBrandResponseDto.builder()
                        .id(brandId)
                        .name("Brand1")
                        .description("desc1")
                        .enabled(true)
                        .createdBy("test")
                        .updatedBy("test")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),
                ProductBrandResponseDto.builder()
                        .id(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                        .name("Brand2")
                        .description("desc2")
                        .enabled(false)
                        .createdBy("test")
                        .updatedBy("test")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        when(productBrandService.findAll()).thenReturn(brands);

        mockMvc.perform(get("/api/v1/product-brands"))
                .andDo(result -> {
                    System.out.println(">>> STATUS: " + result.getResponse().getStatus());
                    System.out.println(">>> BODY: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getBrandById_shouldReturnBrand() throws Exception {
        ProductBrandResponseDto dto = ProductBrandResponseDto.builder()
                .id(brandId)
                .name("BrandZ")
                .description("desc")
                .enabled(true)
                .createdBy("test")
                .updatedBy("test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(productBrandService.findById(brandId)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/product-brands/" + brandId))
                .andDo(result -> {
                    System.out.println(">>> STATUS: " + result.getResponse().getStatus());
                    System.out.println(">>> BODY: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BrandZ"));
    }

    @Test
    void getBrandNameById_shouldReturnName() throws Exception {
        when(productBrandService.findNameById(brandId)).thenReturn(Optional.of("FancyName"));

        mockMvc.perform(get("/api/v1/product-brands/brands/" + brandId + "/name"))
                .andDo(result -> {
                    System.out.println(">>> STATUS: " + result.getResponse().getStatus());
                    System.out.println(">>> BODY: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk())
                .andExpect(content().string("FancyName"));
    }

    @Test
    void enableBrand_shouldReturnEnabledBrand() throws Exception {
        // Arrange
        ProductBrandModel brandModel = new ProductBrandModel(brandId, "CoolBrand", true);
        ProductBrandResponseDto dto = ProductBrandResponseDto.builder()
                .id(brandId)
                .name("CoolBrand")
                .description("desc")
                .enabled(true) // este valor debe ser true para pasar el assert
                .createdBy("test")
                .updatedBy("test")
                .createdAt(LocalDateTime.of(2023, 1, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
                .build();

        when(productBrandUseCase.enableBrand(brandId)).thenReturn(brandModel);
        when(productBrandDtoMapper.toResponseDto(brandModel)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/product-brands/" + brandId + "/enable"))
                .andDo(result -> {
                    System.out.println(">>> STATUS: " + result.getResponse().getStatus());
                    System.out.println(">>> BODY: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Configuration
    static class MockConfig {
        @Bean public ProductBrandService productBrandService() {
            return Mockito.mock(ProductBrandService.class);
        }

        @Bean public ProductBrandUseCase productBrandUseCase() {
            return Mockito.mock(ProductBrandUseCase.class);
        }

        @Bean public ProductBrandDtoMapper productBrandDtoMapper() {
            return Mockito.mock(ProductBrandDtoMapper.class);
        }
    }
}
