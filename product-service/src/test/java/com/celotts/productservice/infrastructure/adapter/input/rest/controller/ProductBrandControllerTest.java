package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productserviceOld.applications.service.ProductBrandService;
import com.celotts.productserviceOld.domain.model.ProductBrandModel;
import com.celotts.productserviceOld.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandCreateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand.ProductBrandUpdateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
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

    @Test
    void delete_shouldReturnNoContent_andInvokeService() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/product-brands/{id}", id))
                .andExpect(status().isNoContent());

        verify(productBrandService).delete(eq(id));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getAllBrands_shouldReturnDataAndTotal() throws Exception {
        ProductBrandResponseDto b1 = ProductBrandResponseDto.builder()
                .id(UUID.randomUUID()).name("A").enabled(true).build();
        ProductBrandResponseDto b2 = ProductBrandResponseDto.builder()
                .id(UUID.randomUUID()).name("B").enabled(false).build();

        when(productBrandService.findAll()).thenReturn(java.util.List.of(b1, b2));

        mockMvc.perform(get("/api/v1/product-brands").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data[0].name").value("A"))
                .andExpect(jsonPath("$.data[1].name").value("B"))
                .andExpect(jsonPath("$.total").value(2));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getBrandById_shouldReturnDto() throws Exception {
        when(productBrandService.findById(brandId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/product-brands/{id}", brandId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(brandId.toString()))
                .andExpect(jsonPath("$.name").value("BrandX"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getAllBrandIds_shouldReturnUuidArray() throws Exception {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();
        when(productBrandService.findAllIds()).thenReturn(java.util.List.of(a, b));

        mockMvc.perform(get("/api/v1/product-brands/brands/ids"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0]").value(a.toString()))
                .andExpect(jsonPath("$[1]").value(b.toString()));
    }


    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getAllBrands_shouldReturnTotalZero_whenServiceReturnsNull() throws Exception {
        when(productBrandService.findAll()).thenReturn(null);

        mockMvc.perform(get("/api/v1/product-brands").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", org.hamcrest.Matchers.hasSize(0)))
                .andExpect(jsonPath("$.total").value(0));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getBrandNameById_shouldMapToResponseEntityOk() throws Exception {
        String expectedName = "BrandMapped";
        when(productBrandService.findNameById(brandId))
                .thenReturn(Optional.of(expectedName));

        mockMvc.perform(get("/api/v1/product-brands/brands/{id}/name", brandId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedName));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void getAllBrands_whenServiceReturnsEmptyList_shouldReturnTotalZero_andEmptyArray() throws Exception {
        when(productBrandService.findAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/api/v1/product-brands").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", org.hamcrest.Matchers.hasSize(0)))
                .andExpect(jsonPath("$.total").value(0));
    }


}