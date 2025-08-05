/*package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductBrandService;
import com.celotts.productservice.applications.service.ProductUnitService;
import com.celotts.productservice.domain.port.product.brand.usecase.ProductBrandUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productBrand.ProductBrandDtoMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ProductUnitController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        ProductUnitController.class,
        ProductUnitControllerTest.MockBeans.class
})
class ProductUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductUnitService productUnitService;

    @Autowired
    private ProductUnitDtoMapper productUnitDtoMapper;

    @Test
    void findAll_shouldReturnListOfProductUnits() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        var dto1 = ProductUnitResponseDto.builder()
                .id(id1)
                .name("Unit One")
                .code("U1")
                .enabled(true)
                .build();

        var dto2 = ProductUnitResponseDto.builder()
                .id(id2)
                .name("Unit Two")
                .code("U2")
                .enabled(true)
                .build();

        List<ProductUnitResponseDto> responseList = List.of(dto1, dto2);

        when(productUnitService.findAll()).thenReturn(responseList);

        mockMvc.perform(get("/api/v1/product-units"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(id1.toString()))
                .andExpect(jsonPath("$[0].name").value("Unit One"))
                .andExpect(jsonPath("$[0].code").value("U1"))
                .andExpect(jsonPath("$[1].id").value(id2.toString()))
                .andExpect(jsonPath("$[1].name").value("Unit Two"))
                .andExpect(jsonPath("$[1].code").value("U2"));
    }

    @Test
    void findById_shouldReturnProductUnit_whenFound() throws Exception {
        UUID id = UUID.randomUUID();

        ProductUnitResponseDto responseDto = ProductUnitResponseDto.builder()
                .id(id)
                .code("U1")
                .name("Unit One")
                .enabled(true)
                .build();

        when(productUnitService.findById(id)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/product-units/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.code").value("U1"))
                .andExpect(jsonPath("$.name").value("Unit One"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void existsByCode_shouldReturnTrue() throws Exception {
        String code = "U1";

        when(productUnitService.existsByCode(code)).thenReturn(true);

        mockMvc.perform(get("/api/v1/product-units/exists-by-code/{code}", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));
    }

    @TestConfiguration
    public static class MockBeans {

        @Bean
        public ProductBrandUseCase productBrandUseCase() {
            return mock(ProductBrandUseCase.class);
        }

        @Bean
        public ProductBrandDtoMapper productBrandDtoMapper() {
            return mock(ProductBrandDtoMapper.class);
        }

        @Bean
        public ProductBrandService productBrandService() {
            return mock(ProductBrandService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }
}*/