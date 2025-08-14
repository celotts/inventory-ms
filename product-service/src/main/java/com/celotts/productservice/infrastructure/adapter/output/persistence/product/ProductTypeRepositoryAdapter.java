package com.celotts.productservice.infrastructure.adapter.output.persistence.product;

import com.celotts.productservice.domain.port.output.product.ProductTypeRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeJpaRepository;

import java.util.List;
import java.util.Optional;

public class ProductTypeRepositoryAdapter implements ProductTypeRepositoryPort {
    private final ProductTypeJpaRepository jpa; // Spring Data

    public ProductTypeRepositoryAdapter(ProductTypeJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public boolean existsByCode(String code) {
        return jpa.existsByCode(code);
    }

    @Override
    public Optional<String> findNameByCode(String code) {
        return jpa.findNameByCode(code);
    }

    @Override
    public List<String> findAllCodes() {
        return jpa.findAllCodes();
    }
}
