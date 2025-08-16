package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.output.product.ProductUnitRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductUnitEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductUnitJpaRepository; // ✅
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional; // ✅ Spring, no jakarta

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductUnitRepositoryAdapter implements ProductUnitRepositoryPort {

    private final ProductUnitJpaRepository jpaRepo;   // ✅ tipo correcto
    private final ProductUnitEntityMapper mapper;

    /* ---------- lecturas “light” ---------- */

    @Override
    public boolean existsByCode(String code) {
        return jpaRepo.existsByCode(code);
    }

    @Override
    public Optional<String> findNameByCode(String code) {
        return jpaRepo.findNameByCode(code);
    }

    @Override
    public List<String> findAllCodes() {
        return jpaRepo.findAllCodes();
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepo.existsById(id);
    }

    /* ---------- CRUD completo ---------- */

    @Override
    @Transactional
    public ProductUnitModel save(ProductUnitModel model) {
        ProductUnitEntity entity = mapper.toEntity(model);
        ProductUnitEntity saved  = jpaRepo.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<ProductUnitModel> findById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }

    @Override
    public List<ProductUnitModel> findAll() {
        return jpaRepo.findAll().stream().map(mapper::toModel).toList();
    }
}