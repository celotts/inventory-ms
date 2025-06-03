package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.stream.Collectors;

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
@Slf4j
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;  // ✅ AGREGAR ESTA LÍNEA

    @Override
    public ProductModel save(ProductModel productModel) {
        ProductEntity entity = productEntityMapper.toEntity(productModel);  // ✅ CORREGIDO: instancia
        ProductEntity saved = productRepository.save(entity);  // ✅ CORREGIDO: instancia
        return productEntityMapper.toModel(saved);  // ✅ CORREGIDO: instancia
    }

    @Override
    public List<ProductModel> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productEntityMapper::toModel)  // ✅ CORREGIDO: instancia
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productEntityMapper::toModel);  // ✅ CORREGIDO: instancia
    }

    @Override
    public Optional<ProductModel> findById(UUID id) {
        return productRepository.findById(id)
                .map(productEntityMapper::toModel);  // ✅ CORREGIDO: instancia
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
                .map(productEntityMapper::toModel);  // ✅ CORREGIDO: instancia
    }

    @Override
    public boolean existsByCode(String code) {
        return productRepository.existsByCode(code);
    }

    @Override
    public List<ProductModel> findByProductTypeCode(String productTypeCode) {
        return productRepository.findByProductTypeCode(productTypeCode)
                .stream()
                .map(productEntityMapper::toModel)  // ✅ CORREGIDO: instancia
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> findByBrandId(UUID brandId) {
        return productRepository.findByBrandId(brandId)
                .stream()
                .map(productEntityMapper::toModel)  // ✅ CORREGIDO: instancia
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> findByEnabled(Boolean enabled) {
        return productRepository.findByEnabled(enabled)
                .stream()
                .map(productEntityMapper::toModel)  // ✅ CORREGIDO: instancia
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductModel> findByEnabled(Boolean enabled, Pageable pageable) {
        return productRepository.findByEnabled(enabled, pageable)
                .map(productEntityMapper::toModel);  // ✅ CORREGIDO: instancia
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
                .map(productEntityMapper::toModel)  // ✅ CORREGIDO: instancia
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> findByCurrentStockLessThan(Integer stock) {
        return productRepository.findByCurrentStockLessThan(stock)
                .stream()
                .map(productEntityMapper::toModel)  // ✅ CORREGIDO: instancia
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductModel> findProductsWithFilters(Pageable pageable, String code, String name, String description) {
        log.info("ProductRepositoryAdapter: Searching products with filters - code: {}, name: {}, description: {}",
                code, name, description);

        Page<ProductEntity> productEntities = productRepository.findProductsWithFilters(pageable, code, name, description);
        return productEntities.map(productEntityMapper::toModel);  // ✅ CORREGIDO: instancia
    }
}