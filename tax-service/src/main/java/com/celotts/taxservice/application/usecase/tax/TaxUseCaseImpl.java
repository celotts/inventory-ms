package com.celotts.taxservice.application.usecase.tax;

import com.celotts.taxservice.domain.model.TaxModel;
import com.celotts.taxservice.domain.port.input.TaxUseCase;
import com.celotts.taxservice.domain.port.output.TaxRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaxUseCaseImpl implements TaxUseCase {

    private final TaxRepositoryPort taxRepository;
    @Override
    @Transactional
    public TaxModel save(TaxModel tax) {
        log.info("Saving tax: {}", tax.getCode());

        if (tax.getId() == null) {
            // Validar que el código no exista
            if (taxRepository.existsByCode(tax.getCode())) {
                throw new IllegalArgumentException("Tax with code '" + tax.getCode() + "' already exists");
            }
        }

        return taxRepository.save(tax);
    }

    @Override
    public Optional<TaxModel> findById(UUID id) {
        log.debug("Finding tax by id: {}", id);
        return taxRepository.findById(id);
    }

    @Override
    public Optional<TaxModel> findByName(String name) {
        log.debug("Finding tax by name: {}", name);
        return taxRepository.findByName(name);
    }

    @Override
    public List<TaxModel> findAll() {
        log.debug("Finding all taxes");
        return taxRepository.findAll();
    }

    @Override
    public List<TaxModel> findAllById(List<UUID> ids) {
        log.debug("Finding taxes by ids: {}", ids);
        return taxRepository.findById(ids);
    }

    @Override
    public List<TaxModel> finjdByNameContaining(String name) {
        log.debug("Finding taxes by name containing: {}", name);
        return taxRepository.findByNameContaining(name);
    }

    @Override
    public List<TaxModel> searchByNameOrDescriptionm(String query, int limit) {
        log.debug("Searching taxes by name or code: {}", query);
        return taxRepository.findByNameOrDescription(query, limit);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        log.info("Deleting tax by id: {}", id);

        if (!taxRepository.existsById(id)) {
            throw new IllegalArgumentException("Tax not found with id: " + id);
        }

        taxRepository.deleteById(id);
    }

    @Override
    public boolean existById(UUID id) {
        log.debug("Checking if tax exists by id: {}", id);
        return taxRepository.existsById(id);
    }

    @Override
    public boolean existByName(String name) {
        log.debug("Checking if tax exists by name: {}", name);
        return taxRepository.existsByName(name);
    }

    @Override
    public Page<TaxModel> findAll(Pageable pageable) {
        log.debug("Finding all taxes with pagination");
        return taxRepository.findAll(pageable);
    }

    @Override
    public Page<TaxModel> findByNameContaining(String name, Pageable pageable) {
        log.debug("Finding taxes by name containing: {}", name);
        return taxRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<TaxModel> findByActive(Boolean active, Pageable pageable) {
        log.debug("Finding taxes by active status: {}", active);
        return taxRepository.findByActive(active, pageable);
    }

    @Override
    public Page<TaxModel> findByNameContainingAndActive(String name, Boolean active, Pageable pageable) {
        log.debug("Finding taxes by name and active status");
        return taxRepository.findByNameContainingAndActive(name, active, pageable);
    }

    @Override
    public Page<TaxModel> findAllPaginated(String name, Boolean active, Pageable pageable) {
        log.debug("Finding all taxes paginated with name: {} and active: {}", name, active);
        return taxRepository.findAllPaginated(name, active, pageable);
    }

    @Override
    @Transactional
    public TaxModel create(TaxModel model) {
        log.info("Creating new tax: {}", model.getCode());

        // Validaciones
        if (taxRepository.existsByCode(model.getCode())) {
            throw new IllegalArgumentException("Tax with code '" + model.getCode() + "' already exists");
        }

        if (model.getIsActive() == null) {
            model.setIsActive(true);
        }

        return taxRepository.save(model);
    }


}
