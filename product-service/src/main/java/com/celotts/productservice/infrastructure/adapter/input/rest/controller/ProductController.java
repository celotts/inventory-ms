package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.input.product.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductResponseMapper;
import com.celotts.productservice.infrastructure.config.PaginationProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final PaginationProperties paginationProperties;

    @GetMapping("/test")
    @Operation(summary = "Test API", description = "Endpoint de prueba para verificar que el servicio funciona.")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("¡Product Service funcionando correctamente!");
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un producto en el sistema")
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid ProductCreateDto createDto) {
        log.info("Creating new product with code: {}", createDto.getCode());
        ProductModel created = productUseCase.createProduct(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMapper.toDto(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto", description = "Actualiza un producto existente")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid ProductUpdateDto updateDto) {
        log.info("Updating product with id: {}", id);
        ProductModel updated = productUseCase.updateProduct(id, updateDto);
        return ResponseEntity.ok(responseMapper.toDto(updated));
    }

    @GetMapping
    @Operation(summary = "Obtener productos activos", description = "Lista todos los productos activos")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> response = responseMapper.toResponseDtoList(
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
        int pageNumber = page != null ? Math.max(0, page) : paginationProperties.getDefaultPage();
        int pageSize = size != null ? Math.min(Math.max(1, size), paginationProperties.getMaxSize()) : paginationProperties.getDefaultSize();
        String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : paginationProperties.getDefaultSort();
        String sortDirection = (sortDir != null && !sortDir.isBlank()) ? sortDir : paginationProperties.getDefaultDirection();

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<ProductModel> products = (code != null || name != null || description != null)
                ? productUseCase.getAllProductsWithFilters(pageable, code, name, description)
                : productUseCase.getAllProducts(pageable);

        Page<ProductModel> result = products;
        Page<ProductResponseDto> dtoPage = result.map(responseMapper::toDto);
        return ResponseEntity.ok(dtoPage);
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
        try {
            List<ProductResponseDto> response = responseMapper.toResponseDtoList(
                    productUseCase.getInactiveProducts()
            );

            if (response.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content
            }

            return ResponseEntity.ok(response); // 200 OK
        } catch (Exception e) {
            log.error("Error al obtener productos inactivos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Obtener productos por categoría", description = "Lista productos de una categoría")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDto> response =
                responseMapper.toResponseDtoList(productUseCase.getProductsByCategory(categoryId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}/low-stock")
    @Operation(summary = "Productos con stock bajo por categoría", description = "Lista productos con bajo stock en la categoría")
    public ResponseEntity<List<ProductResponseDto>> getLowStockByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDto> response = responseMapper.toResponseDtoList(productUseCase.getLowStockByCategory(categoryId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Productos con bajo stock", description = "Lista todos los productos con stock bajo")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts() {
        List<ProductResponseDto> response =
                responseMapper.toResponseDtoList(productUseCase.getLowStockProducts());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/brand/{brandId}")
    @Operation(summary = "Productos por marca", description = "Lista productos de una marca")
    public ResponseEntity<List<ProductResponseDto>> getProductsByBrand(@PathVariable UUID brandId) {
        List<ProductResponseDto> response =
                responseMapper.toResponseDtoList(productUseCase.getProductsByBrand(brandId));
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
        if (code == null || code.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        ProductModel product = productUseCase.getProductByCode(code);
        return (product == null)
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(responseMapper.toResponseDto(product));
    }
}