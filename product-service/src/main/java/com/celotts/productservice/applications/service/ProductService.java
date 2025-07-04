package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.port.category.CategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductDtoMapper;
import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.product.ProductBrandPort;
import com.celotts.productservice.domain.port.product_brand.ProductRepositoryPort;
import com.celotts.productservice.domain.port.product.ProductUnitPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepositoryPort repository;
    private final ProductUnitPort productUnitPort;
    private final ProductBrandPort productBrandPort;
    private final CategoryRepositoryPort categoryPort;


    public ProductService(
            @Qualifier("productRepositoryAdapter") ProductRepositoryPort repository,
            ProductUnitPort productUnitPort,
            @Qualifier("productBrandService") ProductBrandPort productBrandPort,
            @Qualifier("categoryRepositoryAdapter") CategoryRepositoryPort categoryPort
    ) {
        this.repository = repository;
        this.productUnitPort = productUnitPort;
        this.productBrandPort = productBrandPort;
        this.categoryPort = categoryPort;
    }

    public ProductModel createProduct(ProductRequestDTO dto) {
        // Verificar que el código no existe
        if (repository.findByCode(dto.getCode()).isPresent()) {
            throw new ProductAlreadyExistsException("Product code already exists: " + dto.getCode());
        }

        // Validar referencias y obtener nombre de unidad para log
        validateReferences(dto);

        // Obtener nombre de unidad para log descriptivo (opcional)
        String unitName = productUnitPort.findNameByCode(dto.getUnitCode())
                .orElse(dto.getUnitCode()); // Fallback al código si no encuentra nombre

        log.info("Creating product '{}' with unit: {}", dto.getName(), unitName);

        ProductModel model = ProductModel.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .categoryId(dto.getCategoryId())
                .unitCode(dto.getUnitCode())
                .brandId(dto.getBrandId())
                .minimumStock(dto.getMinimumStock())
                .currentStock(dto.getCurrentStock())
                .unitPrice(dto.getUnitPrice())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : true)
                .createdAt(LocalDateTime.now())
                .createdBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : "system")
                .build();

        return repository.save(model);
    }

    public ProductModel updateProduct(UUID id, ProductRequestDTO dto) {
        ProductModel existing = getProductById(id);
        validateReferences(dto);

        ProductUpdateDTO updateDto = ProductDtoMapper.toUpdateDto(dto);
        ProductRequestMapper.updateModelFromDto(existing, updateDto);

        return repository.save(existing);
    }

    public ProductModel getProductById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new ProductNotFoundException(id));
    }

    public ProductModel getProductByCode(String code) {
        return repository.findByCode(code).orElseThrow(() ->
                new ProductNotFoundException("Product not found with code: " + code));
    }

    public List<ProductModel> getAllProducts() {
        return repository.findAll();
    }

    public Page<ProductModel> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ProductModel> getAllProductsWithFilters(Pageable pageable, String code, String name, String description) {
        return repository.findAllWithFilters(pageable, code, name, description);
    }

    // Este método se invoca desde ProductController en el endpoint DELETE /{id}/hard
    public void hardDeleteProduct(UUID id) {
        repository.deleteById(id);
    }

    // Este método se invoca desde ProductController en PATCH /{id}/enable
    public ProductModel enableProduct(UUID id) {
        ProductModel product = getProductById(id);
        return repository.save(product.withEnabled(true));
    }

    // Este método se invoca desde ProductController en PATCH /{id}/disable
    public ProductModel disableProduct(UUID id) {
        ProductModel product = getProductById(id);
        return repository.save(product.withEnabled(false));
    }

    // Este método se invoca desde ProductController en PATCH /{id}/stock
    public ProductModel updateStock(UUID id, int stock) {
        ProductModel product = getProductById(id);
        return repository.save(product.withCurrentStock(stock));
    }

    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    // Este método se invoca desde ProductController en GET /active
    public Page<ProductModel> getActiveProducts(Pageable pageable) {
        return repository.findByEnabled(true, pageable);
    }

    // Este método se invoca desde ProductController en GET /inactive
    public List<ProductModel> getInactiveProducts() {
        Pageable pageable = Pageable.unpaged();
        return repository.findByEnabled(false, pageable).getContent();
    }

    // Este método se invoca desde ProductController en GET /category/{categoryId}
    public List<ProductModel> getProductsByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    // Este método se invoca desde ProductController en GET /category/{categoryId}/low-stock
    public List<ProductModel> getLowStockByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId)
                .stream()
                .filter(ProductModel::lowStock)
                .collect(Collectors.toList());
    }

    // Este método se invoca desde ProductController en GET /low-stock
    public List<ProductModel> getLowStockProducts() {
        return repository.findAll().stream()
                .filter(ProductModel::lowStock)
                .toList();
    }

    // Este método se invoca desde ProductController en GET /brand/{brandId}
    public List<ProductModel> getProductsByBrand(UUID brandId) {
        return repository.findByBrandId(brandId);
    }

    // Este método se invoca desde ProductController en GET /count
    public long countProducts() {
        return repository.findAll().size();
    }

    // Este método se invoca desde ProductController en GET /count/active
    public long countActiveProducts() {
        return repository.findByEnabled(true, Pageable.unpaged()).getTotalElements();
    }

    private void validateReferences(ProductRequestDTO dto) {
        // Validar que unitCode existe
        if (!productUnitPort.existsByCode(dto.getUnitCode())) {
            throw new IllegalArgumentException("Invalid unit code: " + dto.getUnitCode());
        }

        // Validar que brandId existe
        if (!productBrandPort.existsById(dto.getBrandId())) {
            throw new IllegalArgumentException("Invalid brand ID: " + dto.getBrandId());
        }

        // Validar que categoryId existe
        if (!categoryPort.existsById(dto.getCategoryId())) {
            throw new IllegalArgumentException("Invalid category ID: " + dto.getCategoryId());
        }
    }

    // Este método se invoca desde ProductController en GET /validate-unit/{code}
    @Transactional(readOnly = true)
    public Optional<String> validateUnitCode(String code) {
        if (!productUnitPort.existsByCode(code)) {
            return Optional.empty();
        }
        return productUnitPort.findNameByCode(code);
    }
}