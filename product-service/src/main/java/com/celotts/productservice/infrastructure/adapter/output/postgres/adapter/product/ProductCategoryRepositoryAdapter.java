package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;
import com.celotts.productservice.domain.port.output.product.ProductCategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductCategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductCategoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional
public class ProductCategoryRepositoryAdapter implements ProductCategoryRepositoryPort {

    private final ProductCategoryJpaRepository repository;
    private final ProductCategoryEntityMapper mapper;

    @Override
    public ProductCategoryModel save(ProductCategoryModel model) {
        ProductCategoryEntity entity = mapper.toEntity(model);
        ProductCategoryEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCategoryModel> findById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCategoryModel> findByProductIdAndCategoryId(UUID productId, UUID categoryId) {
        return repository.findByProductIdAndCategoryId(productId, categoryId).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProductIdAndCategoryId(UUID productId, UUID categoryId) {
        return repository.existsByProductIdAndCategoryId(productId, categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryModel> findAllByProductId(UUID productId) {
        return repository.findAllByProductId(productId)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByProductIdAndCategoryId(UUID productId, UUID categoryId) {
        repository.deleteByProductIdAndCategoryId(productId, categoryId);
    }

    @Override
    public void disableById(UUID id) {
        ProductCategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductCategory not found: " + id));
        entity.setEnabled(false);
        entity.setUpdatedAt(LocalDateTime.now());
        repository.save(entity);
    }

    @Override
    public void enableById(UUID id) {
        ProductCategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductCategory not found: " + id));
        entity.setEnabled(true);
        entity.setUpdatedAt(LocalDateTime.now());
        repository.save(entity);
    }
}