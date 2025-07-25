package com.celotts.productservice.applications.service;

import com.celotts.productservice.applications.usecase.ProductUseCaseImpl;
import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.product.brand.input.ProductBrandPort;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.domain.port.product.port.output.ProductRepositoryPort;
import com.celotts.productservice.domain.port.product.port.usecase.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static com.celotts.productservice.utils.ProductTestDataFactory.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {


    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private ProductUnitRepositoryPort productUnitPort;

    @Mock
    private ProductBrandPort productBrandPort;

    @Mock
    private CategoryRepositoryPort categoryRepositoryPort;

    private ProductUseCase useCase;

    @Mock
    private ProductRequestMapper productRequestMapper;

    @BeforeEach
    void setUp() {
        useCase = new ProductUseCaseImpl(
                productRepositoryPort,
                productUnitPort,
                productBrandPort,
                categoryRepositoryPort,
                productRequestMapper
        );
    }

    @Test
    void createProduct_shouldCreateProductSuccessfully() {
        // Arrange
        String code = "P001";
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        ProductCreateDto dto = ProductCreateDto.builder()
                .code(code)
                .name("Test Product")
                .description("Test Description")
                .unitCode("UNIT01")
                .brandId(brandId)
                .categoryId(categoryId)
                .minimumStock(10)
                .currentStock(20)
                .unitPrice(BigDecimal.valueOf(99.99))
                .createdBy("tester")
                .build();

        ProductModel expectedModel = ProductModel.builder()
                .id(UUID.randomUUID())
                .code(code)
                .name("Test Product")
                .description("Test Description")
                .unitCode("UNIT01")
                .brandId(brandId)
                .categoryId(categoryId)
                .minimumStock(10)
                .currentStock(20)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .createdBy("tester")
                .build();

        when(productRepositoryPort.findByCode(code)).thenReturn(Optional.empty());
        when(productUnitPort.existsByCode("UNIT01")).thenReturn(true);
        when(productBrandPort.existsById(brandId)).thenReturn(true);
        when(categoryRepositoryPort.existsById(categoryId)).thenReturn(true);
        when(productRequestMapper.toModel(dto)).thenReturn(expectedModel);
        when(productRepositoryPort.save(expectedModel)).thenReturn(expectedModel);

        // Act
        ProductModel result = useCase.createProduct(dto);

        // Assert
        assertNotNull(result);
        assertEquals(code, result.getCode());
        verify(productRepositoryPort).save(expectedModel);
    }

    @Test
    void createProduct_shouldThrowProductAlreadyExistsExceptionWhenCodeAlreadyExists() {
        ProductCreateDto dto = ProductCreateDto.builder()
                .code("EXISTING_CODE")
                .name("New Product")
                .createdBy("admin")
                .unitCode("UNIT01")
                .brandId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(BigDecimal.valueOf(99.99))
                .enabled(true)
                .build();

        ProductModel existingProduct = ProductModel.builder()
                .id(UUID.randomUUID())
                .code("EXISTING_CODE")
                .name("Existing Product")
                .build();

        when(productRepositoryPort.findByCode(dto.getCode())).thenReturn(Optional.of(existingProduct));

        ProductAlreadyExistsException exception = assertThrows(ProductAlreadyExistsException.class, () -> {
            useCase.createProduct(dto);
        });

        assertTrue(exception.getMessage().contains("EXISTING_CODE"));
        verify(productRepositoryPort).findByCode(dto.getCode());
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    void updateProduct_shouldThrowProductNotFoundExceptionWhenProductDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();

        when(productRepositoryPort.findById(nonExistentId)).thenReturn(Optional.empty());

        ProductUpdateDto dto = ProductUpdateDto.builder().build();
        dto.setCode("NEWCODE999");

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            useCase.updateProduct(nonExistentId, dto);
        });

        assertTrue(exception.getMessage().contains(nonExistentId.toString()));
        verify(productRepositoryPort).findById(nonExistentId);
        verify(productRepositoryPort, never()).save(any());
    }

    @Test
    void updateProduct_shouldUpdateProductSuccessfully() {
        UUID productId = UUID.randomUUID();

        ProductModel existingProduct = ProductModel.builder()
                .id(productId)
                .code("CODE123")
                .name("Original Name")
                .unitCode("UNIT01")
                .brandId(UUID.randomUUID())
                .categoryId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .createdBy("tester")
                .build();

        ProductUpdateDto dto = ProductUpdateDto.builder()
                .code("UPDATEDCODE")
                .name("Updated Name")
                .unitCode("UNIT01")
                .brandId(existingProduct.getBrandId())
                .categoryId(existingProduct.getCategoryId())
                .build();
        dto.setUpdatedBy("test-user");
        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productUnitPort.existsByCode("UNIT01")).thenReturn(true);
        when(productBrandPort.existsById(existingProduct.getBrandId())).thenReturn(true);
        when(categoryRepositoryPort.existsById(existingProduct.getCategoryId())).thenReturn(true);

        ProductModel updatedProduct = ProductModel.builder()
                .id(productId)
                .code(dto.getCode())
                .name(dto.getName())
                .unitCode(dto.getUnitCode())
                .brandId(dto.getBrandId())
                .categoryId(dto.getCategoryId())
                .createdAt(existingProduct.getCreatedAt())
                .createdBy(existingProduct.getCreatedBy())
                .updatedAt(LocalDateTime.now())
                .updatedBy(dto.getUpdatedBy()) // ← cambia aquí
                .build();

        doAnswer(invocation -> {
            ProductModel target = invocation.getArgument(0);
            ProductUpdateDto source = invocation.getArgument(1);

            target.setCode(source.getCode());
            target.setName(source.getName());
            target.setUnitCode(source.getUnitCode());
            target.setBrandId(source.getBrandId());
            target.setCategoryId(source.getCategoryId());
            target.setUpdatedBy(source.getUpdatedBy());
            target.setUpdatedAt(LocalDateTime.now());

            return null;
        }).when(productRequestMapper).updateModelFromDto(any(ProductModel.class), any(ProductUpdateDto.class));

        System.out.println("DTO enviado updatedBy: " + dto.getUpdatedBy());

        when(productRepositoryPort.save(any(ProductModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductModel result = useCase.updateProduct(productId, dto);

        assertNotNull(result);

        ArgumentCaptor<ProductModel> captor = ArgumentCaptor.forClass(ProductModel.class);
        verify(productRepositoryPort).save(captor.capture());

        ProductModel savedProduct = captor.getValue();
        assertEquals(dto.getCode(), savedProduct.getCode());
        assertEquals(dto.getName(), savedProduct.getName());
        assertEquals(dto.getUnitCode(), savedProduct.getUnitCode());
        assertEquals(dto.getBrandId(), savedProduct.getBrandId());
        assertEquals(dto.getCategoryId(), savedProduct.getCategoryId());
        assertEquals(existingProduct.getCreatedAt(), savedProduct.getCreatedAt());
        assertEquals(existingProduct.getCreatedBy(), savedProduct.getCreatedBy());
        assertNotNull(savedProduct.getUpdatedAt(), "El campo updatedAt no debe ser null");
        System.out.println("DEBUG - Valor actual de updatedBy: " + savedProduct.getUpdatedBy());
        System.out.println(">>> updatedBy actual: " + savedProduct.getUpdatedBy());
        System.out.println("Valor esperado: " + dto.getUpdatedBy());
        System.out.println("Valor actual: " + savedProduct.getUpdatedBy());
        assertEquals("test-user", savedProduct.getUpdatedBy(), "El campo updatedBy no coincide");
    }

    @Test
    void getProductById_shouldReturnProductWhenExists() {
        UUID id = UUID.randomUUID();
        ProductModel expected = ProductModel.builder().id(id).code("ABC123").build();
        when(productRepositoryPort.findById(id)).thenReturn(Optional.of(expected));

        ProductModel result = useCase.getProductById(id);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getCode(), result.getCode());
    }

    @Test
    void getProductById_shouldThrowExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(productRepositoryPort.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> useCase.getProductById(id));
    }

    @Test
    void existsById_shouldReturnTrueIfExists() {
        UUID id = UUID.randomUUID();
        when(productRepositoryPort.existsById(id)).thenReturn(true);

        assertTrue(useCase.existsById(id));
    }

    @Test
    void existsByCode_shouldReturnTrueIfProductExists() {
        when(productRepositoryPort.findByCode("CODE123")).thenReturn(Optional.of(ProductModel.builder().build()));

        assertTrue(useCase.existsByCode("CODE123"));
    }

    @Test
    void getAll_shouldReturnListOfProducts() {
        when(productRepositoryPort.findAll()).thenReturn(List.of(ProductModel.builder().code("P1").build()));

        List<ProductModel> result = useCase.getAll();

        assertEquals(1, result.size());
        assertEquals("P1", result.get(0).getCode());
    }
    @Test
    void shouldEnableProduct() {
        UUID id = UUID.randomUUID();
        ProductModel product = ProductModel.builder().id(id).enabled(false).build();

        when(productRepositoryPort.findById(id)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(any())).thenAnswer(i -> i.getArgument(0));

        ProductModel result = useCase.enableProduct(id);

        assertTrue(result.getEnabled());
        verify(productRepositoryPort).save(product.withEnabled(true));
    }

    @Test
    void shouldDisableProduct() {
        UUID id = UUID.randomUUID();
        ProductModel product = ProductModel.builder().id(id).enabled(true).build();

        when(productRepositoryPort.findById(id)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(any())).thenAnswer(i -> i.getArgument(0));

        ProductModel result = useCase.disableProduct(id);

        assertFalse(result.getEnabled(), "El producto debe estar deshabilitado");
        verify(productRepositoryPort).save(product.withEnabled(false));
    }


    @Test
    void shouldUpdateProductStockSuccessfully() {
        UUID id = UUID.randomUUID();
        ProductModel product = ProductModel.builder()
                .id(id)
                .currentStock(10)
                .build();

        when(productRepositoryPort.findById(id)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ProductModel result = useCase.updateStock(id, 25);

        assertNotNull(result);
        assertEquals(25, result.getCurrentStock());
        verify(productRepositoryPort).save(product.withCurrentStock(25));
    }

    @Test
    void shouldReturnProductByCode() {
        String code = "P-001";
        ProductModel product = ProductModel.builder().id(UUID.randomUUID()).code(code).build();

        when(productRepositoryPort.findByCode(code)).thenReturn(Optional.of(product));

        ProductModel result = useCase.getProductByCode(code);

        assertNotNull(result);
        assertEquals(code, result.getCode());
        verify(productRepositoryPort).findByCode(code);
    }

    @Test
    void shouldThrowWhenProductCodeNotFound() {
        String code = "INVALID-CODE";

        when(productRepositoryPort.findByCode(code)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> useCase.getProductByCode(code));
        verify(productRepositoryPort).findByCode(code);
    }

    @Test
    void shouldHardDeleteProductById() {
        UUID id = UUID.randomUUID();

        doNothing().when(productRepositoryPort).deleteById(id);

        useCase.hardDeleteProduct(id);

        verify(productRepositoryPort).deleteById(id);
    }

    @Test
    void shouldGetAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductModel> products = List.of(ProductModel.builder().id(UUID.randomUUID()).build());
        when(productRepositoryPort.findAll(pageable)).thenReturn(new PageImpl<>(products));

        Page<ProductModel> result = useCase.getAllProducts(pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepositoryPort).findAll(pageable);
    }

    @Test
    void shouldGetAllProductsWithFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductModel> products = List.of(ProductModel.builder().id(UUID.randomUUID()).build());
        when(productRepositoryPort.findAllWithFilters(pageable, "P01", "Test", "Desc"))
                .thenReturn(new PageImpl<>(products));

        Page<ProductModel> result = useCase.getAllProductsWithFilters(pageable, "P01", "Test", "Desc");

        assertEquals(1, result.getTotalElements());
        verify(productRepositoryPort).findAllWithFilters(pageable, "P01", "Test", "Desc");
    }

    @Test
    void shouldGetActiveProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ProductModel> products = List.of(ProductModel.builder().enabled(true).build());
        when(productRepositoryPort.findByEnabled(true, pageable)).thenReturn(new PageImpl<>(products));

        Page<ProductModel> result = useCase.getActiveProducts(pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepositoryPort).findByEnabled(true, pageable);
    }

    @Test
    void shouldGetInactiveProducts() {
        List<ProductModel> products = List.of(ProductModel.builder().enabled(false).build());
        when(productRepositoryPort.findByEnabled(eq(false), eq(Pageable.unpaged()))).thenReturn(new PageImpl<>(products));

        List<ProductModel> result = useCase.getInactiveProducts();

        assertEquals(1, result.size());
        verify(productRepositoryPort).findByEnabled(false, Pageable.unpaged());
    }

    @Test
    void shouldGetProductsByCategory() {
        UUID categoryId = UUID.randomUUID();
        when(productRepositoryPort.findByCategoryId(categoryId)).thenReturn(List.of(ProductModel.builder().build()));

        List<ProductModel> result = useCase.getProductsByCategory(categoryId);

        assertEquals(1, result.size());
        verify(productRepositoryPort).findByCategoryId(categoryId);
    }

    @Test
    void shouldGetLowStockByCategory() {
        UUID categoryId = UUID.randomUUID();
        ProductModel product = ProductModel.builder().minimumStock(10).currentStock(5).build();
        when(productRepositoryPort.findByCategoryId(categoryId)).thenReturn(List.of(product));

        List<ProductModel> result = useCase.getLowStockByCategory(categoryId);

        assertEquals(1, result.size());
        assertTrue(result.get(0).lowStock());
    }

    @Test
    void shouldGetLowStockProducts() {
        ProductModel product = ProductModel.builder().minimumStock(10).currentStock(5).build();
        when(productRepositoryPort.findAll()).thenReturn(List.of(product));

        List<ProductModel> result = useCase.getLowStockProducts();

        assertEquals(1, result.size());
        assertTrue(result.get(0).lowStock());
    }

    @Test
    void shouldGetProductsByBrand() {
        UUID brandId = UUID.randomUUID();
        when(productRepositoryPort.findByBrandId(brandId)).thenReturn(List.of(ProductModel.builder().build()));

        List<ProductModel> result = useCase.getProductsByBrand(brandId);

        assertEquals(1, result.size());
        verify(productRepositoryPort).findByBrandId(brandId);
    }

    @Test
    void shouldCountProducts() {
        when(productRepositoryPort.findAll()).thenReturn(List.of(ProductModel.builder().build()));

        long result = useCase.countProducts();

        assertEquals(1, result);
    }

    @Test
    void shouldCountActiveProducts() {
        Page<ProductModel> page = new PageImpl<>(List.of(ProductModel.builder().enabled(true).build()));
        when(productRepositoryPort.findByEnabled(eq(true), eq(Pageable.unpaged()))).thenReturn(page);

        long result = useCase.countActiveProducts();

        assertEquals(1, result);
    }

    @Test
    void shouldReturnUnitNameWhenUnitCodeIsValid() {
        when(productUnitPort.existsByCode("U01")).thenReturn(true);
        when(productUnitPort.findNameByCode("U01")).thenReturn(Optional.of("Kilogramo"));

        Optional<String> result = useCase.validateUnitCode("U01");

        assertTrue(result.isPresent());
        assertEquals("Kilogramo", result.get());
    }

    @Test
    void shouldReturnEmptyWhenUnitCodeIsInvalid() {
        when(productUnitPort.existsByCode("X99")).thenReturn(false);

        Optional<String> result = useCase.validateUnitCode("X99");

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowException_WhenUnitCodeIsInvalid() {
        // Arrange
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        ProductCreateDto dto = ProductCreateDto.builder()
                .code("P001")
                .unitCode("INVALID_UNIT")
                .brandId(brandId)
                .categoryId(categoryId)
                .minimumStock(10)
                .currentStock(20)
                .unitPrice(BigDecimal.valueOf(99.99))
                .createdBy("test-user")
                .build();

        when(productRepositoryPort.findByCode(dto.getCode())).thenReturn(Optional.empty());
        when(productUnitPort.existsByCode(dto.getUnitCode())).thenReturn(false);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> useCase.createProduct(dto));

        // Verificaciones
        verify(productUnitPort).existsByCode(dto.getUnitCode());
        verify(productBrandPort, never()).existsById(any());
        verify(categoryRepositoryPort, never()).existsById(any());
    }

    @Test
    void shouldThrowException_WhenBrandIdIsInvalid() {
        // Arrange
        UUID brandId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        ProductCreateDto dto = ProductCreateDto.builder()
                .code("P002")
                .unitCode("UNIT01")
                .brandId(brandId)
                .categoryId(categoryId)
                .minimumStock(10)
                .currentStock(50)
                .unitPrice(BigDecimal.valueOf(100.0))
                .createdBy("test-user")
                .build();

        when(productRepositoryPort.findByCode(dto.getCode())).thenReturn(Optional.empty());
        when(productUnitPort.existsByCode(dto.getUnitCode())).thenReturn(true);
        when(productBrandPort.existsById(brandId)).thenReturn(false);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> useCase.createProduct(dto));

        verify(productUnitPort).existsByCode(dto.getUnitCode());
        verify(productBrandPort).existsById(brandId);
        verify(categoryRepositoryPort, never()).existsById(any());
    }

    @Test
    void shouldThrowException_WhenCategoryIdIsInvalid() {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        UUID brandId = UUID.randomUUID();
        ProductCreateDto dto = ProductCreateDto.builder()
                .code("P003")
                .unitCode("UNIT01")
                .brandId(brandId)
                .categoryId(categoryId)
                .minimumStock(5)
                .currentStock(10)
                .unitPrice(BigDecimal.valueOf(49.99))
                .createdBy("test-user")
                .build();

        when(productRepositoryPort.findByCode(dto.getCode())).thenReturn(Optional.empty());
        when(productUnitPort.existsByCode(dto.getUnitCode())).thenReturn(true);
        when(productBrandPort.existsById(brandId)).thenReturn(true);
        when(categoryRepositoryPort.existsById(categoryId)).thenReturn(false);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> useCase.createProduct(dto));

        verify(productUnitPort).existsByCode(dto.getUnitCode());
        verify(productBrandPort).existsById(brandId);
        verify(categoryRepositoryPort).existsById(categoryId);
    }

    @Test
    void shouldInstantiateProductUseCaseImplConstructorDirectly() {
        // Arrange
        ProductRepositoryPort mockProductRepositoryPort = mock(ProductRepositoryPort.class);
        ProductUnitRepositoryPort mockProductUnitPort = mock(ProductUnitRepositoryPort.class);
        ProductBrandPort mockProductBrandPort = mock(ProductBrandPort.class);
        CategoryRepositoryPort mockCategoryRepositoryPort = mock(CategoryRepositoryPort.class);
        ProductRequestMapper mockProductRequestMapper = mock(ProductRequestMapper.class);

        // Act
        ProductUseCaseImpl useCase = new ProductUseCaseImpl(
                mockProductRepositoryPort,
                mockProductUnitPort,
                mockProductBrandPort,
                mockCategoryRepositoryPort,
                mockProductRequestMapper
        );

        // Assert
        assertNotNull(useCase);
    }

    @Test
    void shouldThrowExceptionWhenDisablingNonExistentProduct() {
        UUID nonexistentId = UUID.randomUUID();
        when(productRepositoryPort.findById(nonexistentId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> useCase.disableProduct(nonexistentId));

        verify(productRepositoryPort).findById(nonexistentId);
    }

    @Test
    void shouldThrowExceptionWhenEnablingNonExistentProduct() {
        UUID id = UUID.randomUUID();
        when(productRepositoryPort.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> useCase.enableProduct(id));

        verify(productRepositoryPort).findById(id);
    }
}