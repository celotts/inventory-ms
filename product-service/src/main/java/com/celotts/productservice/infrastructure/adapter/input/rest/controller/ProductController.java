package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.product.port.usecase.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.allowed-origin:*}")
@Slf4j
@Tag(name = "Product API", description = "API para gestionar productos")
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductResponseMapper responseMapper;

    @Value("${app.pagination.default-page:0}")
    private int defaultPage;

    @Value("${app.pagination.default-size:10}")
    private int defaultSize;

    @Value("${app.pagination.max-size:100}")
    private int maxSize;

    @Value("${app.pagination.default-sort:createdAt}")
    private String defaultSort;

    @Value("${app.pagination.default-direction:desc}")
    private String defaultDirection;

    @GetMapping("/test")
    @Operation(summary = "Test API", description = "Endpoint de prueba para verificar que el servicio funciona.")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("¡Product Service funcionando correctamente!");
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un producto en el sistema")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto requestDTO) {

        log.info("Creating new product with code: {}", requestDTO.getCode());

        ProductModel created = productUseCase.createProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseMapper.toDto(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto", description = "Actualiza un producto existente")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid ProductRequestDto requestDTO) {
        ProductModel updated = productUseCase.updateProduct(id, requestDTO);
        return ResponseEntity.ok(responseMapper.toDto(updated));
    }

    @GetMapping
    @Operation(summary = "Obtener productos activos", description = "Lista todos los productos activos")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> response = ProductResponseMapper.toResponseDTOList(
                productUseCase.getActiveProducts(Pageable.unpaged()).getContent()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Obtener productos paginados", description = "Lista productos con paginación y filtros opcionales")
    public ResponseEntity<Page<ProductResponseDto>> getAllProductsPaginated(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description
    ) {
        int pageNumber = page != null ? Math.max(0, page) : defaultPage;
        int pageSize = size != null ? Math.min(Math.max(1, size), maxSize) : defaultSize;
        String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : defaultSort;
        String sortDirection = (sortDir != null && !sortDir.isBlank()) ? sortDir : defaultDirection;

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<ProductModel> products = (code != null || name != null || description != null)
                ? productUseCase.getAllProductsWithFilters(pageable, code, name, description)
                : productUseCase.getAllProducts(pageable);

        return ResponseEntity.ok(products.map(responseMapper::toDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Consulta un producto por su identificador")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        ProductModel product = productUseCase.getProductById(id);
        return ResponseEntity.ok(responseMapper.toDto(product));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto lógico", description = "Desactiva el producto (borrado lógico)")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productUseCase.disableProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    @Operation(summary = "Eliminar producto físico", description = "Elimina el producto de forma definitiva")
    public ResponseEntity<Void> hardDeleteProduct(@PathVariable UUID id) {
        productUseCase.hardDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "Habilitar producto", description = "Habilita un producto por su ID")
    public ResponseEntity<ProductResponseDto> enableProduct(@PathVariable UUID id) {
        ProductModel enabledProduct = productUseCase.enableProduct(id);
        return ResponseEntity.ok(responseMapper.toDto(enabledProduct));
    }

    @GetMapping("/inactive")
    @Operation(summary = "Obtener productos inactivos", description = "Lista los productos deshabilitados")
    public ResponseEntity<List<ProductResponseDto>> getInactiveProducts() {
        List<ProductResponseDto> response = ProductResponseMapper.toResponseDTOList(
                productUseCase.getInactiveProducts()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Obtener productos por categoría", description = "Lista productos de una categoría")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDto> response =
                ProductResponseMapper.toResponseDtoList(productUseCase.getProductsByCategory(categoryId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}/low-stock")
    @Operation(summary = "Productos con stock bajo por categoría", description = "Lista productos con bajo stock en la categoría")
    public ResponseEntity<List<ProductResponseDto>> getLowStockByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDto> response = ProductResponseMapper.toResponseDtoList(productUseCase.getLowStockByCategory(categoryId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Productos con bajo stock", description = "Lista todos los productos con stock bajo")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts() {
        List<ProductResponseDto> response =
                ProductResponseMapper.toResponseDtoList(productUseCase.getLowStockProducts());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/brand/{brandId}")
    @Operation(summary = "Productos por marca", description = "Lista productos de una marca")
    public ResponseEntity<List<ProductResponseDto>> getProductsByBrand(@PathVariable UUID brandId) {
        List<ProductResponseDto> response =
                ProductResponseMapper.toResponseDtoList(productUseCase.getProductsByBrand(brandId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    @Operation(summary = "Contar productos", description = "Devuelve el número total de productos")
    public ResponseEntity<Long> countProducts() {
        return ResponseEntity.ok(productUseCase.countProducts());
    }

    @GetMapping("/count/active")
    @Operation(summary = "Contar productos activos", description = "Devuelve el número de productos activos")
    public ResponseEntity<Long> countActiveProducts() {
        return ResponseEntity.ok(productUseCase.countActiveProducts());
    }

    @GetMapping("/validate-unit/{code}")
    @Operation(summary = "Validar código de unidad", description = "Valida si un código de unidad existe y obtiene su nombre")
    public ResponseEntity<Map<String, Object>> validateUnit(@PathVariable String code) {
        Optional<String> name = productUseCase.validateUnitCode(code);
        Map<String, Object> response = new HashMap<>();
        response.put("valid", name.isPresent());
        response.put("unitName", name.orElse(null));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Actualizar stock", description = "Actualiza el stock de un producto")
    public ResponseEntity<ProductResponseDto> updateStock(@PathVariable UUID id, @RequestBody int stock) {
        ProductResponseDto response = responseMapper.toDto(
                productUseCase.updateStock(id, stock)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Obtener producto por código", description = "Consulta un producto por su código")
    public ResponseEntity<ProductResponseDto> getProductByCode(@PathVariable String code) {
        ProductModel product = productUseCase.getProductByCode(code);
        return ResponseEntity.ok(responseMapper.toDto(product));
    }

}