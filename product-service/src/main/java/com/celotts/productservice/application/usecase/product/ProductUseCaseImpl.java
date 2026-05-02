package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.domain.model.product.ProductUnitModel;
import com.celotts.productservice.domain.port.input.product.ProductUseCase;
import com.celotts.productservice.domain.port.output.category.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductBrandRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductUnitRepositoryPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductUseCaseImpl implements ProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductUnitRepositoryPort productUnitPort;
    private final ProductBrandRepositoryPort productBrandPort;
    private final CategoryRepositoryPort categoryRepositoryPort;

    // Umbral por defecto para productos con bajo stock
    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 5;

    @Override
    @Transactional
    public ProductModel createProduct(ProductModel cmd) {
        if (productRepositoryPort.existsByCode(cmd.getCode())) {
            throw new ResourceAlreadyExistsException("product.already-exists", cmd.getCode());
        }

        validateReferences(cmd);
        
        // Usar toBuilder para asignar el unitId resuelto de forma segura e inmutable
        ProductModel productToSave = cmd.toBuilder()
                .unitId(resolveUnitId(cmd.getUnitCode()))
                .build();

        return productRepositoryPort.save(productToSave);
    }

    @Override
    @Transactional
    public ProductModel updateProduct(UUID id, ProductModel productUpdates) {
        ProductModel existing = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product.not-found-with-id", id));

        validateReferences(productUpdates);
        UUID newUnitId = resolveUnitId(productUpdates.getUnitCode());

        ProductModel updated = existing.toBuilder()
                .code(productUpdates.getCode() != null ? productUpdates.getCode() : existing.getCode())
                .name(productUpdates.getName() != null ? productUpdates.getName() : existing.getName())
                .description(productUpdates.getDescription() != null ? productUpdates.getDescription() : existing.getDescription())
                .categoryId(productUpdates.getCategoryId() != null ? productUpdates.getCategoryId() : existing.getCategoryId())
                .unitCode(productUpdates.getUnitCode() != null ? productUpdates.getUnitCode() : existing.getUnitCode())
                .unitId(newUnitId != null ? newUnitId : existing.getUnitId())
                .brandId(productUpdates.getBrandId() != null ? productUpdates.getBrandId() : existing.getBrandId())
                .minimumStock(productUpdates.getMinimumStock() != null ? productUpdates.getMinimumStock() : existing.getMinimumStock())
                .currentStock(productUpdates.getCurrentStock() != null ? productUpdates.getCurrentStock() : existing.getCurrentStock())
                .unitPrice(productUpdates.getUnitPrice() != null ? productUpdates.getUnitPrice() : existing.getUnitPrice())
                .enabled(productUpdates.getEnabled() != null ? productUpdates.getEnabled() : existing.getEnabled())
                .updatedBy(productUpdates.getUpdatedBy() != null ? productUpdates.getUpdatedBy() : existing.getUpdatedBy())
                .updatedAt(LocalDateTime.now())
                .build();
        return productRepositoryPort.save(updated);
    }

    @Override
    public ProductModel getProductById(UUID id) {
        return productRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product.not-found-with-id", id));
    }

    @Override
    public ProductModel getProductByCode(String code) {
        return productRepositoryPort.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("product.not-found-code", code));
    }

    @Override
    public void hardDeleteProduct(UUID id) {
        productRepositoryPort.deleteById(id);
    }

    @Override
    public ProductModel enableProduct(UUID id) {
        ProductModel product = getProductById(id);
        if (Boolean.TRUE.equals(product.getEnabled())) return product;
        ProductModel updated = product.toBuilder().enabled(true).build();
        return productRepositoryPort.save(updated);
    }

    @Override
    public ProductModel updateStock(UUID id, int stock) {
        return productRepositoryPort.updateStock(id, stock);
    }

    @Override
    @Transactional
    public void adjustStock(UUID productId, int quantity) {
        ProductModel product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product.not-found-with-id", productId));

        int currentStock = product.getCurrentStock() != null ? product.getCurrentStock() : 0;
        ProductModel updated = product.toBuilder()
                .currentStock(currentStock + quantity)
                .build();

        productRepositoryPort.save(updated);
    }

    @Override
    public Page<ProductModel> getAllProducts(Pageable pageable) {
        return productRepositoryPort.findAll(pageable);
    }

    @Override
    public Page<ProductModel> getAllProductsWithFilters(Pageable pageable, String code, String name, String description) {
        return productRepositoryPort.findAllWithFilters(pageable, code, name, description);
    }

    @Override
    public Page<ProductModel> getActiveProducts(Pageable pageable) {
        return productRepositoryPort.findActive(pageable);
    }

    @Override
    public List<ProductModel> getInactiveProducts() {
        return productRepositoryPort.findInactive(Pageable.unpaged()).getContent();
    }

    @Override
    public List<ProductModel> getProductsByCategory(UUID categoryId) {
        return productRepositoryPort.findByCategory(categoryId, Pageable.unpaged()).getContent();
    }

    @Override
    public List<ProductModel> getLowStockByCategory(UUID categoryId) {
        // Usar el método del repositorio con paginación y umbral
        return productRepositoryPort.findLowStockByCategory(categoryId, Pageable.unpaged(), DEFAULT_LOW_STOCK_THRESHOLD).getContent();
    }

    @Override
    public List<ProductModel> getLowStockProducts() {
        // Usar el método del repositorio con paginación y umbral
        return productRepositoryPort.findLowStock(Pageable.unpaged(), DEFAULT_LOW_STOCK_THRESHOLD).getContent();
    }

    @Override
    public List<ProductModel> getProductsByBrand(UUID brandId) {
        return productRepositoryPort.findByBrand(brandId, Pageable.unpaged()).getContent();
    }

    @Override
    public long countProducts() { return productRepositoryPort.countAll(); }

    @Override
    public long countActiveProducts() { return productRepositoryPort.countActive(); }

    @Override
    public Optional<String> validateUnitCode(String code) {
        if (!productUnitPort.existsByCode(code)) return Optional.empty();
        return productUnitPort.findNameByCode(code);
    }

    private void validateReferences(ProductModel dto) {
        if (dto.getBrandId() != null && !productBrandPort.existsById(dto.getBrandId())) {
            throw new ResourceNotFoundException("brand.not-found", dto.getBrandId());
        }
        if (dto.getCategoryId() != null && !categoryRepositoryPort.existsById(dto.getCategoryId())) {
            throw new ResourceNotFoundException("category.not.found", dto.getCategoryId());
        }
    }

    private UUID resolveUnitId(String unitCode) {
        if (unitCode == null) return null;
        return productUnitPort.findByCode(unitCode)
                .map(ProductUnitModel::getId)
                .orElseThrow(() -> new ResourceNotFoundException("product.unit.not-found", unitCode));
    }

    @Override
    public boolean existsById(UUID id) { return productRepositoryPort.existsById(id); }

    @Override
    public boolean existsByCode(String code) { return productRepositoryPort.existsByCode(code); }



    @Override
    public List<ProductModel> getAll() {
        return productRepositoryPort.findAll(Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional
    public void disableProduct(UUID id) {
        ProductModel product = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product.not-found-with-id", id));

        ProductModel updated = product.toBuilder()
                .enabled(false)
                .updatedAt(LocalDateTime.now())
                .build();

        productRepositoryPort.save(updated);
    }
}
