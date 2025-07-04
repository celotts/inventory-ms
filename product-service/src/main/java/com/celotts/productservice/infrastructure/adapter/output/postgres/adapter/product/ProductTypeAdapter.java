package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.port.product.ProductTypePort;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductTypeAdapter implements ProductTypePort {
    private final ProductTypeRepository productTypeRepository;

    @Override
    public boolean existsByCode(String code) {
        return productTypeRepository.existsByCode(code);
    }

    @Override
    public Optional<String> findNameByCode(String code) {
        return productTypeRepository.findNameByCode(code);
    }

    @Override
    public List<String> findAllCodes() {
        return productTypeRepository.findAllCodes();
    }

    @Override
    public Optional<ProductTypeEntity> findByCode(String code) {
        return productTypeRepository.findByCode(code);
    }
}