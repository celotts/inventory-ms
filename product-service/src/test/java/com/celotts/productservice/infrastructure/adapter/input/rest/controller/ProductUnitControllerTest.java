package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.input.product.ProductUnitUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductUnitControllerTest {

    private static final String BASE = "/api/v1/product-units";

    @Mock private ProductUnitUseCase productUnitUseCase;
    @Mock private ProductUnitDtoMapper productUnitDtoMapper;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        ProductUnitController controller = new ProductUnitController(productUnitUseCase, productUnitDtoMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /product-units -> 201 Created con body")
    void create_returns201() throws Exception {
        // lo que devuelve el caso de uso (dominio)
        ProductUnitModel savedModel = mock(ProductUnitModel.class);

        // lo que expone el controller (dto)
        ProductUnitResponseDto resp = ProductUnitResponseDto.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .code("KG").name("Kilogramo").symbol("kg").enabled(true).build();

        when(productUnitUseCase.save(any(ProductUnitModel.class))).thenReturn(savedModel);
        when(productUnitDtoMapper.toResponseDto(savedModel)).thenReturn(resp);

        Map<String, Object> req = Map.of(
                "code", "KG",
                "name", "Kilogramo",
                "description", "Unidad de masa",
                "enabled", true,
                "symbol", "kg",
                "createdBy", "tester"
        );

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("00000000-0000-0000-0000-000000000001"))
                .andExpect(jsonPath("$.code").value("KG"))
                .andExpect(jsonPath("$.name").value("Kilogramo"))
                .andExpect(jsonPath("$.symbol").value("kg"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void create_returns400_whenInvalid() throws Exception {
        Map<String, Object> invalid = Map.of("enabled", true); // faltan campos @Valid

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(invalid)))
                .andExpect(status().isBadRequest());

        verify(productUnitUseCase, never()).save(any());
    }

    @Test
    void findAll_returnsList() throws Exception {
        // dominio
        var m1 = mock(ProductUnitModel.class);
        var m2 = mock(ProductUnitModel.class);
        when(productUnitUseCase.findAll()).thenReturn(List.of(m1, m2));

        // dto
        var u1 = ProductUnitResponseDto.builder().id(UUID.randomUUID()).code("KG").name("Kilogramo").symbol("kg").enabled(true).build();
        var u2 = ProductUnitResponseDto.builder().id(UUID.randomUUID()).code("LT").name("Litro").symbol("l").enabled(true).build();
        when(productUnitDtoMapper.toResponseDtoList(List.of(m1, m2))).thenReturn(List.of(u1, u2));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code").value("KG"))
                .andExpect(jsonPath("$[1].code").value("LT"));
    }

    @Test
    void findById_returnsDto() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000010");

        // dominio
        var model = mock(ProductUnitModel.class);
        when(productUnitUseCase.findById(id)).thenReturn(Optional.of(model));

        // dto
        var u = ProductUnitResponseDto.builder().id(id).code("KG").name("Kilogramo").symbol("kg").enabled(true).build();
        when(productUnitDtoMapper.toResponseDto(model)).thenReturn(u);

        mockMvc.perform(get(BASE + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.code").value("KG"));
    }

    @Test
    void update_returnsDto() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000020");

        // dominio
        var updated = mock(ProductUnitModel.class);
        when(productUnitUseCase.update(eq(id), any(ProductUnitModel.class))).thenReturn(updated);

        // dto
        var u = ProductUnitResponseDto.builder().id(id).code("KG").name("Kilogramo actualizado").symbol("kg").enabled(true).build();
        when(productUnitDtoMapper.toResponseDto(updated)).thenReturn(u);

        Map<String, Object> req = Map.of(
                "name", "Kilogramo actualizado",
                "description", "desc",
                "symbol", "kg",
                "enabled", true,
                "updatedBy", "tester"
        );

        mockMvc.perform(put(BASE + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Kilogramo actualizado"));
    }

    @Test
    void delete_returns204() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000030");
        doNothing().when(productUnitUseCase).deleteById(id);

        mockMvc.perform(delete(BASE + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(productUnitUseCase).deleteById(id);
    }

    @Test
    void existsByCode_returnsMap() throws Exception {
        when(productUnitUseCase.existsByCode("KG")).thenReturn(true);

        mockMvc.perform(get(BASE + "/exists-by-code/{code}", "KG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));
    }

    @Test
    void findByName_returns200_whenPresent() throws Exception {
        when(productUnitUseCase.findNameByCode("KG")).thenReturn(Optional.of("Kilogramo"));

        mockMvc.perform(get(BASE + "/name-by-code/{code}", "KG"))
                .andExpect(status().isOk())
                .andExpect(content().string("Kilogramo"));
    }

    @Test
    void findByName_returns404_whenEmpty() throws Exception {
        when(productUnitUseCase.findNameByCode("XX")).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE + "/name-by-code/{code}", "XX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllByCode_returnsList() throws Exception {
        when(productUnitUseCase.findAllCodes()).thenReturn(List.of("KG", "LT"));

        mockMvc.perform(get(BASE + "/code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("KG"))
                .andExpect(jsonPath("$[1]").value("LT"));
    }
}