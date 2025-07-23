package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductUnitEntityMapper;  // ← import corregido
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductUnitRepositoryAdapter implements ProductUnitRepositoryPort {

    private final ProductUnitRepository   jpaRepo;
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

    /* ---------- CRUD completo ---------- */

    @Override
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
    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }

    @Override
    public List<ProductUnitModel> findAll() {
        return jpaRepo.findAll()
                .stream()
                .map(mapper::toModel)
                .toList();
    }
}