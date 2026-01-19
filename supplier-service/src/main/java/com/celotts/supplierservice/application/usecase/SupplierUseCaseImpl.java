package com.celotts.supplierservice.application.usecase;

import com.celotts.supplierservice.domain.port.input.SupplierUseCase; // <-- ESTE
import com.celotts.supplierservice.domain.port.output.SupplierRepositoryPort;

import com.celotts.supplierservice.domain.exception.SupplierAlreadyExistsException;
import com.celotts.supplierservice.domain.exception.SupplierNotFoundException;
import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierUseCaseImpl implements SupplierUseCase {

    private final SupplierRepositoryPort repo;
    private final MessageSource messageSource;

    // ---------------- Commands ----------------

    @Override
    public SupplierModel create(SupplierModel supplier) {
        // Unicidad por code
        if (supplier.getCode() != null && repo.existsByCode(supplier.getCode())) {
            throw new SupplierAlreadyExistsException("field.code", supplier.getCode());
        }
        return repo.save(supplier);
    }


    @Override
    public SupplierModel update(UUID id, SupplierModel partial) {
        SupplierModel current = repo.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));
        // ✔️ evita colisión de code con otros registros
        if (partial.getCode() != null) {
            boolean codeChanged = current.getCode() == null
                    || !current.getCode().equalsIgnoreCase(partial.getCode());
            if (codeChanged) {

                if (repo.existsByCode(partial.getCode())) {
                    throw new SupplierAlreadyExistsException("field.code", partial.getCode());
                }
            }
        }

        if (partial.getCode() != null)     current.setCode(partial.getCode());
        if (partial.getName() != null)     current.setName(partial.getName());
        if (partial.getTaxId() != null)    current.setTaxId(partial.getTaxId());
        if (partial.getEmail() != null)    current.setEmail(partial.getEmail());
        if (partial.getPhone() != null)    current.setPhone(partial.getPhone());
        if (partial.getAddress() != null)  current.setAddress(partial.getAddress());
        if (partial.getEnabled() != null)  current.setEnabled(partial.getEnabled());

        return repo.save(current);
    }

    @Override
    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new SupplierNotFoundException(id);
        }
        repo.deleteById(id);
    }

    @Override
    public void delete(UUID id, String deletedBy, String reason) {

        delete(id);
    }

    // ---------------- Queries ----------------

    @Override
    @Transactional(readOnly = true)
    public SupplierModel getById(UUID id) {
        return repo.findById(id).orElseThrow(() -> new SupplierNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierModel> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierModel> findByNameContaining(String q, Pageable pageable) {
        return repo.findByNameContaining(q, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierModel> findByActive(Boolean active) {
        return repo.findByActive(active);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierModel> searchByNameDescription(String q, int limit) {
        return repo.findByNameDescription(q, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return repo.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierModel getByCode(String code) {
        return repo.findByCode(code)
                .orElseThrow(() -> new SupplierNotFoundException("field.code", code));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return repo.existsByCode(code);
    }

    private String translateField(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }


}