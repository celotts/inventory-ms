// ProductController - Controlador completo y corregido
package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.applications.service.ProductService;
import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class ProductController {

    private final ProductService productService;

    // GET /api/v1/products - Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductModel> products = productService.getAllProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/products/paginated - Obtener productos con paginación
    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductResponseDTO>> getAllProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductModel> products = productService.getAllProducts(pageable);
        Page<ProductResponseDTO> response = products.map(ProductResponseDTO::fromModel);

        return ResponseEntity.ok(response);
    }

    // GET /api/v1/products/{id} - Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable UUID id) {
        ProductModel product = productService.getProductById(id);
        ProductResponseDTO response = ProductResponseDTO.fromModel(product);
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/products/code/{code} - Obtener producto por código
    @GetMapping("/code/{code}")
    public ResponseEntity<ProductResponseDTO> getProductByCode(@PathVariable String code) {
        ProductModel product = productService.getProductByCode(code);
        ProductResponseDTO response = ProductResponseDTO.fromModel(product);
        return ResponseEntity.ok(response);
    }

    // POST /api/v1/products - Crear nuevo producto
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductModel createdProduct = productService.createProduct(requestDTO);
        ProductResponseDTO response = ProductResponseDTO.fromModel(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT /api/v1/products/{id} - Actualizar producto completo
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {

        ProductModel updatedProduct = productService.updateProduct(id, requestDTO);
        ProductResponseDTO response = ProductResponseDTO.fromModel(updatedProduct);
        return ResponseEntity.ok(response);
    }

    // PATCH /api/v1/products/{id} - Actualizar producto parcial
    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> partialUpdateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody ProductUpdateDTO updateDTO) {

        ProductModel existingProduct = productService.getProductById(id);
        // Aquí aplicarías los cambios parciales usando el mapper
        // ProductRequestMapper.updateModelFromDto(existingProduct, updateDTO);
        // Por simplicidad, reutilizamos el método de update completo

        ProductModel updatedProduct = productService.updateProduct(id, convertToRequestDTO(updateDTO, existingProduct));
        ProductResponseDTO response = ProductResponseDTO.fromModel(updatedProduct);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/v1/products/{id} - Eliminar producto (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/v1/products/{id}/hard - Eliminar producto definitivamente
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteProduct(@PathVariable UUID id) {
        productService.hardDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/v1/products/{id}/exists - Verificar si existe producto
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsProduct(@PathVariable UUID id) {
        boolean exists = productService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    // GET /api/v1/products/active - Obtener productos activos
    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDTO>> getActiveProducts() {
        List<ProductModel> products = productService.getActiveProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/products/inactive - Obtener productos inactivos
    @GetMapping("/inactive")
    public ResponseEntity<List<ProductResponseDTO>> getInactiveProducts() {
        List<ProductModel> products = productService.getInactiveProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/products/low-stock - Obtener productos con stock bajo
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStockProducts() {
        List<ProductModel> products = productService.getLowStockProducts();
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/products/type/{typeCode} - Obtener productos por tipo
    @GetMapping("/type/{typeCode}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByType(@PathVariable String typeCode) {
        List<ProductModel> products = productService.getProductsByType(typeCode);
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/products/brand/{brandId} - Obtener productos por marca
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByBrand(@PathVariable UUID brandId) {
        List<ProductModel> products = productService.getProductsByBrand(brandId);
        List<ProductResponseDTO> response = products.stream()
                .map(ProductResponseDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // PATCH /api/v1/products/{id}/stock - Actualizar stock
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDTO> updateStock(
            @PathVariable UUID id,
            @Valid @RequestBody ProductStockUpdateDTO stockUpdateDTO) {

        ProductModel updatedProduct = productService.updateStock(id, stockUpdateDTO.getStock());
        ProductResponseDTO response = ProductResponseDTO.fromModel(updatedProduct);
        return ResponseEntity.ok(response);
    }

    // PATCH /api/v1/products/{id}/enable - Habilitar producto
    @PatchMapping("/{id}/enable")
    public ResponseEntity<ProductResponseDTO> enableProduct(@PathVariable UUID id) {
        ProductModel updatedProduct = productService.enableProduct(id);
        ProductResponseDTO response = ProductResponseDTO.fromModel(updatedProduct);
        return ResponseEntity.ok(response);
    }

    // PATCH /api/v1/products/{id}/disable - Deshabilitar producto
    @PatchMapping("/{id}/disable")
    public ResponseEntity<ProductResponseDTO> disableProduct(@PathVariable UUID id) {
        ProductModel updatedProduct = productService.disableProduct(id);
        ProductResponseDTO response = ProductResponseDTO.fromModel(updatedProduct);
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/products/count - Contar productos
    @GetMapping("/count")
    public ResponseEntity<Long> countProducts() {
        long count = productService.countProducts();
        return ResponseEntity.ok(count);
    }

    // GET /api/v1/products/count/active - Contar productos activos
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