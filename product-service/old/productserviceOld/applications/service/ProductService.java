package com.celotts.productserviceOld.applications.service;

import com.celotts.productserviceOld.domain.model.ProductModel;
import com.celotts.productserviceOld.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productserviceOld.domain.port.product.brand.input.ProductBrandPort;
import com.celotts.productserviceOld.domain.port.product.port.input.ProductPort;
import com.celotts.productserviceOld.domain.port.product.port.output.ProductRepositoryPort;
import com.celotts.productserviceOld.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;

import com.celotts.productserviceOld.domain.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ProductService implements ProductPort {

    private final ProductRepositoryPort repository;
    private final ProductUnitRepositoryPort productUnitPort;
    private final ProductBrandPort productBrandPort;
    private final CategoryRepositoryPort categoryPort;

    public ProductService(
            ProductRepositoryPort repository,
            ProductUnitRepositoryPort productUnitPort,
            @Qualifier("productBrandService") ProductBrandPort productBrandPort,
            @Qualifier("categoryRepositoryAdapter") CategoryRepositoryPort categoryPort,
            ProductRequestMapper productRequestMapper) {
        this.repository = repository;
        this.productUnitPort = productUnitPort;
        this.productBrandPort = productBrandPort;
        this.categoryPort = categoryPort;
    }

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
        return getById(id).orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Override
    public Optional<ProductModel> getByCode(String code) {
        return repository.findByCode(code);
    }

    public ProductModel getProductByCode(String code) {
        return getByCode(code).orElseThrow(() -> new ResourceNotFoundException("Product", "Product not found with code: " + code));
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
}