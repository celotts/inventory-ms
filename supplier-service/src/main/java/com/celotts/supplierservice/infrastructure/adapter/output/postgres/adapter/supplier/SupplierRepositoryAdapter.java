package com.celotts.supplierservice.infrastructure.adapter.output.postgres.adapter.supplier;

import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import com.celotts.supplierservice.domain.port.output.supplier.SupplierRepositoryPort;
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
    public SupplierModel save(SupplierModel supplier){
        SupplierEntity toSave = mapper.toEntity(supplier);
        SupplierEntity saved = jpa.save(toSave);
        return mapper.toModel(saved);
    }

    // ---------- Reads (single) ----------
    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierModel> findById(UUID id) {
        return jpa.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierModel> findByName(String name){
        return jpa.findByNameIgnoreCase(name).map(mapper::toModel);
    }

    // ---------- Reads (lists) ----------
    @Override
    @Transactional(readOnly = true)
    public List<SupplierModel> findAll() {
        return jpa.findAll()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierModel> findAllById(List<UUID> ids) {
        return jpa.findAllById(ids)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierModel> findByNameContaining(String name) {
        return jpa.findByNameContainingIgnoreCase(name)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierModel> findByNameDescription(String query, int limit) {
        int size = Math.max(1, Math.min(limit, 1000)); // límite de seguridad
        Page<SupplierEntity> page = jpa.searchByLooseQuery(query, PageRequest.of(0, size));
        return page.getContent()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    // ---------- Delete ----------
    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpa.deleteById(id);
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

    /**
     * El puerto pide Page sin Pageable. Devolvemos una PageImpl con todos los que
     * cumplen la condición. Recomendación: cambiar el puerto a (Boolean active, Pageable pageable).
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SupplierModel> findByActive(Boolean active) {
        List<SupplierModel> list = jpa.findByEnabled(active)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
        return new PageImpl<>(list, PageRequest.of(0, list.size()), list.size());
    }
}