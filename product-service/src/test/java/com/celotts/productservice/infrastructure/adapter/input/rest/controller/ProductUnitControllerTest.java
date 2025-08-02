package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductUnitService;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.domain.port.product.unit.usecase.ProductUnitUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductUnitController.class)
@Import(ProductUnitControllerTest.MockBeans.class)
@ActiveProfiles("test")
class ProductUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductUnitService productUnitService;

    @Test
    void create_shouldReturnCreatedProductUnit() throws Exception {
        UUID id = UUID.randomUUID();
        ProductUnitCreateDto createDto = ProductUnitCreateDto.builder()
                .code("kg")
                .name("Kilogramo")
                .description("Unidad de masa")
                .enabled(true)
                .createdBy("tester")
                .updatedBy("tester")
                .build();

        ProductUnitResponseDto responseDto = ProductUnitResponseDto.builder()
                .id(id)
                .code("kg")
                .name("Kilogramo")
                .description("Unidad de masa")
                .enabled(true)
                .createdBy("tester")
                .updatedBy("tester")
                .build();

        when(productUnitService.create(any(ProductUnitCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/product-units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Kilogramo"))
                .andExpect(jsonPath("$.code").value("kg"));
    }

    @TestConfiguration
    static class MockBeans {

        @Bean
        public ProductUnitUseCase productUnitUseCase() {
            return mock(ProductUnitUseCase.class);
        }

        @Bean
        public ProductUnitRepositoryPort productUnitRepositoryPort() {
            return mock(ProductUnitRepositoryPort.class);
        }

        @Bean
        public ProductUnitDtoMapper productUnitDtoMapper() {
            return new ProductUnitDtoMapper();
        }

        @Bean
        public ProductUnitService productUnitService(
                ProductUnitUseCase useCase,
                ProductUnitRepositoryPort repo,
                ProductUnitDtoMapper mapper
        ) {
            return spy(new ProductUnitService(useCase, repo, mapper)); // spy para poder usar when().thenReturn()
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper().findAndRegisterModules();
        }
    }
}