package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.output.product.ProductBrandRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductBrandEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductBrandJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("productBrandAdapter")
@RequiredArgsConstructor
public class ProductBrandRepositoryAdapter implements ProductBrandRepositoryPort {

    private final ProductBrandJpaRepository repository;
    private final ProductBrandEntityMapper mapper;

    @Override
    public ProductBrandModel save(ProductBrandModel model) {
        ProductBrandEntity saved = repository.save(mapper.toEntity(model));
        return mapper.toModel(saved);
    }

    @Override
    public Optional<ProductBrandModel> findById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<ProductBrandModel> findByName(String name) {
        return repository.findByNameIgnoreCase(name).map(mapper::toModel);
    }

    @Override
    public List<ProductBrandModel> findAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    // ----- extras del puerto -----

    @Override
    public Optional<String> findNameById(UUID id) {
        return repository.findNameById(id);
    }

    @Override
    public List<UUID> findAllIds() {
        return repository.findAllIds();
    }

    // ----- enable/disable -----

    @Override
    @Transactional
    public ProductBrandModel enable(UUID id) {
        repository.enableBrandById(id);
        ProductBrandEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductBrand not found: " + id));
        return mapper.toModel(entity);
    }

    @Override
    @Transactional
    public ProductBrandModel disable(UUID id) {
        repository.disableBrandById(id);
        ProductBrandEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductBrand not found: " + id));
        return mapper.toModel(entity);
    }
}