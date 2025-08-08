package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductBrandService;
import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import java.util.UUID;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(com.celotts.productservice.infrastructure.common.config.ProductBrandTestBeansConfig.class)
@ActiveProfiles("test")
class ProductBrandControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ProductBrandUseCase productBrandUseCase;
    @Autowired private ProductBrandService productBrandService;
    @Autowired private ProductBrandDtoMapper productBrandDtoMapper;

    private static final UUID brandId = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private ProductBrandModel brandModel;
    private ProductBrandResponseDto responseDto;

    @BeforeEach
    void setup() {
        brandModel = ProductBrandModel.builder()
                .id(brandId)
                .name("BrandX")
                .description("description")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        responseDto = ProductBrandResponseDto.builder()
                .id(brandId)
                .name("BrandX")
                .description("description")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void create_shouldReturnCreatedBrand() throws Exception {
        ProductBrandCreateDto createDto = ProductBrandCreateDto.builder()
                .name("BrandX")
                .description("description")
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        when(productBrandService.create(any(ProductBrandCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/product-brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print()) // <-- para ver la respuesta real
                .andExpect(status().isCreated()) // asegúrate que tu controlador retorna HttpStatus.CREATED
                .andExpect(jsonPath("$.id").value(brandId.toString()))
                .andExpect(jsonPath("$.name").value("BrandX"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void update_shouldReturnUpdatedBrand() throws Exception {
        ProductBrandUpdateDto updateDto = ProductBrandUpdateDto.builder()
                .name("BrandX")
                .description("description")
                .enabled(true)
                .updatedBy("admin")
                .build();

        when(productBrandService.update(eq(brandId), any())).thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/product-brands/{id}", brandId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void disableBrand_shouldReturnDisabledBrand() throws Exception {
        ProductBrandModel disabledModel = brandModel.toBuilder().enabled(false).build();
        ProductBrandResponseDto disabledDto = responseDto.toBuilder().enabled(false).build();

        when(productBrandUseCase.disableBrand(brandId)).thenReturn(disabledModel);
        when(productBrandDtoMapper.toResponseDto(disabledModel)).thenReturn(disabledDto);

        mockMvc.perform(patch("/api/v1/product-brands/{id}/disable", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false)); // ✅ aquí el cambio
    }

   @Test
   @WithMockUser(authorities = "ROLE_ADMIN")
   void enableBrand_shouldReturnEnabledBrand() throws Exception {
       when(productBrandUseCase.enableBrand(brandId)).thenReturn(brandModel);
       when(productBrandDtoMapper.toResponseDto(brandModel)).thenReturn(responseDto);

       mockMvc.perform(patch("/api/v1/product-brands/{id}/enable", brandId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.enabled").value(true)); // ✅ valor correcto según el mock}
   }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getBrandNameById_shouldReturnName() throws Exception {
        when(productBrandService.findNameById(brandId)).thenReturn(Optional.of("BrandX"));

        mockMvc.perform(get("/api/v1/product-brands/brands/{id}/name", brandId))
                .andExpect(status().isOk())
                .andExpect(content().string("BrandX"));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void contextLoads() {
        assertNotNull(mockMvc);
        assertNotNull(productBrandService);
        assertNotNull(productBrandUseCase);
        assertNotNull(productBrandDtoMapper);
    }
}