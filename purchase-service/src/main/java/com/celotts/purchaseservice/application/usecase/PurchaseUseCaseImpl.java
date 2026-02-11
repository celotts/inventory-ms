package com.celotts.purchaseservice.application.usecase;

import com.celotts.purchaseservice.domain.exception.*;
import com.celotts.purchaseservice.domain.model.purchase.PurchaseItemModel;
import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.input.PurchaseUseCase;
import com.celotts.purchaseservice.domain.port.output.PurchaseRepositoryPort;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.product.ProductDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.supplier.SupplierDto;
import com.celotts.purchaseservice.infrastructure.client.ProductClient;
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

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseUseCaseImpl implements PurchaseUseCase {

    private final PurchaseRepositoryPort repositoryPort;
    private final MessageSource messageSource;
    private final SupplierClient supplierClient;
    private final ProductClient productClient;

    private String getMsg(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    public PurchaseModel create(PurchaseModel purchase) {
        log.info("Creating purchase with order number: {}", purchase.getOrderNumber());
        purchase.normalize();

        validateSupplier(purchase.getSupplierId());
        validateAndEnrichItems(purchase);
        calculateTotals(purchase);

        if (purchase.getCreatedBy() == null || purchase.getCreatedBy().isBlank()) {
            purchase.setCreatedBy(getMsg("app.user.default"));
        }

        if (repositoryPort.existsByOrderNumber(purchase.getOrderNumber())) {
            throw new PurchaseAlreadyExistsException("purchase.already-exists", "orderNumber", purchase.getOrderNumber());
        }

        return repositoryPort.save(purchase);
    }

    private void validateAndEnrichItems(PurchaseModel purchase) {
        if (purchase.getItems() == null || purchase.getItems().isEmpty()) {
            throw new InvalidPurchaseStateException("purchase.items.required");
        }

        for (PurchaseItemModel item : purchase.getItems()) {
            try {
                ProductDto product = productClient.getProductById(item.getProductId());
                item.setProductName(product.getName());
                item.setProductCode(product.getCode());
                item.setUnitSymbol(product.getUnitSymbol());
            } catch (FeignException.NotFound e) {
                throw new ProductNotFoundException("product.not-found", item.getProductId());
            } catch (FeignException e) {
                log.error("Error communicating with Product Service: Status={}, Msg={}", e.status(), e.getMessage());
                throw new ServiceUnavailableException(getMsg("service.product.unavailable"));
            }
        }
    }

    private void calculateTotals(PurchaseModel purchase) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (PurchaseItemModel item : purchase.getItems()) {
            BigDecimal lineTotal = item.getUnitCost().multiply(item.getQuantity());
            subtotal = subtotal.add(lineTotal);
        }
        purchase.setSubtotal(subtotal);
        purchase.setTaxTotal(BigDecimal.ZERO);
        purchase.setDiscountTotal(BigDecimal.ZERO);
        purchase.setGrandTotal(subtotal);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseModel findById(UUID id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(getMsg("purchase.not-found-with-id", id), id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseModel> findAll(Pageable pageable) {
        return repositoryPort.findAll(pageable);
    }

    @Override
    @Transactional
    public PurchaseModel update(UUID id, PurchaseModel purchaseUpdates) {
        PurchaseModel existingPurchase = repositoryPort.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(getMsg("purchase.cannot-update-not-found", id), id));

        if (purchaseUpdates.getUpdatedBy() == null || purchaseUpdates.getUpdatedBy().isBlank()) {
            throw new IllegalArgumentException(getMsg("purchase.update.updatedBy.required"));
        }

        if (purchaseUpdates.getSupplierId() != null && !purchaseUpdates.getSupplierId().equals(existingPurchase.getSupplierId())) {
            validateSupplier(purchaseUpdates.getSupplierId());
            existingPurchase.setSupplierId(purchaseUpdates.getSupplierId());
        }

        if (purchaseUpdates.getItems() != null && !purchaseUpdates.getItems().isEmpty()) {
            validateAndEnrichItems(purchaseUpdates);
            calculateTotals(purchaseUpdates);
            existingPurchase.setItems(purchaseUpdates.getItems());
            existingPurchase.setSubtotal(purchaseUpdates.getSubtotal());
            existingPurchase.setGrandTotal(purchaseUpdates.getGrandTotal());
        }
        
        // Actualizar otros campos si vienen en el DTO
        if (purchaseUpdates.getOrderNumber() != null) existingPurchase.setOrderNumber(purchaseUpdates.getOrderNumber());
        if (purchaseUpdates.getStatus() != null) existingPurchase.setStatus(purchaseUpdates.getStatus());
        if (purchaseUpdates.getNotes() != null) existingPurchase.setNotes(purchaseUpdates.getNotes());
        
        existingPurchase.setUpdatedBy(purchaseUpdates.getUpdatedBy());
        existingPurchase.normalize();

        return repositoryPort.save(existingPurchase);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!repositoryPort.existsById(id)) {
            throw new PurchaseNotFoundException(getMsg("purchase.cannot-delete-not-found", id), id);
        }
        repositoryPort.deleteById(id);
    }

    private void validateSupplier(UUID supplierId) {
        try {
            SupplierDto supplier = supplierClient.getSupplier(supplierId);
            if (!supplier.isActive()) {
                throw new SupplierInactiveException("supplier.inactive", "name", supplier.getName());
            }
        } catch (FeignException.NotFound e) {
            throw new SupplierNotFoundException("supplier.not-found", "id", supplierId.toString());
        } catch (FeignException e) {
            log.error("Error communicating with Supplier Service: Status={}, Msg={}", e.status(), e.getMessage());
            throw new ServiceUnavailableException(getMsg("service.supplier.unavailable"));
        }
    }
}
