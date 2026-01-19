package com.celotts.supplierservice.infrastructure.adapter.output.postgres.adapter.supplier;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import com.celotts.supplierservice.domain.port.output.SupplierRepositoryPort;
import com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity.supplier.SupplierEntity;
import com.celotts.supplierservice.infrastructure.adapter.output.postgres.mapper.supplier.SupplierEntityMapper;
import com.celotts.supplierservice.infrastructure.adapter.output.postgres.repository.supplier.SupplierJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component("supplierAdapter")
public class SupplierRepositoryAdapter implements SupplierRepositoryPort {

    private final SupplierJpaRepository jpa;
    private final SupplierEntityMapper mapper;

    // ---------- Writes ----------
    @Override
    @Transactional
    public SupplierModel save(SupplierModel supplier) {
        SupplierEntity toSave = mapper.toEntity(supplier);
        SupplierEntity saved = jpa.save(toSave);
        return mapper.toModel(saved);
    }



    // ---------- Reads ----------
    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierModel> findById(UUID id) {
        return jpa.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierModel> findByCode(String code) {
        return jpa.findByCodeIgnoreCase(code).map(mapper::toModel);
    }

    // ---------- Exists ----------
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

    // ---------- Delete ----------
    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    // ---------- Pages ----------
    @Override
    @Transactional(readOnly = true)
    public Page<SupplierModel> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierModel> findByNameContaining(String name, Pageable pageable) {
        return jpa.findByNameContainingIgnoreCase(name, pageable)
                .map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierModel> findByActive(Boolean active) {
        List<SupplierModel> list = jpa.findByEnabled(active)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());

        return new PageImpl<>(list, PageRequest.of(0, list.size()), list.size());
    }

    // ---------- Search (suggestions / autocomplete) ----------
    @Override
    @Transactional(readOnly = true)
    public List<SupplierModel> findByNameDescription(String query, int limit) {
        int size = Math.max(1, Math.min(limit, 1000));

        if (query == null || query.isBlank()) {
            return jpa.findAll(PageRequest.of(0, size))
                    .map(mapper::toModel)
                    .getContent();
        }

        return jpa.searchByLooseQuery(query, PageRequest.of(0, size))
                .map(mapper::toModel)
                .getContent();
    }
}