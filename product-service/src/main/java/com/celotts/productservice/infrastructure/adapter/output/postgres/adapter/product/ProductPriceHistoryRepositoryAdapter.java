// src/main/java/com/celotts/productservice/infrastructure/adapter/output/postgres/adapter/product/ProductPriceHistoryRepositoryAdapter.java
package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.product.ProductPriceHistoryModel;
import com.celotts.productservice.domain.port.output.product.ProductPriceHistoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductPriceHistoryEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductPriceHistoryEntityMapper; // <- interface @Mapper
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductPriceHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional
public class ProductPriceHistoryRepositoryAdapter implements ProductPriceHistoryRepositoryPort {

    private final ProductPriceHistoryJpaRepository repository;
    private final ProductPriceHistoryEntityMapper mapper;

    @Override
    public ProductPriceHistoryModel save(ProductPriceHistoryModel model) {
        ProductPriceHistoryEntity entity = mapper.toEntity(model);
        ProductPriceHistoryEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPriceHistoryModel> findAllByProductIdOrderByChangedAtDesc(UUID productId) {
        return repository.findAllByProduct_IdOrderByChangedAtDesc(productId)
                .stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductPriceHistoryModel> findTopByProductIdOrderByChangedAtDesc(UUID productId) {
        return repository.findTopByProduct_IdOrderByChangedAtDesc(productId)
                .map(mapper::toModel);
    }
}