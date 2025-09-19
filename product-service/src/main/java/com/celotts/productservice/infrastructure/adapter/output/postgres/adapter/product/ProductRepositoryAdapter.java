package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.domain.port.output.product.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    // ---------- EXISTS / COUNTS ----------
    @Override public boolean existsById(UUID id) { return productRepository.existsById(id); }
    @Override public boolean existsByCode(String code) { return productRepository.existsByCode(code); }
    @Override public boolean existsByName(String name) { return productRepository.existsByName(name); }
    @Override public long countAll() { return productRepository.count(); }
    @Override public long countActive() { return productRepository.countByEnabled(true); }

    // ---------- FINDS (single) ----------
    @Override
    public Optional<ProductModel> findById(UUID id) {
        return productRepository.findById(id).map(productEntityMapper::toModel);
    }

    @Override
    public Optional<ProductModel> findByCode(String code) {
        return productRepository.findByCode(code).map(productEntityMapper::toModel);
    }

    // ---------- LISTADOS / PAGINACIÓN ----------
    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findAllWithFilters(Pageable pageable, String code, String name, String description) {
        return productRepository.findAllWithFilters(pageable, code, name, description)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findActive(Pageable pageable) {
        return productRepository.findByEnabled(true, pageable).map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findInactive(Pageable pageable) {
        return productRepository.findByEnabled(false, pageable).map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findByCategory(UUID categoryId, Pageable pageable) {
        // requiere en el repo: findByCategoryId(UUID, Pageable) con JOIN a ProductCategoryEntity
        return productRepository.findByCategoryId(categoryId, pageable).map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findByBrand(UUID brandId, Pageable pageable) {
        return productRepository.findByBrandId(brandId, pageable).map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findLowStock(Pageable pageable, int threshold) {
        // requiere en el repo: Page<ProductEntity> findByCurrentStockLessThanEqual(BigDecimal, Pageable)
        return productRepository
                .findByCurrentStockLessThanEqual(BigDecimal.valueOf(threshold), pageable)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findLowStockByCategory(UUID categoryId, Pageable pageable, int threshold) {
        // requiere en el repo: findByCategoryAndMaxStock(UUID, BigDecimal, Pageable) con JOIN + <=
        return productRepository
                .findByCategoryAndMaxStock(categoryId, BigDecimal.valueOf(threshold), pageable)
                .map(productEntityMapper::toModel);
    }

    // ---------- MUTACIONES ----------
    @Override
    @Transactional
    public ProductModel save(ProductModel product) {
        ProductEntity entity = productEntityMapper.toEntity(product);
        ProductEntity saved = productRepository.save(entity);
        return productEntityMapper.toModel(saved);
    }

    @Override
    @Transactional
    public ProductModel updateStock(UUID id, int newStock) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
        entity.setCurrentStock(BigDecimal.valueOf(newStock));
        return productEntityMapper.toModel(productRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }
}