package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.domain.port.ProductCategoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductCategory;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.ProductCategoryEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductCategoryAdapter implements ProductCategoryPort {

    private final ProductCategoryRepository repository;
    private final ProductCategoryEntityMapper mapper;

    @Override
    public ProductCategoryModel create(ProductCategoryModel model) {
        ProductCategory entity = repository.save(mapper.toEntity(model));
        return mapper.toModel(entity);
    }

    @Override
    public List<ProductCategoryModel> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Optional<ProductCategoryModel> findById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<ProductCategoryModel> findByProductId(UUID productId) {
        return repository.findByProductId(productId).stream().map(mapper::toModel).toList();
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}