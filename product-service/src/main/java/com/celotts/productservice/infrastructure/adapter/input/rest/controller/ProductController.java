package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.domain.port.input.product.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductMapper;
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
@Tag(name = "Product API", description = "API to manage products")
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductMapper productMapper;
    private final PaginationProperties paginationProperties;

    @GetMapping("/test")
    @Operation(summary = "Test API", description = "Test endpoint to verify that the service is working.")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("¡Product Service working correctly!");
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a product in the system")
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid ProductCreateDto createDto) {
        log.info("Creating new product with code: {}", createDto.getCode());
        ProductModel created = productUseCase.createProduct(productMapper.toModel(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a product", description = "Update a product")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid ProductUpdateDto updateDto) {
        log.info("Updating product with id: {}", id);
        ProductModel updated = productUseCase.updateProduct(id, productMapper.toModel(updateDto));
        return ResponseEntity.ok(productMapper.toResponse(updated));
    }

    @GetMapping
    @Operation(summary = "List all active products", description = "List all active products")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> response = productMapper.toResponseList(
                productUseCase.getActiveProducts(Pageable.unpaged()).getContent()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated products", description = "Product list with optional pagination and filters")
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

        // Si ProductMapper tiene helper toResponsePage(page):
        // Page<ProductResponseDto> dtoPage = productMapper.toResponsePage(products);
        // Si no, mapea así:
        Page<ProductResponseDto> dtoPage = products.map(productMapper::toResponse);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Query a product by its identifier")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        ProductModel product = productUseCase.getProductById(id);
        return ResponseEntity.ok(productMapper.toResponse(product));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete logical product", description = "Delete logical product")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productUseCase.disableProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    @Operation(summary = "Delete physical product", description = "Delete physical product")
    public ResponseEntity<Void> hardDeleteProduct(@PathVariable UUID id) {
        productUseCase.hardDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "Enable product", description = "Enable product")
    public ResponseEntity<ProductResponseDto> enableProduct(@PathVariable UUID id) {
        ProductModel enabledProduct = productUseCase.enableProduct(id);
        return ResponseEntity.ok(productMapper.toResponse(enabledProduct));
    }

    @GetMapping("/inactive")
    @Operation(summary = "Get inactive products", description = "Get inactive products")
    public ResponseEntity<List<ProductResponseDto>> getInactiveProducts() {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getInactiveProducts());

        if (response.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category", description = "Get products by category")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getProductsByCategory(categoryId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}/low-stock")
    @Operation(summary = "Low stock products by category", description = "Low stock products by category")
    public ResponseEntity<List<ProductResponseDto>> getLowStockByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(
                productMapper.toResponseList(productUseCase.getLowStockByCategory(categoryId))
        );
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Products with low stock", description = "List all products with low stock")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts() {
        return ResponseEntity.ok(
                productMapper.toResponseList(productUseCase.getLowStockProducts())
        );
    }

    @GetMapping("/brand/{brandId}")
    @Operation(summary = "Products by brand", description = "List products of a brand")
    public ResponseEntity<List<ProductResponseDto>> getProductsByBrand(@PathVariable UUID brandId) {
        return ResponseEntity.ok(
                productMapper.toResponseList(productUseCase.getProductsByBrand(brandId))
        );
    }

    @GetMapping("/count")
    @Operation(summary = "Count products", description = "Returns the total number of products")
    public ResponseEntity<Long> countProducts() {
        return ResponseEntity.ok(productUseCase.countProducts());
    }

    @GetMapping("/count/active")
    @Operation(summary = "Count active products", description = "Returns the number of active products")
    public ResponseEntity<Long> countActiveProducts() {
        return ResponseEntity.ok(productUseCase.countActiveProducts());
    }

    @GetMapping("/validate-unit/{code}")
    @Operation(summary = "Validate unit code", description = "Validates if a unit code exists and obtains its name")
    public ResponseEntity<Map<String, Object>> validateUnit(@PathVariable String code) {
        Optional<String> name = productUseCase.validateUnitCode(code);
        Map<String, Object> response = new HashMap<>();
        response.put("valid", name.isPresent());
        response.put("unitName", name.orElse(null));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update stock", description = "Update the stock of a product")
    public ResponseEntity<ProductResponseDto> updateStock(@PathVariable UUID id, @RequestBody int stock) {
        ProductModel updated = productUseCase.updateStock(id, stock);
        return ResponseEntity.ok(productMapper.toResponse(updated));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get product by code", description = "Query a product by its code")
    public ResponseEntity<ProductResponseDto> getProductByCode(@PathVariable String code) {
        if (code == null || code.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        ProductModel product = productUseCase.getProductByCode(code);
        return (product == null)
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(productMapper.toResponse(product));
    }
}