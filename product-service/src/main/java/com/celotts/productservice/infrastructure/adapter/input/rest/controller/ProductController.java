package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.product.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductResponseMapper responseMapper;
    private final ProductRequestMapper productRequestMapper;

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
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("¡Product Service funcionando correctamente!");
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductModel requestDTO) {
        log.info("Creating new product with code: {}", requestDTO.getCode());
        ProductModel createdProduct = productUseCase.createProduct(requestDTO);
        ProductResponseDTO response = responseMapper.toDto(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/validate-unit/{code}")
    public ResponseEntity<String> validateUnitCode(@PathVariable String code) {
        return productUseCase.validateUnitCode(code)
                .map(name -> ResponseEntity.ok("Valid unit: " + name))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> response = productUseCase.getAllProducts().stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProductsPaginated(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {

        int pageNumber = page != null ? Math.max(0, page) : defaultPage;
        int pageSize = size != null ? Math.min(Math.max(1, size), maxSize) : defaultSize;
        String sortField = (sortBy != null && !sortBy.isBlank()) ? sortBy : defaultSort;
        String sortDirection = (sortDir != null && !sortDir.isBlank()) ? sortDir : defaultDirection;

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<ProductModel> products =
                (code != null || name != null || description != null) ?
                        productUseCase.getAllProductsWithFilters(pageable, code, name, description) :
                        productUseCase.getAllProducts(pageable);

        return ResponseEntity.ok(products.map(responseMapper::toDto));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProductResponseDTO> getProductByCode(@PathVariable String code) {
        ProductModel product = productUseCase.getProductByCode(code);
        return ResponseEntity.ok(responseMapper.toDto(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable UUID id) {
        ProductModel product = productUseCase.getProductById(id);
        return ResponseEntity.ok(responseMapper.toDto(product));
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDTO>> getActiveProducts() {
        List<ProductResponseDTO> response = productUseCase.getActiveProducts(Pageable.unpaged())
                .getContent().stream().map(responseMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<ProductResponseDTO>> getInactiveProducts() {
        List<ProductResponseDTO> response = productUseCase.getInactiveProducts().stream()
                .map(responseMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStockProducts() {
        List<ProductResponseDTO> response = productUseCase.getLowStockProducts().stream()
                .map(responseMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDTO> response = productUseCase.getProductsByCategory(categoryId).stream()
                .map(responseMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStockByCategory(@PathVariable UUID categoryId) {
        List<ProductResponseDTO> response = productUseCase.getLowStockByCategory(categoryId).stream()
                .map(responseMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByBrand(@PathVariable UUID brandId) {
        List<ProductResponseDTO> response = productUseCase.getProductsByBrand(brandId).stream()
                .map(responseMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProducts() {
        return ResponseEntity.ok(productUseCase.countProducts());
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveProducts() {
        return ResponseEntity.ok(productUseCase.countActiveProducts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductModel model = productRequestMapper.toModel(requestDTO);
        ProductModel updated = productUseCase.updateProduct(id, model);
        return ResponseEntity.ok(responseMapper.toDto(updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> partialUpdateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductUpdateDTO updateDTO) {
        ProductModel existing = productUseCase.getProductById(id);
        ProductModel updated = productUseCase.updateProduct(id, convertToRequestDTO(updateDTO, existing));
        return ResponseEntity.ok(responseMapper.toDto(updated));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDTO> updateStock(
            @PathVariable UUID id,
            @Valid @RequestBody ProductStockUpdateDTO stockUpdateDTO) {
        ProductModel updated = productUseCase.updateStock(id, stockUpdateDTO.getStock());
        return ResponseEntity.ok(responseMapper.toDto(updated));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductResponseDTO> enableProduct(@PathVariable UUID id) {
        ProductModel updated = productUseCase.enableProduct(id);
        return ResponseEntity.ok(responseMapper.toDto(updated));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductResponseDTO> disableProduct(@PathVariable UUID id) {
        ProductModel updated = productUseCase.disableProduct(id);
        return ResponseEntity.ok(responseMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productUseCase.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteProduct(@PathVariable UUID id) {
        productUseCase.hardDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unit-codes")
    public ResponseEntity<List<String>> getAllUnitCodes() {
        return ResponseEntity.ok(productUseCase.findAllUnitCodes());
    }



    private ProductModel convertToRequestDTO(ProductUpdateDTO updateDTO, ProductModel existingProduct) {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setCode(Optional.ofNullable(updateDTO.getCode()).orElse(existingProduct.getCode()));
        dto.setName(Optional.ofNullable(updateDTO.getName()).orElse(existingProduct.getName()));
        dto.setDescription(Optional.ofNullable(updateDTO.getDescription()).orElse(existingProduct.getDescription()));
        dto.setCategoryId(Optional.ofNullable(updateDTO.getCategoryId()).orElse(existingProduct.getCategoryId()));
        dto.setUnitCode(Optional.ofNullable(updateDTO.getUnitCode()).orElse(existingProduct.getUnitCode()));
        dto.setBrandId(Optional.ofNullable(updateDTO.getBrandId()).orElse(existingProduct.getBrandId()));
        dto.setMinimumStock(Optional.ofNullable(updateDTO.getMinimumStock()).orElse(existingProduct.getMinimumStock()));
        dto.setCurrentStock(Optional.ofNullable(updateDTO.getCurrentStock()).orElse(existingProduct.getCurrentStock()));
        dto.setUnitPrice(Optional.ofNullable(updateDTO.getUnitPrice()).orElse(existingProduct.getUnitPrice()));
        dto.setEnabled(Optional.ofNullable(updateDTO.getEnabled()).orElse(existingProduct.getEnabled()));
        dto.setUpdatedBy(updateDTO.getUpdatedBy());
        return productRequestMapper.toModel(dto);
    }


}