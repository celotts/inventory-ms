package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productserviceOld.domain.model.ProductCategoryModel;
import com.celotts.productserviceOld.domain.port.product.port.usecase.ProductCategoryUseCase;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.controller.ProductCategoryController;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryCreateDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productCategory.ProductCategoryResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productCategory.ProductCategoryDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test en modo standalone (sin contexto de Spring).
 */
class ProductCategoryControllerTest {

    private MockMvc mockMvc;
    private final ProductCategoryUseCase useCase = Mockito.mock(ProductCategoryUseCase.class);
    private final ProductCategoryDtoMapper mapper = Mockito.mock(ProductCategoryDtoMapper.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASE = "/api/v1/product-category";

    @BeforeEach
    void setup() {
        ProductCategoryController controller = new ProductCategoryController(useCase, mapper);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                // .setControllerAdvice(new TuGlobalExceptionHandler()) // si tienes uno
                .build();
    }

    private ProductCategoryCreateDto createDto(UUID productId, UUID categoryId) {
        return ProductCategoryCreateDto.builder()
                .productId(productId)
                .categoryId(categoryId)
                .enabled(true)
                .createdBy("tester")
                .build();
    }

    private ProductCategoryModel model(UUID id, UUID productId, UUID categoryId) {
        return ProductCategoryModel.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .enabled(true)
                .createdBy("tester")
                .build();
    }

    private ProductCategoryResponseDto response(UUID id, UUID productId, UUID categoryId) {
        return ProductCategoryResponseDto.builder()
                .id(id)
                .productId(productId)
                .categoryId(categoryId)
                .enabled(true)
                .createdBy("tester")
                .build();
    }

    @Test
    @DisplayName("POST -> 201 Created")
    void create_shouldReturn201() throws Exception {
        UUID id = UUID.randomUUID(), p = UUID.randomUUID(), c = UUID.randomUUID();
        var dto = createDto(p, c);
        var created = model(id, p, c);
        var resp = response(id, p, c);

        Mockito.when(useCase.assignCategoryToProduct(any(ProductCategoryCreateDto.class))).thenReturn(created);
        Mockito.when(mapper.toDto(created)).thenReturn(resp);

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.productId").value(p.toString()))
                .andExpect(jsonPath("$.categoryId").value(c.toString()))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    @DisplayName("GET /{id} -> 200 OK")
    void getById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID(), p = UUID.randomUUID(), c = UUID.randomUUID();
        var found = model(id, p, c);
        var resp = response(id, p, c);

        Mockito.when(useCase.getById(eq(id))).thenReturn(found);
        Mockito.when(mapper.toDto(found)).thenReturn(resp);

        mockMvc.perform(get(BASE + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.productId").value(p.toString()))
                .andExpect(jsonPath("$.categoryId").value(c.toString()));
    }

    @Test
    @DisplayName("GET -> 200 OK (list)")
    void getAll_shouldReturnList() throws Exception {
        UUID id1 = UUID.randomUUID(), p1 = UUID.randomUUID(), c1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID(), p2 = UUID.randomUUID(), c2 = UUID.randomUUID();

        var m1 = model(id1, p1, c1);
        var m2 = model(id2, p2, c2);
        var r1 = response(id1, p1, c1);
        var r2 = response(id2, p2, c2);

        Mockito.when(useCase.getAll()).thenReturn(List.of(m1, m2));
        Mockito.when(mapper.toDto(m1)).thenReturn(r1);
        Mockito.when(mapper.toDto(m2)).thenReturn(r2);

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(id1.toString()))
                .andExpect(jsonPath("$[1].id").value(id2.toString()));
    }

    @Test
    @DisplayName("DELETE /{id} -> 204 No Content (soft)")
    void disable_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete(BASE + "/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(useCase).disableById(id);
    }

    @Test
    @DisplayName("DELETE /{id}/hard -> 204 No Content (hard)")
    void delete_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete(BASE + "/{id}/hard", id))
                .andExpect(status().isNoContent());

        Mockito.verify(useCase).deleteById(id);
    }

    @Test
    @DisplayName("POST -> 400 Bad Request cuando faltan campos requeridos")
    void create_shouldReturn400_whenValidationFails() throws Exception {
        var invalid = ProductCategoryCreateDto.builder()
                .enabled(true)
                .createdBy("tester")
                .build();

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(useCase);
    }
}