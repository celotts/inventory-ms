package com.celotts.purchaseservice.application.usecase;

import com.celotts.purchaseservice.domain.exception.PurchaseAlreadyExistsException;
import com.celotts.purchaseservice.domain.exception.PurchaseNotFoundException;
import com.celotts.purchaseservice.domain.exception.SupplierInactiveException;
import com.celotts.purchaseservice.domain.exception.SupplierNotFoundException;
import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.input.PurchaseUseCase;
import com.celotts.purchaseservice.domain.port.output.PurchaseRepositoryPort;

import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.supplier.SupplierDto;
import com.celotts.purchaseservice.infrastructure.client.SupplierClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseUseCaseImpl implements PurchaseUseCase {

    private final PurchaseRepositoryPort repositoryPort;
    private final MessageSource messageSource; // ✅ Inyectado para i18n
    private final SupplierClient supplierClient;


    // Método auxiliar para obtener mensajes del properties
    private String getMsg(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    public PurchaseModel create(PurchaseModel purchase) {
        purchase.normalize();

        validateSupplier(purchase.getSupplierId());

        if (purchase.getCreatedBy() == null || purchase.getCreatedBy().isBlank()) {
            purchase.setCreatedBy(
                    messageSource.getMessage("app.user.default", null, LocaleContextHolder.getLocale())
            );
        }

        if (repositoryPort.existsByOrderNumber(purchase.getOrderNumber())) {
            throw new PurchaseAlreadyExistsException(
                    "purchase.already-exists",
                    "orderNumber",
                    purchase.getOrderNumber()
            );
        }
        return repositoryPort.save(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseModel> findById(UUID id) {
        return repositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseModel> findAll(Pageable pageable) {
        return repositoryPort.findAll(pageable);
    }

    @Override
    @Transactional
    public PurchaseModel update(UUID id, PurchaseModel purchase) {
        return repositoryPort.findById(id)
                .map(existingPurchase -> {
                    purchase.setId(id);
                    if(purchase.getUpdatedBy() == null) {
                        throw new IllegalArgumentException(
                                messageSource.getMessage("purchase.update.updatedBy.required", null, LocaleContextHolder.getLocale())
                        );
                    }
                    if (purchase.getSupplierId() == null) {
                        purchase.setSupplierId(existingPurchase.getSupplierId());
                    }
                    purchase.normalize();
                    return repositoryPort.save(purchase);
                })
                .orElseThrow(() -> {
                    // Usamos la clave específica que ya tienes en tu messages.properties
                    String errorMsg = messageSource.getMessage("purchase.cannot-update-not-found", new Object[]{id}, LocaleContextHolder.getLocale());
                    return new PurchaseNotFoundException(errorMsg, id);
                });
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!repositoryPort.existsById(id)) {
            throw new PurchaseNotFoundException(
                    getMsg("app.error.not-found") + ": " + id, id);
        }
        repositoryPort.deleteById(id);
    }

    private void validateSupplier(UUID supplierId) {
        try {
            SupplierDto supplier = supplierClient.getSupplier(supplierId);
            if (supplier == null) {
                throw new SupplierNotFoundException("supplier.not-found", "id", supplierId.toString());
            }
            if (!supplier.isActive()) {
                throw new SupplierInactiveException("supplier.inactive", "id", supplierId.toString());
            }
        } catch (feign.FeignException.NotFound e) {
            // Captura específicamente el 404 de Feign
            throw new SupplierNotFoundException("supplier.not-found", "id", supplierId.toString());
        } catch (feign.FeignException e) {
            // Error de conexión o servidor (500, timeout, etc)
            throw new RuntimeException(messageSource.getMessage("error.communication.supplier-service", null, LocaleContextHolder.getLocale())
            );
        }
    }


}