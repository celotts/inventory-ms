package com.celotts.purchaseservice.infrastructure.adapter.output.postgres.adapter;

import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.output.PurchaseRepositoryPort;
import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.entity.PurchaseEntity;
import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.mapper.PurchaseEntityMapper;
import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.repository.PurchaseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component("purchaseAdapter")
public class PurchaseRepositoryAdapter implements PurchaseRepositoryPort {

    private final PurchaseJpaRepository jpa;
    private final PurchaseEntityMapper mapper;

    @Override
    @Transactional
    public PurchaseModel save(PurchaseModel purchase) {
        try {
            if (purchase.getId() != null) {
                return jpa.findById(purchase.getId())
                        .map(existingEntity -> {
                            mapper.updateEntityFromModel(purchase, existingEntity);

                            return mapper.toModel(jpa.saveAndFlush(existingEntity));
                        })
                        .orElseGet(() -> createNew(purchase));
            }
            return createNew(purchase);
        } catch (Exception e) {
             throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "ERROR REAL: " + e.getMessage() + " - CAUSA: " + (e.getCause() != null ? e.getCause().getMessage() : "Desconocida")
            );
        }
    }

    // Método privado para manejar la creación desde cero
    private PurchaseModel createNew(PurchaseModel purchase) {
        PurchaseEntity toSave = mapper.toEntity(purchase);
        return mapper.toModel(jpa.save(toSave));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseModel> findById(UUID id) {
        return jpa.findById(id).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseModel> findByOrderNumber(String orderNumber) {
        return jpa.findByOrderNumberIgnoreCase(orderNumber).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return jpa.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByOrderNumber(String orderNumber) {
        return jpa.existsByOrderNumberIgnoreCase(orderNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseModel> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toModel);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}