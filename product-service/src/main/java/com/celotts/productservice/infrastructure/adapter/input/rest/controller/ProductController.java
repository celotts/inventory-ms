package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductService;
import com.celotts.productservice.applications.service.CategoryService; // ✅ AGREGAR ESTE IMPORT
import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductResponseDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductStockUpdateDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductResponseMapper;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class ProductController {

    private final ProductService productService;
    //private final CategoryService categoryService; // ✅ AGREGAR ESTA DEPENDENCIA
    private final ProductResponseMapper responseMapper;

    // Configuración de paginación desde variables de entorno
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

    // ===============================================
    // HEALTH CHECK / TEST
    // ===============================================

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("¡Product Service funcionando correctamente!");
    }

    // ===============================================
    // CREATE - Operaciones de creación
    // ===============================================

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        log.info("Creating new product with code: {}", requestDTO.getCode());
        ProductModel createdProduct = productService.createProduct(requestDTO);
        ProductResponseDTO response = responseMapper.toDto(createdProduct);
        log.info("Product created successfully with ID: {}", createdProduct.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/validate-unit/{code}")
    public ResponseEntity<String> validateUnitCode(@PathVariable String code) {
        Optional<String> unitName = productService.validateUnitCode(code);
        return unitName.map(name -> ResponseEntity.ok("Valid unit: " + name))
                .orElse(ResponseEntity.notFound().build());
    }

    // ===============================================
    // READ - Operaciones de lectura
    // ===============================================

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        log.info("Fetching all products");
        List<ProductModel> products = productService.getAllProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
        log.info("List Retrieved {} products", response.size());
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

        log.info("Fetching paginated products - page: {}, size: {}, sortBy: {}, sortDir: {}, code: {}, name: {}, description: {}",
                page, size, sortBy, sortDir, code, name, description);

        // Aplicar valores por defecto y validaciones
        int pageNumber = page != null ? Math.max(0, page) : defaultPage;
        int pageSize = size != null ? Math.min(Math.max(1, size), maxSize) : defaultSize;
        String sortField = sortBy != null && !sortBy.trim().isEmpty() ? sortBy.trim() : defaultSort;
        String sortDirection = sortDir != null && !sortDir.trim().isEmpty() ? sortDir.trim() : defaultDirection;

        // Validar campo de ordenamiento
        List<String> allowedSortFields = List.of("createdAt", "updatedAt", "code", "name", "description", "unitPrice", "currentStock");
        if (!allowedSortFields.contains(sortField)) {
            log.warn("Invalid sort field: {}. Using default: {}", sortField, defaultSort);
            sortField = defaultSort;
        }

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortField).descending() : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Determinar si hay filtros aplicados
        boolean hasFilters = (code != null && !code.trim().isEmpty()) ||
                (name != null && !name.trim().isEmpty()) ||
                (description != null && !description.trim().isEmpty());

        Page<ProductModel> products;
        if (hasFilters) {
            products = productService.getAllProductsWithFilters(pageable, code, name, description);
            log.info("Applied filters - code: {}, name: {}, description: {}", code, name, description);
        } else {
            products = productService.getAllProducts(pageable);
        }

        Page<ProductResponseDTO> response = products.map(responseMapper::toDto);

        log.info("Retrieved {} products out of {} total (page {}/{})",
                response.getNumberOfElements(),
                response.getTotalElements(),
                response.getNumber() + 1,
                response.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable UUID id) {
        log.info("Fetching product by ID: {}", id);
        ProductModel product = productService.getProductById(id);
        ProductResponseDTO response = responseMapper.toDto(product);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProductResponseDTO> getProductByCode(@PathVariable String code) {
        log.info("Fetching product by code: {}", code);
        ProductModel product = productService.getProductByCode(code);
        ProductResponseDTO response = responseMapper.toDto(product);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsProduct(@PathVariable UUID id) {
        log.info("Checking if product exists: {}", id);
        boolean exists = productService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDTO>> getActiveProducts() {
        log.info("Fetching active products");
        Pageable pageable = Pageable.unpaged();
        List<ProductModel> products = productService.getActiveProducts(pageable).getContent();
        List<ProductResponseDTO> response = products.stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
        log.info("Retrieved Product Response {} active products", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<ProductResponseDTO>> getInactiveProducts() {
        log.info("Fetching inactive products");
        List<ProductModel> products = productService.getInactiveProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
        log.info("Retrieved {} inactive products", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStockProducts() {
        log.info("Fetching low stock products");
        List<ProductModel> products = productService.getLowStockProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
        log.info("Retrieved {} low stock products", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable UUID categoryId) {
        log.info("Fetching products by category: {}", categoryId);
        List<ProductModel> products = productService.getProductsByCategory(categoryId);
        List<ProductResponseDTO> response = products.stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
        log.info("Retrieved {} products for category: {}", response.size(), categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStockByCategory(@PathVariable UUID categoryId) {
        log.info("Fetching low stock products by category: {}", categoryId);
        List<ProductModel> products = productService.getLowStockByCategory(categoryId);
        List<ProductResponseDTO> response = products.stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
        log.info("Retrieved {} low stock products for category: {}", response.size(), categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByBrand(@PathVariable UUID brandId) {
        log.info("Fetching products by brand: {}", brandId);
        List<ProductModel> products = productService.getProductsByBrand(brandId);
        List<ProductResponseDTO> response = products.stream()
                .map(responseMapper::toDto)
                .toList();
        log.info("Retrieved {} products for brand: {}", response.size(), brandId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProducts() {
        log.info("Counting all products");
        long count = productService.countProducts();
        log.info("Total products count: {}", count);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/active")
    public ResponseEntity<Long> countActiveProducts() {
        log.info("Counting active products");
        long count = productService.countActiveProducts();
        log.info("Active products count: {}", count);
        return ResponseEntity.ok(count);
    }

    // ===============================================
    // ENDPOINTS CON DETALLES DE CATEGORÍA
    // ===============================================
    //TODO: Se comneta para futuro desarrollo
   /* @GetMapping("/{id}/with-details")
    public ResponseEntity<ProductResponseDTO> getProductWithDetails(@PathVariable UUID id) {
        log.info("Fetching product with details by ID: {}", id);
        ProductModel product = productService.getProductById(id);

        // Obtener nombre de categoría
        String categoryName = categoryService.findById(product.getCategoryId())
                .map(CategoryModel::getName)
                .orElse("Unknown Category");

        // ✅ CORREGIR: Usar la instancia del mapper, no estática
        ProductResponseDTO response = ProductResponseMapper.toDtoWithCategoryName(product, categoryName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-details")
    public ResponseEntity<List<ProductResponseDTO>> getAllProductsWithDetails() {
        log.info("Fetching all products with details");
        List<ProductModel> products = productService.getAllProducts();

        List<ProductResponseDTO> response = products.stream()
                .map(product -> {
                    String categoryName = categoryService.findById(product.getCategoryId())
                            .map(CategoryModel::getName)
                            .orElse("Unknown Category");
                    // ✅ CORREGIR: Usar la instancia del mapper, no estática
                    return ProductResponseMapper.toDtoWithCategoryName(product, categoryName);
                })
                .collect(Collectors.toList());

        log.info("Retrieved {} products with details", response.size());
        return ResponseEntity.ok(response);
    }*/

    // ===============================================
    // UPDATE - Operaciones de actualización
    // ===============================================

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {

        log.info("Updating product with ID: {}", id);
        ProductModel updatedProduct = productService.updateProduct(id, requestDTO);
        ProductResponseDTO response = responseMapper.toDto(updatedProduct);
        log.info("Product updated successfully: {}", id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> partialUpdateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductUpdateDTO updateDTO) {

        log.info("Partially updating product with ID: {}", id);
        ProductModel existingProduct = productService.getProductById(id);
        ProductModel updatedProduct = productService.updateProduct(id, convertToRequestDTO(updateDTO, existingProduct));
        ProductResponseDTO response = responseMapper.toDto(updatedProduct);
        log.info("Product partially updated successfully: {}", id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDTO> updateStock(
            @PathVariable UUID id,
            @Valid @RequestBody ProductStockUpdateDTO stockUpdateDTO) {

        log.info("Updating stock for product ID: {} to {}", id, stockUpdateDTO.getStock());
        ProductModel updatedProduct = productService.updateStock(id, stockUpdateDTO.getStock());
        ProductResponseDTO response = responseMapper.toDto(updatedProduct);
        log.info("Stock updated successfully for product: {}", id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductResponseDTO> enableProduct(@PathVariable UUID id) {
        log.info("Enabling product with ID: {}", id);
        ProductModel updatedProduct = productService.enableProduct(id);
        ProductResponseDTO response = responseMapper.toDto(updatedProduct);
        log.info("Product enabled successfully: {}", id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductResponseDTO> disableProduct(@PathVariable UUID id) {
        log.info("Disabling product with ID: {}", id);
        ProductModel updatedProduct = productService.disableProduct(id);
        ProductResponseDTO response = responseMapper.toDto(updatedProduct);
        log.info("Product disabled successfully: {}", id);
        return ResponseEntity.ok(response);
    }

    // ===============================================
    // DELETE - Operaciones de eliminación
    // ===============================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        log.info("Soft deleting product with ID: {}", id);
        productService.deleteProduct(id);
        log.info("Product soft deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteProduct(@PathVariable UUID id) {
        log.info("Hard deleting product with ID: {}", id);
        productService.hardDeleteProduct(id);
        log.info("Product hard deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }



    // ===============================================
    // MÉTODOS AUXILIARES
    // ===============================================

    /**
     * Convierte un ProductUpdateDTO a ProductRequestDTO manteniendo los valores existentes
     * para los campos no especificados en la actualización parcial.
     */
    private ProductRequestDTO convertToRequestDTO(ProductUpdateDTO updateDTO, ProductModel existingProduct) {
        ProductRequestDTO requestDTO = new ProductRequestDTO();
        requestDTO.setCode(updateDTO.getCode() != null ? updateDTO.getCode() : existingProduct.getCode());
        requestDTO.setName(updateDTO.getName() != null ? updateDTO.getName() : existingProduct.getName());
        requestDTO.setDescription(updateDTO.getDescription() != null ? updateDTO.getDescription() : existingProduct.getDescription());
        requestDTO.setCategoryId(updateDTO.getCategoryId() != null ? updateDTO.getCategoryId() : existingProduct.getCategoryId());
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