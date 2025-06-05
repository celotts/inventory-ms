package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.ProductEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Primary
public class ProductAdapter implements ProductRepositoryPort {

    private final ProductRepository repository;
    private final ProductEntityMapper mapper;

    @Override
    public ProductModel save(ProductModel model) {
        ProductEntity entity = mapper.toEntity(model);
        return mapper.toModel(repository.save(entity));
    }

    @Override
    public List<ProductModel> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toModel);
    }

    @Override
    public Optional<ProductModel> findById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<ProductModel> findByCode(String code) {
        return repository.findByCode(code).map(mapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }

    @Override
    public Page<ProductModel> findAllWithFilters(Pageable pageable, String code, String name, String description) {
        return repository.findAllWithFilters(code, name, description, pageable).map(mapper::toModel);
    }

    @Override
    public List<ProductModel> findByProductTypeCode(String productTypeCode) {
        return repository.findByProductTypeCode(productTypeCode).stream().map(mapper::toModel).toList();
    }

    @Override
    public Page<ProductModel> findByProductTypeCode(String typeCode, Pageable pageable) {
        return repository.findByProductTypeCode(typeCode, pageable).map(mapper::toModel);
    }

    @Override
    public List<ProductModel> findByBrandId(UUID brandId) {
        return repository.findByBrandId(brandId).stream().map(mapper::toModel).toList();
    }

    @Override
    public Page<ProductModel> findByBrandId(UUID brandId, Pageable pageable) {
        return repository.findByBrandId(brandId, pageable).map(mapper::toModel);
    }


    @Override
    public Page<ProductModel> findByEnabled(Boolean enabled, Pageable pageable) {
        return repository.findByEnabled(enabled, pageable)
                .map(mapper::toModel);
    }

    @Override
    public Page<ProductModel> findProductsWithFilters(Pageable pageable, String code, String name, String description) {
        return repository.findProductsWithFilters(pageable, code, name, description).map(mapper::toModel);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long countByEnabled(Boolean enabled) {
        return repository.countByEnabled(enabled);
    }

    @Override
    public Page<ProductModel> findByCurrentStockLessThanMinimumStock(Pageable pageable) {
        return repository.findProductsWithLowStock(pageable).map(mapper::toModel);
    }

    @Override
    public Page<ProductModel> findByCurrentStockLessThan(Integer stock, Pageable pageable) {
        return repository.findByCurrentStockLessThan(stock, pageable).map(mapper::toModel);
    }
}