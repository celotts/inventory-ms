package com.celotts.purchaseservice.application.usecase;

import com.celotts.purchaseservice.domain.exception.*;
import com.celotts.purchaseservice.domain.model.purchase.PurchaseItemModel;
import com.celotts.purchaseservice.domain.model.purchase.PurchaseModel;
import com.celotts.purchaseservice.domain.port.input.PurchaseUseCase;
import com.celotts.purchaseservice.domain.port.output.PurchaseRepositoryPort;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.product.ProductDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.supplier.SupplierDto;
import com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto.tax.TaxDto;
import com.celotts.purchaseservice.infrastructure.client.ProductClient;
import com.celotts.purchaseservice.infrastructure.client.SupplierClient;
import com.celotts.purchaseservice.infrastructure.client.TaxClient;
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
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseUseCaseImpl implements PurchaseUseCase {

    private final PurchaseRepositoryPort repositoryPort;
    private final MessageSource messageSource;
    private final SupplierClient supplierClient;
    private final ProductClient productClient;
    private final TaxClient taxClient;

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
            // 1. Validate Product
            // El fallback retorna null si falla o si no existe (dependiendo de la implementación del fallback)
            // Asumimos que el fallback loguea el error y retorna null en caso de fallo de conexión.
            ProductDto product = productClient.getProductById(item.getProductId());

            if (product == null) {
                // Decisión de Resiliencia: ¿Fallamos toda la compra o la permitimos con datos parciales?
                // Para consistencia de datos, si no podemos validar el producto, debemos rechazar la compra
                // pero con un mensaje claro de que el servicio no está disponible o el producto no existe.
                log.error("Product validation failed for ID: {}. Service might be down or product not found.", item.getProductId());
                throw new ServiceUnavailableException(getMsg("service.product.unavailable"));
            }

            item.setProductName(product.getName());
            item.setProductCode(product.getCode());
            item.setUnitSymbol(product.getUnitSymbol());

            // 2. Validate Tax (if present)
            if (item.getTaxId() != null) {
                TaxDto tax = taxClient.getTaxById(item.getTaxId());
                
                if (tax == null) {
                    // Resiliencia: Si el servicio de impuestos falla, ¿detenemos la venta?
                    // Opción A: Detener (Estricto) -> throw new ServiceUnavailableException(...)
                    // Opción B: Continuar sin impuestos (Degradación grácil) -> loguear y seguir.
                    
                    // Dado que es un sistema financiero, optamos por lo seguro: Fallar si no podemos calcular impuestos.
                    log.error("Tax validation failed for ID: {}. Service might be down.", item.getTaxId());
                    throw new ServiceUnavailableException(getMsg("service.tax.unavailable"));
                }

                if (Boolean.FALSE.equals(tax.getIsActive())) {
                    throw new IllegalArgumentException(getMsg("tax.inactive", tax.getName()));
                }
                item.setTaxRate(tax.getRate());
            } else {
                item.setTaxRate(BigDecimal.ZERO);
            }
        }
    }

    private void calculateTotals(PurchaseModel purchase) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;

        for (PurchaseItemModel item : purchase.getItems()) {
            // Line Total = Quantity * Unit Cost
            BigDecimal lineTotal = item.getUnitCost().multiply(item.getQuantity());
            subtotal = subtotal.add(lineTotal);

            // Tax Amount = Line Total * (Tax Rate / 100)
            if (item.getTaxRate() != null && item.getTaxRate().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal taxAmount = lineTotal.multiply(item.getTaxRate())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                item.setTaxAmount(taxAmount);
                taxTotal = taxTotal.add(taxAmount);
            } else {
                item.setTaxAmount(BigDecimal.ZERO);
            }
        }

        purchase.setSubtotal(subtotal);
        purchase.setTaxTotal(taxTotal);
        purchase.setDiscountTotal(BigDecimal.ZERO); // TODO: Implement discount logic
        purchase.setGrandTotal(subtotal.add(taxTotal).subtract(purchase.getDiscountTotal()));
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
            existingPurchase.setTaxTotal(purchaseUpdates.getTaxTotal());
            existingPurchase.setGrandTotal(purchaseUpdates.getGrandTotal());
        }
        
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
        // Ahora el cliente usa FallbackFactory, por lo que lanzará la excepción correcta
        // (SupplierNotFoundException o ServiceUnavailableException) si algo falla.
        SupplierDto supplier = supplierClient.getSupplier(supplierId);

        // Ya no necesitamos verificar null, porque el fallback factory lanza excepción.
        // Pero mantenemos la validación de negocio.
        if (!supplier.isActive()) {
            throw new SupplierInactiveException("supplier.inactive", "name", supplier.getName());
        }
    }
}
