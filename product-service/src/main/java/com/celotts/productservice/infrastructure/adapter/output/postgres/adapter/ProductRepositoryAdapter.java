package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.Product;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;

    @Override
    public List<ProductModel> findAll() {
        return productRepository.findAll()
                .stream()
                .map(Product::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductModel> findById(UUID id) {
        return productRepository.findById(id)
                .map(Product::toModel);
    }

    @Override
    public ProductModel save(ProductModel productModel) {
        Product entity = Product.fromModel(productModel);
        Product saved = productRepository.save(entity);
        return saved.toModel();
    }
}
