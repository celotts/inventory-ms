package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.port.output.product.ProductTypePort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTypeJpaRepository; // 👈 JPA repo

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional; // ✅
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductTypeRepositoryAdapter implements ProductTypePort {
    private final ProductTypeJpaRepository productTypeRepository;

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