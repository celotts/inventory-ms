package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productserviceOld.applications.service.ProductUnitService;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.controller.ProductUnitController;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit.ProductUnitResponseDto;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.productUnit.ProductUnitDtoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductUnitControllerTest {

    private static final String BASE = "/api/v1/product-units";

    @Mock private ProductUnitService productUnitService;
    @Mock private ProductUnitDtoMapper productUnitDtoMapper;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        ProductUnitController controller = new ProductUnitController(productUnitService, productUnitDtoMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /product-units -> 201 Created con body")
    void create_returns201() throws Exception {
        ProductUnitResponseDto resp = ProductUnitResponseDto.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .code("KG").name("Kilogramo").symbol("kg").enabled(true).build();

        when(productUnitService.create(any())).thenReturn(resp);

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

        verify(productUnitService, never()).create(any());
    }

    @Test
    void findAll_returnsList() throws Exception {
        var u1 = ProductUnitResponseDto.builder().id(UUID.randomUUID()).code("KG").name("Kilogramo").symbol("kg").enabled(true).build();
        var u2 = ProductUnitResponseDto.builder().id(UUID.randomUUID()).code("LT").name("Litro").symbol("l").enabled(true).build();
        when(productUnitService.findAll()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code").value("KG"))
                .andExpect(jsonPath("$[1].code").value("LT"));
    }

    @Test
    void findById_returnsDto() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000010");
        var u = ProductUnitResponseDto.builder().id(id).code("KG").name("Kilogramo").symbol("kg").enabled(true).build();
        when(productUnitService.findById(id)).thenReturn(u);

        mockMvc.perform(get(BASE + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.code").value("KG"));
    }

    @Test
    void update_returnsDto() throws Exception {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000020");
        var u = ProductUnitResponseDto.builder().id(id).code("KG").name("Kilogramo actualizado").symbol("kg").enabled(true).build();
        when(productUnitService.update(eq(id), any())).thenReturn(u);

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
        doNothing().when(productUnitService).delete(id);

        mockMvc.perform(delete(BASE + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(productUnitService).delete(id);
    }

    @Test
    void existsByCode_returnsMap() throws Exception {
        when(productUnitService.existsByCode("KG")).thenReturn(true);

        mockMvc.perform(get(BASE + "/exists-by-code/{code}", "KG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true));
    }

    @Test
    void findByName_returns200_whenPresent() throws Exception {
        when(productUnitService.findNameByCode("KG")).thenReturn(Optional.of("Kilogramo"));

        mockMvc.perform(get(BASE + "/name-by-code/{code}", "KG"))
                .andExpect(status().isOk())
                .andExpect(content().string("Kilogramo"));
    }

    @Test
    void findByName_returns404_whenEmpty() throws Exception {
        when(productUnitService.findNameByCode("XX")).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE + "/name-by-code/{code}", "XX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllByCode_returnsList() throws Exception {
        when(productUnitService.findAllCodes()).thenReturn(List.of("KG", "LT"));

        mockMvc.perform(get(BASE + "/code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]").value("KG"))
                .andExpect(jsonPath("$[1]").value("LT"));
    }
}