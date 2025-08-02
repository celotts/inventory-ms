package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductBrandService;
import com.celotts.productservice.config.MockBeansConfig;
import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductBrandController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({ProductBrandController.class, MockBeansConfig.class})
@ActiveProfiles("test")
class ProductBrandControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProductBrandService productBrandService;
    @Autowired private ProductBrandUseCase productBrandUseCase;
    @Autowired private ProductBrandDtoMapper productBrandDtoMapper;
    @Autowired private ObjectMapper objectMapper;

    private UUID brandId;
    private ProductBrandResponseDto responseDto;

    @BeforeEach
    void setUp() {
        brandId = UUID.randomUUID();
        responseDto = ProductBrandResponseDto.builder()
                .id(brandId)
                .name("BrandX")
                .description("Desc")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();
    }

    @Test
    void getAllBrands_shouldReturnList() throws Exception {
        when(productBrandService.findAll()).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/product-brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(brandId.toString()));
    }

    @Test
    void getBrandById_shouldReturnDto() throws Exception {
        when(productBrandService.findById(brandId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/product-brands/{id}", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(brandId.toString()));
    }

    @Test
    void getAllBrandIds_shouldReturnListOfIds() throws Exception {
        when(productBrandService.findAllIds()).thenReturn(List.of(brandId));

        mockMvc.perform(get("/api/v1/product-brands/brands/ids"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]").value(brandId.toString()));
    }

    @Test
    void getBrandNameById_shouldReturnName() throws Exception {
        when(productBrandService.findNameById(brandId)).thenReturn(Optional.of("BrandX"));

        mockMvc.perform(get("/api/v1/product-brands/brands/{id}/name", brandId))
                .andExpect(status().isOk())
                .andExpect(content().string("BrandX"));
    }

    @Test
    void create_shouldReturnCreatedBrand() throws Exception {
        ProductBrandCreateDto createDto = ProductBrandCreateDto.builder()
                .name("BrandX")
                .description("Desc")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        when(productBrandService.create(any())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/product-brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(brandId.toString()));
    }

    @Test
    void update_shouldReturnUpdatedBrand() throws Exception {
        ProductBrandUpdateDto updateDto = ProductBrandUpdateDto.builder()
                .name("UpdatedBrand")
                .description("Updated description")
                .enabled(true)
                .updatedBy("admin")
                .build();

        when(productBrandService.update(eq(brandId), any())).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/product-brands/{id}", brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(brandId.toString()));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        doNothing().when(productBrandService).delete(brandId);

        mockMvc.perform(delete("/api/v1/product-brands/{id}", brandId))
                .andExpect(status().isNoContent());
    }

    @Test
    void enableBrand_shouldReturnEnabledBrand() throws Exception {
        ProductBrandModel brandModel = ProductBrandModel.builder()
                .id(brandId)
                .name("BrandX")
                .description("description")
                .enabled(true)
                .createdBy("tester")
                .updatedBy("tester")
                .build();

        when(productBrandUseCase.enableBrand(brandId)).thenReturn(brandModel);
        when(productBrandDtoMapper.toResponseDto(brandModel)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/product-brands/" + brandId + "/enable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(brandId.toString()))
                .andExpect(jsonPath("$.name").value("BrandX"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void disableBrand_shouldReturnDisabledBrand() throws Exception {
        ProductBrandModel disabledModel = ProductBrandModel.builder()
                .id(brandId).name("BrandX").enabled(false).build();

        when(productBrandUseCase.disableBrand(brandId)).thenReturn(disabledModel);
        when(productBrandDtoMapper.toResponseDto(disabledModel)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/product-brands/{id}/disable", brandId))
                .andExpect(status().isOk());
    }

    @Test
    void getBrandById_shouldReturnBrand_whenExists() throws Exception {
        when(productBrandService.findById(brandId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/product-brands/{id}", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(brandId.toString()))
                .andExpect(jsonPath("$.name").value("BrandX"));
    }

    @Test
    void getBrandById_shouldReturnNotFound_whenNotExists() throws Exception {
        when(productBrandService.findById(brandId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/v1/product-brands/{id}", brandId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBrandNameById_shouldReturnName_whenExists() throws Exception {
        when(productBrandService.findNameById(brandId)).thenReturn(java.util.Optional.of("BrandX"));

        mockMvc.perform(get("/api/v1/product-brands/brands/{id}/name", brandId))
                .andExpect(status().isOk())
                .andExpect(content().string("BrandX"));
    }

    @Test
    void getBrandNameById_shouldReturnNotFound_whenNotExists() throws Exception {
        when(productBrandService.findNameById(brandId)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/v1/product-brands/brands/{id}/name", brandId))
                .andExpect(status().isNotFound());
    }


    @Test
    void enableBrand_shouldEnableBrand() throws Exception {
        ProductBrandModel brandModel = ProductBrandModel.builder()
                .id(brandId)
                .name("BrandX")
                .enabled(true)
                .build();

        when(productBrandUseCase.enableBrand(brandId)).thenReturn(brandModel);
        when(productBrandDtoMapper.toResponseDto(brandModel)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/product-brands/{id}/enable", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(brandId.toString()))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void disableBrand_shouldDisableBrand() throws Exception {
        ProductBrandModel brandModel = ProductBrandModel.builder()
                .id(brandId)
                .name("BrandX")
                .enabled(false)
                .build();

        ProductBrandResponseDto disabledResponse = ProductBrandResponseDto.builder()
                .id(brandId)
                .name("BrandX")
                .enabled(false)
                .description("description")
                .createdBy("tester")
                .updatedBy("tester")
                .build();

        when(productBrandUseCase.disableBrand(brandId)).thenReturn(brandModel);
        when(productBrandDtoMapper.toResponseDto(brandModel)).thenReturn(disabledResponse);

        mockMvc.perform(patch("/api/v1/product-brands/{id}/disable", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(brandId.toString()))
                .andExpect(jsonPath("$.enabled").value(false));
    }

   /* @TestConfiguration
    public static class MockBeans {

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
    } */
}