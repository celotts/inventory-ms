package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.product.brand.input.ProductBrandPort;
import com.celotts.productservice.domain.port.product.port.input.ProductPort;
import com.celotts.productservice.domain.port.product.port.output.ProductRepositoryPort;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductRequestDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductDtoMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;

import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ProductService implements ProductPort {

    private final ProductRepositoryPort repository;
    private final ProductUnitRepositoryPort productUnitPort;

    @Qualifier("productBrandService")
    private final ProductBrandPort productBrandPort;

    @Qualifier("categoryRepositoryAdapter")
    private final CategoryRepositoryPort categoryPort;

    @Override
    public ProductModel create(ProductModel product) {
        return repository.save(product);
    }

    @Override
    public ProductModel update(UUID id, ProductModel product) {
        ProductModel existing = getProductById(id);
        existing = existing.withName(product.getName())
                .withDescription(product.getDescription())
                .withUnitCode(product.getUnitCode())
                .withCategoryId(product.getCategoryId())
                .withBrandId(product.getBrandId())
                .withMinimumStock(product.getMinimumStock())
                .withCurrentStock(product.getCurrentStock())
                .withUnitPrice(product.getUnitPrice())
                .withEnabled(product.getEnabled());

        return repository.save(existing);
    }

    @Override
    public Optional<ProductModel> getById(UUID id) {
        return repository.findById(id);
    }

    public ProductModel getProductById(UUID id) {
        return getById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Optional<ProductModel> getByCode(String code) {
        return repository.findByCode(code);
    }

    public ProductModel getProductByCode(String code) {
        return getByCode(code).orElseThrow(() -> new ProductNotFoundException("Product not found with code: " + code));
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<ProductModel> getAll() {
        return repository.findAll();
    }

    @Override
    public Page<ProductModel> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<ProductModel> getAllWithFilters(Pageable pageable, String code, String name, String description) {
        return repository.findAllWithFilters(pageable, code, name, description);
    }

    @Override
    public Page<ProductModel> getEnabledProducts(Pageable pageable) {
        return repository.findByEnabled(true, pageable);
    }

    @Override
    public List<ProductModel> getDisabledProducts() {
        return repository.findByEnabled(false, Pageable.unpaged()).getContent();
    }

    @Override
    public List<ProductModel> getByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    @Override
    public List<ProductModel> getLowStockByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId)
                .stream()
                .filter(ProductModel::lowStock)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getLowStockProducts() {
        return repository.findAll().stream()
                .filter(ProductModel::lowStock)
                .toList();
    }

    @Override
    public List<ProductModel> getByBrand(UUID brandId) {
        return repository.findByBrandId(brandId);
    }

    @Override
    public ProductModel enable(UUID id) {
        ProductModel product = getProductById(id);
        return repository.save(product.withEnabled(true));
    }

    @Override
    public void disable(UUID id) {
        ProductModel product = getProductById(id);
        repository.save(product.withEnabled(false));
    }

    @Override
    public ProductModel updateStock(UUID id, int stock) {
        ProductModel product = getProductById(id);
        return repository.save(product.withCurrentStock(stock));
    }

    @Override
    public long count() {
        return repository.findAll().size();
    }

    @Override
    public long countEnabled() {
        return repository.findByEnabled(true, Pageable.unpaged()).getTotalElements();
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.findByCode(code).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<String> validateUnitCode(String code) {
        if (!productUnitPort.existsByCode(code)) {
            return Optional.empty();
        }
        return productUnitPort.findNameByCode(code);
    }

    // --- Utilidad adicional para inicialización desde DTO ---

    public ProductModel createProduct(ProductRequestDto dto) {
        if (repository.findByCode(dto.getCode()).isPresent()) {
            throw new ProductAlreadyExistsException("Product code already exists: " + dto.getCode());
        }

        validateReferences(dto);

        String unitName = productUnitPort.findNameByCode(dto.getUnitCode())
                .orElse(dto.getUnitCode());

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

    public ProductModel updateProduct(UUID id, ProductRequestDto dto) {
        ProductModel existing = getProductById(id);
        validateReferences(dto);

        ProductUpdateDto updateDto = ProductDtoMapper.toUpdateDto(dto);
        ProductRequestMapper.updateModelFromDto(existing, updateDto);

        return repository.save(existing);
    }

    private void validateReferences(ProductRequestDto dto) {
        if (!productUnitPort.existsByCode(dto.getUnitCode())) {
            throw new IllegalArgumentException("Invalid unit code: " + dto.getUnitCode());
        }

        if (!productBrandPort.existsById(dto.getBrandId())) {
            throw new IllegalArgumentException("Invalid brand ID: " + dto.getBrandId());
        }

        if (!categoryPort.existsById(dto.getCategoryId())) {
            throw new IllegalArgumentException("Invalid category ID: " + dto.getCategoryId());
        }
    }
}