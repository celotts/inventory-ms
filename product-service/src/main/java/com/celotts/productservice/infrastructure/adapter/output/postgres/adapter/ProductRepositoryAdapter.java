package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.ProductEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
    public Optional<ProductModel> findById(UUID id) {
        return productRepository.findById(id)
                .map(productEntityMapper::toModel);
    }

    @Override
    public ProductModel save(ProductModel model) {
        ProductEntity entity = productEntityMapper.toEntity(model);
        return productEntityMapper.toModel(productRepository.save(entity));
    }

    @Override
    public List<ProductModel> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productEntityMapper::toModel)
                .toList();
    }

    // Puedes agregar más métodos aquí según tus necesidades
}