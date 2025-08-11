package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.product.port.usecase.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.TestConfiguration;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductUseCase productUseCase;

    @Mock
    private ProductResponseMapper responseMapper;

    @Mock
    private ProductRequestMapper productRequestMapper;

    // PaginationProperties is required for paginated endpoint; inject mock if needed
    @Mock
    private com.celotts.productservice.infrastructure.config.PaginationProperties paginationProperties;

    @BeforeEach
    void setup() {
        // Inject paginationProperties if ProductController has it as a field
        ReflectionTestUtils.setField(productController, "paginationProperties", paginationProperties);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getInactiveProducts_shouldReturn200WhenProductsExist() throws Exception {
        ProductModel mockProduct = new ProductModel();
        com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto mockDto =
                com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto.builder()
                        .id(UUID.randomUUID())
                        .code("P001")
                        .name("Mock Product")
                        .description("Mock Description")
                        .enabled(false)
                        .currentStock(0)
                        .minimumStock(5)
                        .unitPrice(java.math.BigDecimal.valueOf(99.99))
                        .createdAt(java.time.LocalDateTime.now())
                        .updatedAt(java.time.LocalDateTime.now())
                        .build();

        Mockito.when(productUseCase.getInactiveProducts()).thenReturn(List.of(mockProduct));
        Mockito.when(responseMapper.toResponseDtoList(List.of(mockProduct))).thenReturn(List.of(mockDto));

        mockMvc.perform(get("/api/v1/products/inactive"))
                .andExpect(status().isOk());
    }

    @Test
    void getInactiveProducts_shouldReturn204WhenNoProductsExist() throws Exception {
        Mockito.when(productUseCase.getInactiveProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/products/inactive"))
                .andExpect(status().isNoContent());
    }

    /*@Test
    void getProductsByCategory_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.getProductsByCategory(Mockito.any()))
                .thenReturn(List.of(new ProductModel()));

        mockMvc.perform(get("/api/v1/products/category/" + UUID.randomUUID()))
                .andExpect(status().isOk());
    } */

    @Test
    void getProductsByCategory_shouldReturnOk() throws Exception {
        UUID categoryId = UUID.randomUUID();
        ProductModel product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("CODE1")
                .name("Product 1")
                .enabled(true)
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .enabled(true)
                .build();

        Mockito.when(productUseCase.getProductsByCategory(categoryId)).thenReturn(List.of(product));
        Mockito.when(responseMapper.toResponseDtoList(List.of(product))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/products/category/" + categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("CODE1"))
                .andExpect(jsonPath("$[0].enabled").value(true));
    }

    @Test
    void getLowStockByCategory_shouldReturnOk() throws Exception {
        UUID categoryId = UUID.randomUUID();
        ProductModel product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("LOW1")
                .currentStock(2)
                .minimumStock(5)
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .currentStock(2)
                .minimumStock(5)
                .build();

        Mockito.when(productUseCase.getLowStockByCategory(categoryId)).thenReturn(List.of(product));
        Mockito.when(responseMapper.toResponseDtoList(List.of(product))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/products/category/" + categoryId + "/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("LOW1"))
                .andExpect(jsonPath("$[0].currentStock").value(2))
                .andExpect(jsonPath("$[0].minimumStock").value(5));
    }

    @Test
    void getLowStockProducts_shouldReturnOk() throws Exception {
        ProductModel product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("LOW2")
                .currentStock(1)
                .minimumStock(4)
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .currentStock(1)
                .minimumStock(4)
                .build();

        Mockito.when(productUseCase.getLowStockProducts()).thenReturn(List.of(product));
        Mockito.when(responseMapper.toResponseDtoList(List.of(product))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/products/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("LOW2"))
                .andExpect(jsonPath("$[0].currentStock").value(1));
    }

    @Test
    void getProductsByBrand_shouldReturnOk() throws Exception {
        UUID brandId = UUID.randomUUID();
        ProductModel product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("BRAND1")
                .name("Brand Product")
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(product.getId())
                .code("BRAND1")
                .name("Brand Product")
                .build();

        Mockito.when(productUseCase.getProductsByBrand(brandId)).thenReturn(List.of(product));
        Mockito.when(responseMapper.toResponseDtoList(List.of(product))).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/products/brand/" + brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("BRAND1"))
                .andExpect(jsonPath("$[0].name").value("Brand Product"));
    }

    @Test
    void countProducts_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.countProducts()).thenReturn(5L);

        mockMvc.perform(get("/api/v1/products/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void countActiveProducts_shouldReturnOk() throws Exception {
        Mockito.when(productUseCase.countActiveProducts()).thenReturn(3L);

        mockMvc.perform(get("/api/v1/products/count/active"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void validateUnitCode_shouldReturnValid() throws Exception {
        Mockito.when(productUseCase.validateUnitCode("UNIT1"))
                .thenReturn(Optional.of("Unit 1"));

        mockMvc.perform(get("/api/v1/products/validate-unit/UNIT1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.unitName").value("Unit 1"));
    }

    @Test
    void updateStock_shouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        ProductModel product = ProductModel.builder().id(id).currentStock(10).build();

        Mockito.when(productUseCase.updateStock(Mockito.eq(id), Mockito.anyInt()))
                .thenReturn(product);

        mockMvc.perform(patch("/api/v1/products/" + id + "/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("15"))
                .andExpect(status().isOk());
    }

    @Test
    void updateStock_shouldReturnBadRequest_whenInvalidStockValue() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(patch("/api/v1/products/" + id + "/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"invalid\"")) // sin comillas, causa error de deserialización
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProductByCode_shouldReturnOk() throws Exception {
        ProductModel product = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("CODE123")
                .name("Product X")
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(product.getId())
                .code("CODE123")
                .name("Product X")
                .build();

        Mockito.when(productUseCase.getProductByCode("CODE123")).thenReturn(product);
        Mockito.when(responseMapper.toResponseDto(product)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/products/code/CODE123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("CODE123"))
                .andExpect(jsonPath("$.name").value("Product X"));
    }

    @Test
    void getProductByCode_shouldReturnBadRequestWhenCodeIsBlank() throws Exception {
        mockMvc.perform(get("/api/v1/products/code/"))
                .andExpect(status().isNotFound()); // porque /code/ sin valor no mapea correctamente
    }

    @Test
    void testEndpoint_shouldReturnOkMessage() throws Exception {
        mockMvc.perform(get("/api/v1/products/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("¡Product Service funcionando correctamente!"));
    }

    @Test
    void createProduct_shouldReturnCreated() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();

        ProductModel createdModel = ProductModel.builder()
                .id(productId)
                .code("NEW1")
                .name("New Product")
                .categoryId(categoryId)
                .brandId(brandId)
                .unitCode("UNIT1")
                .minimumStock(5)
                .currentStock(10)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .createdBy("admin")
                .updatedBy("admin")
                .build();

        ProductResponseDto responseDto = ProductResponseDto.builder()
                .id(productId)
                .code("NEW1")
                .name("New Product")
                .enabled(true)
                .build();

        Mockito.when(productUseCase.createProduct(Mockito.any())).thenReturn(createdModel);
        Mockito.when(responseMapper.toDto(createdModel)).thenReturn(responseDto);

        String json = String.format("""
        {
          "code": "NEW1",
          "name": "New Product",
          "description": "A sample product",
          "categoryId": "%s",
          "unitCode": "UNIT1",
          "brandId": "%s",
          "minimumStock": 5,
          "currentStock": 10,
          "unitPrice": 99.99,
          "enabled": true,
          "createdBy": "admin",
          "updatedBy": "admin"
        }
    """, categoryId, brandId);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("NEW1"))
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void deleteProduct_shouldReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/products/" + id))
                .andExpect(status().isNoContent());

        Mockito.verify(productUseCase).disableProduct(id);
    }

    @Test
    void hardDeleteProduct_shouldReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/products/" + id + "/hard"))
                .andExpect(status().isNoContent());

        Mockito.verify(productUseCase).hardDeleteProduct(id);
    }

    @Test
    void enableProduct_shouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        ProductModel enabledProduct = ProductModel.builder()
                .id(id)
                .code("P123")
                .name("Enabled Product")
                .build();

        ProductResponseDto responseDto = ProductResponseDto.builder()
                .id(id)
                .code("P123")
                .name("Enabled Product")
                .build();

        Mockito.when(productUseCase.enableProduct(id)).thenReturn(enabledProduct);
        Mockito.when(responseMapper.toDto(enabledProduct)).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/products/" + id + "/enable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("P123"));
    }

    @Test
    void getProductById_shouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        ProductModel product = ProductModel.builder()
                .id(id)
                .code("P999")
                .name("Product ID")
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(id)
                .code("P999")
                .name("Product ID")
                .build();

        Mockito.when(productUseCase.getProductById(id)).thenReturn(product);
        Mockito.when(responseMapper.toDto(product)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/products/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("P999"));
    }

    @Test
    void getAllProductsPaginated_shouldReturnPageWithoutFilters() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductModel product = ProductModel.builder()
                .id(productId)
                .code("PROD001")
                .name("Paginated Product")
                .build();

        ProductResponseDto dto = ProductResponseDto.builder()
                .id(productId)
                .code("PROD001")
                .name("Paginated Product")
                .build();

        Page<ProductModel> modelPage = new PageImpl<>(
                List.of(product),
                org.springframework.data.domain.PageRequest.of(0, 10),
                1L
        );

        Mockito.when(productUseCase.getAllProducts(Mockito.any(Pageable.class))).thenReturn(modelPage);
        Mockito.when(responseMapper.toDto(product)).thenReturn(dto);
        Mockito.when(paginationProperties.getDefaultPage()).thenReturn(0);
        Mockito.when(paginationProperties.getDefaultSize()).thenReturn(10);
        Mockito.when(paginationProperties.getMaxSize()).thenReturn(100);
        Mockito.when(paginationProperties.getDefaultSort()).thenReturn("name");
        Mockito.when(paginationProperties.getDefaultDirection()).thenReturn("asc");

        mockMvc.perform(get("/api/v1/products/paginated")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("sortDir", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(productId.toString()))
                .andExpect(jsonPath("$.content[0].code").value("PROD001"))
                .andExpect(jsonPath("$.content[0].name").value("Paginated Product"))
                .andExpect(jsonPath("$.number").value(0)) // número de página
                .andExpect(jsonPath("$.size").value(10)) // tamaño de página // tamaño del contenido
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}
