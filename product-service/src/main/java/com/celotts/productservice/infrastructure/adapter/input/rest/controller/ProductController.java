package com.celotts.productservice.infrastructure.adapter.input.rest.controller;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.product.root.input.ProductUseCase;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@CrossOrigin(origins = "*")
@Slf4j
@Tag(name = "Product API", description = "API para gestionar productos")
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
    @Operation(summary = "Test API", description = "Endpoint de prueba para verificar que el servicio funciona.")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("¡Product Service funcionando correctamente!");
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Crea un producto en el sistema")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductModel requestDTO) {
        log.info("Creating new product with code: {}", requestDTO.getCode());
        ProductModel createdProduct = productUseCase.createProduct(requestDTO);
        ProductResponseDTO response = responseMapper.toDto(createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Obtener productos activos", description = "Lista todos los productos activos")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> response = ProductResponseMapper.toResponseDtoList(
                productUseCase.getActiveProducts(Pageable.unpaged()).getContent()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Obtener productos paginados", description = "Lista productos con paginación y filtros opcionales")
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

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Consulta un producto por su identificador")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable UUID id) {
        ProductModel product = productUseCase.getProductById(id);
        return ResponseEntity.ok(responseMapper.toDto(product));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto lógico", description = "Elimina de forma lógica un producto")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productUseCase.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/enable")
    @Operation(summary = "Habilitar producto", description = "Habilita un producto por su ID")
    public ResponseEntity<ProductResponseDTO> enableProduct(@PathVariable UUID id) {
        ProductModel enabledProduct = productUseCase.enableProduct(id);
        return ResponseEntity.ok(responseMapper.toDto(enabledProduct));
    }

}