package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductService;
import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class ProductController {

    private final ProductService productService;

    // ✅ AGREGAR: Configuración desde variables de entorno
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

    // GET /api/v1/products - Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductModel> products = productService.getAllProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // ✅ CAMBIAR: Este método para usar configuración externa
    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProductsPaginated(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {

        log.info("Fetching paginated products - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        // Usar valores de configuración en lugar de hardcodeados
        int pageNumber = page != null ? Math.max(0, page) : defaultPage;
        int pageSize = size != null ? Math.min(Math.max(1, size), maxSize) : defaultSize;
        String sortField = sortBy != null && !sortBy.trim().isEmpty() ? sortBy.trim() : defaultSort;
        String sortDirection = sortDir != null && !sortDir.trim().isEmpty() ? sortDir.trim() : defaultDirection;

        // Validar campo de ordenamiento
        List<String> allowedSortFields = List.of("createdAt", "updatedAt", "code", "description", "unitPrice", "currentStock");
        if (!allowedSortFields.contains(sortField)) {
            log.warn("Invalid sort field: {}. Using default: {}", sortField, defaultSort);
            sortField = defaultSort;
        }

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<ProductModel> products = productService.getAllProducts(pageable);
        Page<ProductResponseDTO> response = products.map(ProductResponseDTO::fromModel);

        log.info("Retrieved {} products out of {} total (page {}/{})",
                response.getNumberOfElements(),
                response.getTotalElements(),
                response.getNumber() + 1,
                response.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // ✅ EL RESTO DE TUS MÉTODOS PERMANECEN IGUAL
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable UUID id) {
        ProductModel product = productService.getProductById(id);
        ProductResponseDTO response = ProductResponseDTO.fromModel(product);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProductResponseDTO> getProductByCode(@PathVariable String code) {
        ProductModel product = productService.getProductByCode(code);
        ProductResponseDTO response = ProductResponseDTO.fromModel(product);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductModel createdProduct = productService.createProduct(requestDTO);
        ProductResponseDTO response = ProductResponseDTO.fromModel(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {

        ProductModel updatedProduct = productService.updateProduct(id, requestDTO);
        ProductResponseDTO response = ProductResponseDTO.fromModel(updatedProduct);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> partialUpdateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductUpdateDTO updateDTO) {

        ProductModel existingProduct = productService.getProductById(id);
        ProductModel updatedProduct = productService.updateProduct(id, convertToRequestDTO(updateDTO, existingProduct));
        ProductResponseDTO response = ProductResponseDTO.fromModel(updatedProduct);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteProduct(@PathVariable UUID id) {
        productService.hardDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsProduct(@PathVariable UUID id) {
        boolean exists = productService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDTO>> getActiveProducts() {
        List<ProductModel> products = productService.getActiveProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<ProductResponseDTO>> getInactiveProducts() {
        List<ProductModel> products = productService.getInactiveProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStockProducts() {
        List<ProductModel> products = productService.getLowStockProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{typeCode}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByType(@PathVariable String typeCode) {
        List<ProductModel> products = productService.getProductsByType(typeCode);
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByBrand(@PathVariable UUID brandId) {
        List<ProductModel> products = productService.getProductsByBrand(brandId);
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .toList();  // ✅ Forma moderna
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDTO> updateStock(
            @PathVariable UUID id,
            @Valid @RequestBody ProductStockUpdateDTO stockUpdateDTO) {

        ProductModel updatedProduct = productService.updateStock(id, stockUpdateDTO.getStock());
        ProductResponseDTO response = ProductResponseDTO.fromModel(updatedProduct);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductResponseDTO> enableProduct(@PathVariable UUID id) {
        ProductModel updatedProduct = productService.enableProduct(id);
        ProductResponseDTO response = ProductResponseDTO.fromModel(updatedProduct);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductResponseDTO> disableProduct(@PathVariable UUID id) {
        ProductModel updatedProduct = productService.disableProduct(id);
        ProductResponseDTO response = ProductResponseDTO.fromModel(updatedProduct);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProducts() {
        long count = productService.countProducts();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveProducts() {
        long count = productService.countActiveProducts();
        return ResponseEntity.ok(count);
    }

    // Método auxiliar para convertir UpdateDTO a RequestDTO
    private ProductRequestDTO convertToRequestDTO(ProductUpdateDTO updateDTO, ProductModel existingProduct) {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode(updateDTO.getCode() != null ? updateDTO.getCode() : existingProduct.getCode());
        requestDTO.setDescription(updateDTO.getDescription() != null ? updateDTO.getDescription() : existingProduct.getDescription());
        requestDTO.setProductTypeCode(updateDTO.getProductTypeCode() != null ? updateDTO.getProductTypeCode() : existingProduct.getProductTypeCode());
        requestDTO.setUnitCode(updateDTO.getUnitCode() != null ? updateDTO.getUnitCode() : existingProduct.getUnitCode());
        requestDTO.setBrandId(updateDTO.getBrandId() != null ? updateDTO.getBrandId() : existingProduct.getBrandId());
        requestDTO.setMinimumStock(updateDTO.getMinimumStock() != null ? updateDTO.getMinimumStock() : existingProduct.getMinimumStock());
        requestDTO.setCurrentStock(updateDTO.getCurrentStock() != null ? updateDTO.getCurrentStock() : existingProduct.getCurrentStock());
        requestDTO.setUnitPrice(updateDTO.getUnitPrice() != null ? updateDTO.getUnitPrice() : existingProduct.getUnitPrice());
        requestDTO.setEnabled(updateDTO.getEnabled() != null ? updateDTO.getEnabled() : existingProduct.getEnabled());
        requestDTO.setUpdatedBy(updateDTO.getUpdatedBy());
        return requestDTO;
    }
}