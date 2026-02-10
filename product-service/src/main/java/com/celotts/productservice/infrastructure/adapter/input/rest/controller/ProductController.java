package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.domain.port.input.product.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.ListResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.response.PageResponse;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductMapper;
import com.celotts.productservice.infrastructure.config.PaginationProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    private final MessageSource messageSource;

    @GetMapping("/test")
    @Operation(summary = "Test API", description = "Test endpoint to verify that the service is working.")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok(messageSource.getMessage("app.status.ok", null, LocaleContextHolder.getLocale()));
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Create a product in the system")
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid ProductCreateDto createDto) {
        log.info("Creating new product with code: {}", createDto.getCode());
        ProductModel created = productUseCase.createProduct(productMapper.toModel(createDto));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(productMapper.toResponse(created));
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
    public ResponseEntity<ListResponse<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> response = productMapper.toResponseList(
                productUseCase.getActiveProducts(Pageable.unpaged()).getContent()
        );
        return ResponseEntity.ok(ListResponse.of(response));
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated products",
            description = "Product list with optional pagination and filters")
    public ResponseEntity<PageResponse<ProductResponseDto>> getAllProductsPaginated(
            @Valid @ModelAttribute ProductRequestDto requestDto
    ) {
        // Fallbacks si el cliente no envía valores
        int page = Optional.ofNullable(requestDto.getPage())
                .filter(p -> p >= 0)
                .orElse(paginationProperties.getDefaultPage());

        int size = Optional.ofNullable(requestDto.getSize())
                .map(s -> Math.max(1, Math.min(s, paginationProperties.getMaxSize())))
                .orElse(paginationProperties.getDefaultSize());

        String sortBy = Optional.ofNullable(requestDto.getSortBy())
                .filter(s -> !s.isBlank())
                .orElse(paginationProperties.getDefaultSort());

        String sortDir = Optional.ofNullable(requestDto.getSortDir())
                .filter(s -> !s.isBlank())
                .orElse(paginationProperties.getDefaultDirection());

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        String code = requestDto.getCode();
        String name = requestDto.getName();
        String description = requestDto.getDescription();

        Page<ProductModel> products = (code != null || name != null || description != null)
                ? productUseCase.getAllProductsWithFilters(pageable, code, name, description)
                : productUseCase.getAllProducts(pageable);

        // Envolver en PageResponse usando el mapper a DTO
        return ResponseEntity.ok(PageResponse.from(products, productMapper::toResponse));
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
    public ResponseEntity<ListResponse<ProductResponseDto>> getInactiveProducts() {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getInactiveProducts());
        return ResponseEntity.ok(ListResponse.of(response));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ListResponse<ProductResponseDto>> getProductsByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getProductsByCategory(categoryId));
        return ResponseEntity.ok(ListResponse.of(response));
    }

    @GetMapping("/category/{categoryId}/low-stock")
    public ResponseEntity<ListResponse<ProductResponseDto>> getLowStockByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getLowStockByCategory(categoryId));
        return ResponseEntity.ok(ListResponse.of(response));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ListResponse<ProductResponseDto>> getLowStockProducts() {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getLowStockProducts());
        return ResponseEntity.ok(ListResponse.of(response));
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<ListResponse<ProductResponseDto>> getProductsByBrand(@PathVariable UUID brandId) {
        List<ProductResponseDto> response =
                productMapper.toResponseList(productUseCase.getProductsByBrand(brandId));
        return ResponseEntity.ok(ListResponse.of(response));
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
    public ResponseEntity<ProductResponseDto> updateStock(@PathVariable UUID id, @RequestBody @Valid UpdateStockDto stockDto) {
        ProductModel updated = productUseCase.updateStock(id, stockDto.getStock());
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