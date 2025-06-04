package com.celotts.productservice.infrastructure.adapter.output.postgres.repository;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.ProductEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public boolean existsById(UUID id) {
        return productRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return productRepository.existsByCode(code);
    }

    @Override
    public Optional<ProductModel> findByCode(String code) {
        return productRepository.findByCode(code)
                .map(productEntityMapper::toModel);
    }

    @Override
    public List<ProductModel> findByProductTypeCode(String typeCode) {
        return productRepository.findByProductTypeCode(typeCode).stream()
                .map(productEntityMapper::toModel)
                .toList();
    }

    @Override
    public List<ProductModel> findByBrandId(UUID brandId) {
        return productRepository.findByBrandId(brandId).stream()
                .map(productEntityMapper::toModel)
                .toList();
    }

    @Override
    public Page<ProductModel> findByEnabled(Boolean enabled, Pageable pageable) {
        return productRepository.findByEnabled(enabled, pageable)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findByCurrentStockLessThanMinimumStock(Pageable pageable) {
        return productRepository.findProductsWithLowStock(pageable)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findByCurrentStockLessThan(Integer stock, Pageable pageable) {
        return productRepository.findByCurrentStockLessThan(stock, pageable)
                .map(productEntityMapper::toModel);
    }

    @Override
    public long count() {
        return productRepository.count();
    }

    @Override
    public long countByEnabled(Boolean enabled) {
        return productRepository.countByEnabled(enabled);
    }

    @Override
    public Page<ProductModel> findByProductTypeCode(String productTypeCode, Pageable pageable) {
        return productRepository.findByProductTypeCode(productTypeCode, pageable)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findByBrandId(UUID brandId, Pageable pageable) {
        return productRepository.findByBrandId(brandId, pageable)
                .map(productEntityMapper::toModel);
    }

    @Override
    public ProductModel save(ProductModel product) {
        return productEntityMapper.toModel(
                productRepository.save(productEntityMapper.toEntity(product))
        );
    }

    @Override
    public List<ProductModel> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productEntityMapper::toModel)
                .toList(); // Usa .collect(Collectors.toList()) si estás en Java 8
    }

    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findAllWithFilters(Pageable pageable, String code, String name, String description) {
        return productRepository.findAllWithFilters(code, name, description, pageable)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Optional<ProductModel> findById(UUID id) {
        return productRepository.findById(id)
                .map(productEntityMapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductModel> findProductsWithFilters(Pageable pageable, String code, String name, String description) {
        return productRepository.findProductsWithFilters(pageable, code, name, description)
                .map(productEntityMapper::toModel);
    }

}