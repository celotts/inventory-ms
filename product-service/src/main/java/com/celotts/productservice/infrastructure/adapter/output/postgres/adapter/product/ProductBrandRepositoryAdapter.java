package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.prodcut_brand.ProductBrandRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductBrandEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductBrandRepositoryAdapter implements ProductBrandRepositoryPort {

    private final ProductBrandRepository productBrandRepository;
    private final ProductBrandEntityMapper entityMapper;

    @Override
    public ProductBrandModel save(ProductBrandModel productBrand) {
        ProductBrandEntity entity = entityMapper.toEntity(productBrand);
        ProductBrandEntity saved = productBrandRepository.save(entity);
        return entityMapper.toModel(saved);
    }

    @Override
    public Optional<ProductBrandModel> findById(UUID id) {
        return productBrandRepository.findById(id)
                .map(entityMapper::toModel);
    }

    @Override
    public Optional<ProductBrandModel> findByName(String name) {
        return productBrandRepository.findByName(name)
                .map(entityMapper::toModel);
    }

    @Override
    public List<ProductBrandModel> findAll() {
        return productBrandRepository.findAll()
                .stream()
                .map(entityMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return productBrandRepository.existsByName(name);
    }

    @Override
    public boolean existsById(UUID id) {
        return productBrandRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        productBrandRepository.deleteById(id);
    }
}