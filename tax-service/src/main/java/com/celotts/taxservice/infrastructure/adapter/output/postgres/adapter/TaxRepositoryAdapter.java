package com.celotts.taxservice.infrastructure.adapter.output.postgres.adapter;

import com.celotts.taxservice.domain.model.TaxModel;
import com.celotts.taxservice.domain.port.output.TaxRepositoryPort;
import com.celotts.taxservice.infrastructure.adapter.output.postgres.entity.TaxEntity;
import com.celotts.taxservice.infrastructure.adapter.output.postgres.mapper.tax.TaxEntityMapper;
import com.celotts.taxservice.infrastructure.adapter.output.postgres.repository.tax.TaxJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component("taxAdapter")
public class TaxRepositoryAdapter implements TaxRepositoryPort {

    private final TaxJpaRepository jpa;
    private final TaxEntityMapper mapper;

    @Override
    public TaxModel save(TaxModel tax) {
        log.debug("Saving tax: {}", tax.getName());
        TaxEntity entity = mapper.toEntity(tax);
        TaxEntity saved = jpa.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<TaxModel> findById(UUID id) {
        log.debug("Finding tax by id: {}", id);
        return jpa.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<TaxModel> findByName(String name) {
        log.debug("Finding tax by name: {}", name);
        return jpa.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<TaxModel> findByCode(String code) {
        log.debug("Finding tax by code: {}", code);
        return jpa.findByCode(code)
                .map(mapper::toDomain);
    }

    @Override
    public List<TaxModel> findAll() {
        log.debug("Finding all taxes");
        return jpa.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaxModel> findById(List<UUID> ids) {
        log.debug("Finding taxes by ids: {}", ids);
        return jpa.findAllById(ids)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaxModel> findByNameContaining(String name) {
        log.debug("Finding taxes by name containing: {}", name);
        return jpa.findByNameIgnoreCase(name, Pageable.unpaged())
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaxModel> findByNameOrDescription(String query, int limit) {
        log.debug("Finding taxes by name or code containing: {}", query);
        return jpa.findByCodeIgnoreCase(query, Pageable.unpaged())
                .getContent()
                .stream()
                .limit(limit)
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        log.debug("Deleting tax by id: {}", id);
        jpa.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        log.debug("Checking if tax exists by id: {}", id);
        return jpa.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        log.debug("Checking if tax exists by name: {}", name);
        return jpa.existsByName(name);
    }

    @Override
    public boolean existsByCode(String code) {
        log.debug("Checking if tax exists by code: {}", code);
        return jpa.existsByCode(code);
    }

    @Override
    public Page<TaxModel> findAll(Pageable pageable) {
        log.debug("Finding all taxes with pagination");
        Page<TaxEntity> page = jpa.findAll(pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public Page<TaxModel> findByNameContaining(String name, Pageable pageable) {
        log.debug("Finding taxes by name containing: {}", name);
        Page<TaxEntity> page = jpa.findByNameIgnoreCase(name, pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public Page<TaxModel> findByActive(Boolean active, Pageable pageable) {
        log.debug("Finding taxes by active status: {}", active);
        Page<TaxEntity> page = jpa.findByIsActive(active, pageable);
        return page.map(mapper::toDomain);
    }

    @Override
    public Page<TaxModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        log.debug("Finding taxes by name containing: {} and active: {}", name, active);
        return jpa.findAllWithFilters(name, name, active, null, null, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<TaxModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        log.debug("Finding all taxes paginated with name: {} and active: {}", name, active);
        return jpa.findAllWithFilters(name, name, active, null, null, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<TaxModel> findAllPaginated(String name, boolean active, Pageable pageable) {
        log.debug("Finding all taxes paginated with name: {} and active: {}", name, active);
        return jpa.findAllWithFilters(name, name, active, null, null, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public long count() {
        log.debug("Counting all taxes");
        return jpa.count();
    }

    @Override
    public long countByActive(boolean active) {
        log.debug("Counting taxes by active status: {}", active);
        return jpa.countByIsActive(active);
    }
}