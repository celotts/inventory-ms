package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductTagModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTag.ProductTagUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.productTag.ProductTagRequestMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductTagControllerTest {

    private MockMvc mvc;

    @Mock
    private ProductTagUseCase useCase;

    private final ProductTagRequestMapper mapper = new ProductTagRequestMapper();
    private final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        ProductTagController controller = new ProductTagController(useCase, mapper);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    // --------- CREATE ----------
    @Test
    void create_returns_created_dto() throws Exception {
        ProductTagCreateDto body = ProductTagCreateDto.builder()
                .name("Gaming").description("Gaming related").enabled(true).createdBy("alice").build();

        ProductTagModel saved = ProductTagModel.builder()
                .id(UUID.randomUUID()).name("Gaming").description("Gaming related").enabled(true).createdBy("alice").build();

        when(useCase.create(any(ProductTagModel.class))).thenReturn(saved);

        mvc.perform(post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Gaming")))
                .andExpect(jsonPath("$.description", is("Gaming related")))
                .andExpect(jsonPath("$.enabled", is(true)));

        // opcional: verificar que mapper aplicó default/valores esperados
        ArgumentCaptor<ProductTagModel> captor = ArgumentCaptor.forClass(ProductTagModel.class);
        verify(useCase).create(captor.capture());
        // el create mapper pone enabled TRUE si viene null; aquí vino true explícito
        // solo confirmamos nombre/desc
        ProductTagModel sent = captor.getValue();
        org.assertj.core.api.Assertions.assertThat(sent.getName()).isEqualTo("Gaming");
        org.assertj.core.api.Assertions.assertThat(sent.getDescription()).isEqualTo("Gaming related");
    }

    // --------- UPDATE ----------
    @Test
    void update_returns_updated_dto() throws Exception {
        UUID id = UUID.randomUUID();
        ProductTagUpdateDto body = ProductTagUpdateDto.builder()
                .name("Updated").description("Updated desc").enabled(false).updatedBy("bob").build();

        ProductTagModel updated = ProductTagModel.builder()
                .id(id).name("Updated").description("Updated desc").enabled(false).updatedBy("bob").build();

        when(useCase.update(eq(id), any(ProductTagModel.class))).thenReturn(updated);

        mvc.perform(put("/api/v1/tags/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.name", is("Updated")))
                .andExpect(jsonPath("$.enabled", is(false)));
    }

    // --------- GET BY ID ----------
    @Test
    void get_returns_one() throws Exception {
        UUID id = UUID.randomUUID();
        ProductTagModel model = ProductTagModel.builder()
                .id(id).name("Tech").description("Tech").enabled(true).build();

        when(useCase.findById(id)).thenReturn(model);

        mvc.perform(get("/api/v1/tags/{id}", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.name", is("Tech")));
    }

    // --------- LIST (PAGEABLE) ----------
    @Test
    void list_returns_page_of_tags() throws Exception {
        ProductTagModel t1 = ProductTagModel.builder()
                .id(UUID.randomUUID()).name("Gaming").description("Gaming related").enabled(true).build();
        ProductTagModel t2 = ProductTagModel.builder()
                .id(UUID.randomUUID()).name("Tech").description("Tech related").enabled(false).build();

        Page<ProductTagModel> page = new PageImpl<>(List.of(t1, t2), PageRequest.of(0, 2), 2);
        when(useCase.findAll(any(Pageable.class))).thenReturn(page);

        mvc.perform(get("/api/v1/tags")
                        .param("page", "0")
                        .param("size", "2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", is("Gaming")))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    // --------- ENABLED LIST ----------
    @Test
    void enabled_returns_enabled_tags() throws Exception {
        ProductTagModel t1 = ProductTagModel.builder()
                .id(UUID.randomUUID()).name("On").description("on").enabled(true).build();
        when(useCase.findAllEnabled()).thenReturn(List.of(t1));

        mvc.perform(get("/api/v1/tags/enabled").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].enabled", is(true)))
                .andExpect(jsonPath("$[0].name", is("On")));
    }

    // --------- ENABLE ----------
    @Test
    void enable_returns_enabled_tag() throws Exception {
        UUID id = UUID.randomUUID();
        ProductTagModel enabled = ProductTagModel.builder()
                .id(id).name("X").enabled(true).build();

        when(useCase.enable(id)).thenReturn(enabled);

        mvc.perform(patch("/api/v1/tags/{id}/enable", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.enabled", is(true)));
    }

    // --------- DISABLE ----------
    @Test
    void disable_returns_disabled_tag() throws Exception {
        UUID id = UUID.randomUUID();
        ProductTagModel disabled = ProductTagModel.builder()
                .id(id).name("X").enabled(false).build();

        when(useCase.disable(id)).thenReturn(disabled);

        mvc.perform(patch("/api/v1/tags/{id}/disable", id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.enabled", is(false)));
    }

    // --------- DELETE ----------
    @Test
    void delete_returns_no_content() throws Exception {
        UUID id = UUID.randomUUID();

        mvc.perform(delete("/api/v1/tags/{id}", id))
                .andExpect(status().isNoContent());

        verify(useCase).delete(id);
    }
}