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
        System.out.println(">>> [DEBUG] Iniciando creación de compra: " + purchase.getOrderNumber());
        purchase.normalize();

        try {
            System.out.println(">>> [DEBUG] Validando proveedor ID: " + purchase.getSupplierId());
            validateSupplier(purchase.getSupplierId());
            System.out.println(">>> [DEBUG] Proveedor validado con éxito");
        } catch (Exception e) {
            System.err.println(">>> [ERROR] Falló la validación del proveedor:");
            e.printStackTrace(); // Esto imprimirá el error real en los logs de Podman
            throw e;
        }

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

        System.out.println(">>> [DEBUG] Guardando compra en base de datos...");
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
                    // Validación de quién actualiza
                    if (purchase.getUpdatedBy() == null || purchase.getUpdatedBy().isBlank()) {
                        throw new IllegalArgumentException(getMsg("purchase.update.updatedBy.required"));
                    }
                    // Lógica inteligente para el Proveedor:
                    if (purchase.getSupplierId() != null && !purchase.getSupplierId().equals(existingPurchase.getSupplierId())) {
                        validateSupplier(purchase.getSupplierId());
                    }
                    // 2. Si no mandan nada, mantenemos el que ya tenía la compra original.
                    else if (purchase.getSupplierId() == null) {
                        purchase.setSupplierId(existingPurchase.getSupplierId());
                    }

                    // Preservamos el autor original de la compra
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
            // Intentamos obtener el proveedor
            SupplierDto supplier = supplierClient.getSupplier(supplierId);

            // Si el cliente devuelve null (aunque Feign suele lanzar 404)
            if (supplier == null) {
                throw new SupplierNotFoundException("supplier.not-found", "id", supplierId.toString());
            }

            // Validación de estado (Si tienes SupplierInactiveException creada)
            if (!supplier.isActive()) {
                throw new SupplierInactiveException("supplier.inactive", "name", supplier.getName());
            }

        } catch (feign.FeignException.NotFound e) {
            // ✅ AQUÍ ES DONDE OCURRE LA MAGIA:
            // Lanzamos tu excepción personalizada que hereda de BaseDomainException
            throw new SupplierNotFoundException("supplier.not-found", "id", supplierId.toString());

        } catch (feign.FeignException e) {
            // Para errores de red o caídas del servidor (500),
            // lanzamos algo que el GlobalExceptionHandler capture como error de infraestructura
            throw new RuntimeException("service.supplier.unavailable");
        }
    }
}