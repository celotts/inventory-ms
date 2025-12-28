package com.celotts.purchaseservice.infrastructure.adapter.output.postgres.adapter.purchese;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.output.PurchaseRepositoryPort;
import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.entity.PurchaseEntity;
import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.mapper.purchase.PurchaseEntityMapper;
import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.repository.purchese.PurchaseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("purchaseAdapter")
public class PurchaseRepositoryAdapter implements PurchaseRepositoryPort {

    private final PurchaseJpaRepository jpa;
    private final PurchaseEntityMapper mapper;

    // ---------- Escritura ----------
    @Override
    @Transactional
    public PurchaseModel save(PurchaseModel purchase) {
        PurchaseEntity toSave = mapper.toEntity(purchase);
        PurchaseEntity saved = jpa.save(toSave);
        return mapper.toModel(saved);
    }

    // ---------- Lectura ----------
    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseModel> findById(UUID id) {
        return jpa.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseModel> findByCode(String code) {
        return jpa.findByCodeIgnoreCase(code).map(mapper::toModel);
    }

    // ---------- Verificaciones ----------
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return jpa.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return jpa.existsByNameIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return jpa.existsByCodeIgnoreCase(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseModel> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseModel> findByNameContaining(String name, Pageable pageable) {
        return jpa.findByNameContainingIgnoreCase(name, pageable).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseModel> findByActive(Boolean active) {
        List<PurchaseModel> list = jpa.findByEnabled(active)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());

        return new PageImpl<>(list, PageRequest.of(0, Math.max(1, list.size())), list.size());
    }

    @Override
    public List<PurchaseModel> findByNameDescription(String query, int limit) {
        // Implementación según necesidad de negocio
        return List.of();
    }

    // ---------- Borrado ----------
    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}