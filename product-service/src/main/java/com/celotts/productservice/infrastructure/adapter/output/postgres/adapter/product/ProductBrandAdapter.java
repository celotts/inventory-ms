package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.port.product.ProductBrandPort;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductBrandAdapter implements ProductBrandPort {

    private final ProductBrandRepository productBrandRepository;

    @Override
    public boolean existsById(UUID id) {
        return productBrandRepository.existsById(id);
    }

    @Override
    public Optional<String> findNameById(UUID id) {
        return productBrandRepository.findNameById(id);
    }

    @Override
    public List<UUID> findAllIds() {
        return productBrandRepository.findAll().stream()
                .map(ProductBrandEntity::getId)
                .toList();
    }
}