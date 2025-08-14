package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productserviceOld.domain.model.ProductCategoryModel;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.product.ProductCategoryEntityMapper;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository.product.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductCategoryAdapter {

    private final ProductCategoryRepository repository;
    private final ProductCategoryEntityMapper mapper;

    public ProductCategoryModel save(ProductCategoryModel model) {
        ProductCategoryEntity entity = mapper.toEntity(model);
        return mapper.toModel(repository.save(entity));
    }

    public ProductCategoryModel getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toModel)
                .orElseThrow(() -> new RuntimeException("ProductCategory not found: " + id));
    }

    public List<ProductCategoryModel> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public void disableById(UUID id) {
        ProductCategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductCategory not found: " + id));
        entity.setEnabled(false);
        entity.setUpdatedAt(java.time.LocalDateTime.now());
        repository.save(entity);
    }
}