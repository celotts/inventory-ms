package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;           // ✅ AQUÍ van estos imports
import org.springframework.data.domain.Pageable;       // ✅ AQUÍ van estos imports
import java.util.stream.Collectors;                   // ✅ AQUÍ van estos imports

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.ProductRepository;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.ProductEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;

    @Override
    public ProductModel save(ProductModel productModel) {  // ✅ Usar ProductModel
        ProductEntity entity = ProductEntityMapper.toEntity(productModel);
        ProductEntity saved = productRepository.save(entity);
        return ProductEntityMapper.toModel(saved);  // ✅ Usar toModel
    }

    @Override
    public List<ProductModel> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductEntityMapper::toModel)  // ✅ Usar toModel
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductEntityMapper::toModel);
    }

    @Override
    public Optional<ProductModel> findById(UUID id) {
        return productRepository.findById(id)
                .map(ProductEntityMapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return productRepository.existsById(id);
    }

    @Override
    public Optional<ProductModel> findByCode(String code) {
        return productRepository.findByCode(code)
                .map(ProductEntityMapper::toModel);
    }

    @Override
    public boolean existsByCode(String code) {
        return productRepository.existsByCode(code);
    }

    @Override
    public List<ProductModel> findByProductTypeCode(String productTypeCode) {
        return productRepository.findByProductTypeCode(productTypeCode)
                .stream()
                .map(ProductEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> findByBrandId(UUID brandId) {
        return productRepository.findByBrandId(brandId)
                .stream()
                .map(ProductEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> findByEnabled(Boolean enabled) {
        return productRepository.findByEnabled(enabled)
                .stream()
                .map(ProductEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductModel> findByEnabled(Boolean enabled, Pageable pageable) {
        return productRepository.findByEnabled(enabled, pageable)
                .map(ProductEntityMapper::toModel);
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
    public List<ProductModel> findByCurrentStockLessThanMinimumStock() {
        return productRepository.findByCurrentStockLessThanMinimumStock()
                .stream()
                .map(ProductEntityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> findByCurrentStockLessThan(Integer stock) {
        return productRepository.findByCurrentStockLessThan(stock)
                .stream()
                .map(ProductEntityMapper::toModel)
                .collect(Collectors.toList());
    }
}