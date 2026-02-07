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
        System.out.println(">>> [DEBUG] PurchaseUseCaseImpl.create() INICIADO");
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
            System.out.println(">>> [DEBUG] Llamando a SupplierClient con ID: " + supplierId);
            SupplierDto supplier = supplierClient.getSupplier(supplierId);
            System.out.println(">>> [DEBUG] Respuesta de SupplierClient: " + supplier);

            if (supplier == null) {
                throw new SupplierNotFoundException("supplier.not-found", "id", supplierId.toString());
            }

            if (!supplier.isActive()) {
                throw new SupplierInactiveException("supplier.inactive", "name", supplier.getName());
            }

        } catch (feign.FeignException.NotFound e) {
            System.err.println(">>> [ERROR] Feign NotFound (404): " + e.getMessage());
            throw new SupplierNotFoundException("supplier.not-found", "id", supplierId.toString());

        } catch (feign.FeignException e) {
            System.err.println(">>> [ERROR CRÍTICO] Falló la llamada a Supplier Service!");
            System.err.println(">>> Status: " + e.status());
            System.err.println(">>> URL: " + e.request().url());
            System.err.println(">>> Body: " + e.contentUTF8());
            e.printStackTrace();
            
            log.error("Error al comunicar con Supplier Service: Status={}, Msg={}", e.status(), e.getMessage(), e);
            throw new RuntimeException("FALLO EN LLAMADA FEIGN - REVISAR LOGS");
        } catch (Exception e) {
            System.err.println(">>> [DEBUG] Clase de la excepción: " + e.getClass().getName());
            System.err.println(">>> [DEBUG] Mensaje: " + e.getMessage());
            e.printStackTrace();

            // CAMBIA ESTO PARA QUE EN POSTMAN VEAS ALGO DIFERENTE
            throw new RuntimeException("ERROR_NUEVO_DETECTADO: " + e.getMessage());
        }
    }
}