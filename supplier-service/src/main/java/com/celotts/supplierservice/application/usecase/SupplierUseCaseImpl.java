package com.celotts.supplierservice.application.usecase;

import com.celotts.supplierservice.domain.exception.SupplierAlreadyExistsException;
import com.celotts.supplierservice.domain.exception.SupplierNotFoundException;
import com.celotts.supplierservice.domain.model.supplier.SupplierModel;
import com.celotts.supplierservice.domain.port.output.supplier.SupplierRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierUseCaseImpl implements SupplierUseCase {

    private final SupplierRepositoryPort repo;

    // ---------------- Commands ----------------

    @Override
    public SupplierModel create(SupplierModel supplier) {
        // (Opcional) normaliza aquí si tu mapper/DTO no lo hizo
        // normalize(supplier);

        // Unicidad por code (recomendado)
        if (supplier.getCode() != null && repo.existsByCode(supplier.getCode())) {
            // Si usas i18n en excepciones: new SupplierAlreadyExistsException("supplier.already-exists", "code", supplier.getCode())
            throw new SupplierAlreadyExistsException("code", supplier.getCode());
        }
        return repo.save(supplier);
    }

    @Override
    public SupplierModel update(UUID id, SupplierModel partial) {
        SupplierModel current = repo.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException(id));

        // (Opcional) normalize(partial);

        // ✔️ evita colisión de code con otros registros
        if (partial.getCode() != null) {
            boolean codeChanged = current.getCode() == null
                    || !current.getCode().equalsIgnoreCase(partial.getCode());
            if (codeChanged) {
                // a) si tu port ya expone existsByCodeExceptId, úsalo (mejor):
                // if (repo.existsByCodeExceptId(partial.getCode(), id)) ...
                // b) si NO lo tienes, usa existsByCode + compara con el actual:
                if (repo.existsByCode(partial.getCode())) {
                    throw new SupplierAlreadyExistsException("code", partial.getCode());
                }
            }
        }

        // merge “partial” → “current”
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
        // FUTURO: si quieres persistir deleted_by / deleted_reason antes de soft-delete,
        // agrega método custom en el repo.
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

    // (Opcional) normalizador simple
    // private void normalize(SupplierModel m) {
    //     if (m.getCode() != null)    m.setCode(m.getCode().trim().toUpperCase());
    //     if (m.getEmail() != null)   m.setEmail(m.getEmail().trim().toLowerCase());
    //     if (m.getName() != null)    m.setName(m.getName().trim().replaceAll("\\s+"," "));
    //     if (m.getPhone() != null)   m.setPhone(m.getPhone().trim().replaceAll("\\s+"," "));
    //     if (m.getAddress() != null) m.setAddress(m.getAddress().trim().replaceAll("\\s+"," "));
    // }
}