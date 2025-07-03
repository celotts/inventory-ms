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
        //TODO: NO SE USA
        return repository.findByCode(code).orElseThrow(() ->
                new ProductNotFoundException("Product not found with code: " + code));
    }

    public List<ProductModel> getAllProducts() {
        //TODO: NO SE USA
        return repository.findAll();
    }

    public Page<ProductModel> getAllProducts(Pageable pageable) {
        //TODO: NO SE USA
        return repository.findAll(pageable);
    }

    public Page<ProductModel> getAllProductsWithFilters(Pageable pageable, String code, String name, String description) {
        return repository.findAllWithFilters(pageable, code, name, description);
    }

    public void deleteProduct(UUID id) {
        //TODO: NO SE USA
        repository.findById(id).orElseThrow(() ->
                new ProductNotFoundException(id));
    }

    public void hardDeleteProduct(UUID id) {
        //TODO: NO SE USA
        repository.deleteById(id);
    }

    public ProductModel enableProduct(UUID id) {
        //TODO: NO SE USA
        ProductModel product = getProductById(id);
        return repository.save(product.withEnabled(true));
    }

    public ProductModel disableProduct(UUID id) {
        //TODO: NO SE USA
        ProductModel product = getProductById(id);
        return repository.save(product.withEnabled(false));
    }

    public ProductModel updateStock(UUID id, int stock) {
        //TODO: NO SE USA
        ProductModel product = getProductById(id);
        return repository.save(product.withCurrentStock(stock));
    }

    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    public Page<ProductModel> getActiveProducts(Pageable pageable) {
        //TODO: NO SE USA
        return repository.findByEnabled(true, pageable);
    }

    public List<ProductModel> getInactiveProducts() {
        //TODO: NO SE USA
        Pageable pageable = Pageable.unpaged();
        return repository.findByEnabled(false, pageable).getContent();
    }

    public List<ProductModel> getProductsByCategory(UUID categoryId) {
        //TODO: NO SE USA
        return repository.findByCategoryId(categoryId);
    }

    public List<ProductModel> getLowStockByCategory(UUID categoryId) {
        //TODO: NO SE USA
        return repository.findByCategoryId(categoryId)
                .stream()
                .filter(ProductModel::lowStock)
                .collect(Collectors.toList());
    }

    public List<ProductModel> getLowStockProducts() {
        //TODO: NO SE USA
        return repository.findAll().stream()
                .filter(ProductModel::lowStock)
                .toList();
    }

    public List<ProductModel> getProductsByBrand(UUID brandId) {
        //TODO: NO SE USA
        return repository.findByBrandId(brandId);
    }

    public long countProducts() {
        //TODO: NO SE USA
        return repository.findAll().size();
    }

    public long countActiveProducts() {
        //TODO: NO SE USA
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

    @Transactional(readOnly = true)
    public Optional<String> validateUnitCode(String code) {
        if (!productUnitPort.existsByCode(code)) {
            return Optional.empty();
        }
        return productUnitPort.findNameByCode(code);
    }





}