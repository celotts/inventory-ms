package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.category.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.product_brand.ProductBrandRepositoryPort;
import com.celotts.productservice.domain.port.product_brand.ProductRepositoryPort;
import com.celotts.productservice.domain.port.product.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductUseCaseImpl implements ProductUseCase {

    private final ProductRepositoryPort repository;
    private final ProductUnitPort productUnitPort;
    private final ProductBrandRepositoryPort productBrandPort;
    private final CategoryRepositoryPort categoryPort;

    @Autowired
    public ProductUseCaseImpl(
            ProductRepositoryPort repository,
            ProductUnitPort productUnitPort,
            @Qualifier("productBrandAdapter") ProductBrandRepositoryPort productBrandPort,
            @Qualifier("categoryAdapter") CategoryRepositoryPort categoryPort
    ) {
        this.repository = repository;
        this.productUnitPort = productUnitPort;
        this.productBrandPort = productBrandPort;
        this.categoryPort = categoryPort;
    }


    @Override
    public ProductModel createProduct(ProductModel model) {
        if (repository.findByCode(model.getCode()).isPresent()) {
            throw new ProductAlreadyExistsException("Product code already exists: " + model.getCode());
        }
        validateReferences(model);
        model.setCreatedAt(LocalDateTime.now());
        return repository.save(model);
    }

    @Override
    public ProductModel updateProduct(UUID id, ProductModel model) {
        ProductModel existing = getProductById(id);
        validateReferences(model);
        return repository.save(model.withId(id));
    }

    @Override
    public ProductModel getProductById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public ProductModel getProductByCode(String code) {
        return repository.findByCode(code).orElseThrow(() ->
                new ProductNotFoundException("Product not found with code: " + code));
    }

    @Override
    public List<ProductModel> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public Page<ProductModel> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<ProductModel> getAllProductsWithFilters(Pageable pageable, String code, String name, String description) {
        return repository.findAllWithFilters(pageable, code, name, description);
    }

    @Override
    public void deleteProduct(UUID id) {
        repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void hardDeleteProduct(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public ProductModel enableProduct(UUID id) {
        ProductModel product = getProductById(id);
        return repository.save(product.withEnabled(true));
    }

    @Override
    public ProductModel disableProduct(UUID id) {
        ProductModel product = getProductById(id);
        return repository.save(product.withEnabled(false));
    }

    @Override
    public ProductModel updateStock(UUID id, int stock) {
        ProductModel product = getProductById(id);
        return repository.save(product.withCurrentStock(stock));
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public Page<ProductModel> getActiveProducts(Pageable pageable) {
        return repository.findByEnabled(true, pageable);
    }

    @Override
    public List<ProductModel> getInactiveProducts() {
        return repository.findByEnabled(false, Pageable.unpaged()).getContent();
    }

    @Override
    public List<ProductModel> getProductsByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    @Override
    public List<ProductModel> getLowStockByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId)
                .stream().filter(ProductModel::lowStock).collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getLowStockProducts() {
        return repository.findAll().stream().filter(ProductModel::lowStock).toList();
    }

    @Override
    public List<ProductModel> getProductsByBrand(UUID brandId) {
        return repository.findByBrandId(brandId);
    }

    @Override
    public long countProducts() {
        return repository.findAll().size();
    }

    @Override
    public long countActiveProducts() {
        return repository.findByEnabled(true, Pageable.unpaged()).getTotalElements();
    }

    @Override
    public Optional<String> validateUnitCode(String code) {
        return productUnitPort.findNameByCode(code);
    }


    private void validateReferences(ProductModel model) {
        if (!productUnitPort.existsByCode(model.getUnitCode())) {
            throw new IllegalArgumentException("Invalid unit code: " + model.getUnitCode());
        }

        if (!productBrandPort.existsById(model.getBrandId())) {
            throw new IllegalArgumentException("Invalid brand ID: " + model.getBrandId());
        }

        if (!categoryPort.existsById(model.getCategoryId())) {
            throw new IllegalArgumentException("Invalid category ID: " + model.getCategoryId());
        }
    }
}