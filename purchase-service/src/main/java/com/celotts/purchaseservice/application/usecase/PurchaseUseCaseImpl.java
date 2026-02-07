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
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PurchaseUseCaseImpl implements PurchaseUseCase {

    private final PurchaseRepositoryPort repositoryPort;
    private final MessageSource messageSource;
    private final SupplierClient supplierClient;


    private String getMsg(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    public PurchaseModel create(PurchaseModel purchase) {
        log.info(">>> [DEBUG] Iniciando creación de compra: {}", purchase.getOrderNumber());
        purchase.normalize();

        log.info(">>> [DEBUG] Validando proveedor ID: {}", purchase.getSupplierId());
        validateSupplier(purchase.getSupplierId());
        log.info(">>> [DEBUG] Proveedor validado con éxito");

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

        log.info(">>> [DEBUG] Guardando compra en base de datos...");
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
                    if (purchase.getUpdatedBy() == null || purchase.getUpdatedBy().isBlank()) {
                        throw new IllegalArgumentException(getMsg("purchase.update.updatedBy.required"));
                    }
                    
                    if (purchase.getSupplierId() != null && !purchase.getSupplierId().equals(existingPurchase.getSupplierId())) {
                        validateSupplier(purchase.getSupplierId());
                    }
                    else if (purchase.getSupplierId() == null) {
                        purchase.setSupplierId(existingPurchase.getSupplierId());
                    }

                    purchase.setCreatedBy(existingPurchase.getCreatedBy());
                    purchase.normalize();
                    return repositoryPort.save(purchase);
                })
                .orElseThrow(() -> new PurchaseNotFoundException(getMsg("purchase.cannot-update-not-found", id), id));
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
            log.debug(">>> [DEBUG] Llamando a SupplierClient con ID: {}", supplierId);
            SupplierDto supplier = supplierClient.getSupplier(supplierId);
            log.debug(">>> [DEBUG] Respuesta de SupplierClient: {}", supplier);

            if (supplier == null) {
                throw new SupplierNotFoundException("supplier.not-found", "id", supplierId.toString());
            }

            if (!supplier.isActive()) {
                throw new SupplierInactiveException("supplier.inactive", "name", supplier.getName());
            }

        } catch (FeignException.NotFound e) {
            log.error(">>> [ERROR] Feign NotFound (404): {}", e.getMessage());
            throw new SupplierNotFoundException("supplier.not-found", "id", supplierId.toString());

        } catch (FeignException e) {
            log.error(">>> [ERROR CRÍTICO] Falló la llamada a Supplier Service! Status: {}, URL: {}, Body: {}", 
                    e.status(), e.request().url(), e.contentUTF8(), e);
            throw new RuntimeException("service.supplier.unavailable");
        } catch (Exception e) {
            log.error(">>> [ERROR] Error inesperado al validar proveedor: {}", e.getMessage(), e);
            throw e;
        }
    }
}