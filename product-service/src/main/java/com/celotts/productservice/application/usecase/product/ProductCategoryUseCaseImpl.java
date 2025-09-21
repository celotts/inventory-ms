package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;
import com.celotts.productservice.domain.port.input.product.ProductCategoryUseCase;
import com.celotts.productservice.domain.port.output.product.ProductCategoryRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.productcategory.ProductCategoryCreateDto; // <-- IMPORTA EL DTO
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductCategoryUseCaseImpl implements ProductCategoryUseCase {

    private final ProductCategoryRepositoryPort repo;

    // === Nuevo: satisface la firma de la interfaz que recibe DTO ===
    public ProductCategoryModel assignCategoryToProduct(ProductCategoryCreateDto dto) {
        // map DTO -> Model (sin depender del mapper REST)
        var modelIn = ProductCategoryModel.builder()
                .productId(dto.getProductId())
                .categoryId(dto.getCategoryId())
                .enabled(dto.getEnabled()) // puede ser null; abajo ponemos default
                .build();

        return assignCategoryToProduct(modelIn); // delega al método de dominio
    }

    // === Tu método de dominio (lo mantenemos) ===
    public ProductCategoryModel assignCategoryToProduct(ProductCategoryModel dtoModel) {
        // Evitar duplicado activo (ajusta si tu repo no tiene este método)
        if (repo.existsByProductIdAndCategoryIdAndEnabledTrue(dtoModel.getProductId(), dtoModel.getCategoryId())) {
            throw new IllegalArgumentException("La asignación productId-categoryId ya existe y está activa");
        }

        var now = LocalDateTime.now();
        var model = dtoModel.toBuilder()
                .id(dtoModel.getId() != null ? dtoModel.getId() : UUID.randomUUID())
                .enabled(dtoModel.getEnabled() != null ? dtoModel.getEnabled() : Boolean.TRUE)
                .assignedAt(dtoModel.getAssignedAt() != null ? dtoModel.getAssignedAt() : now)
                // Auditoría y createdAt/updatedAt -> JPA Auditing o @PrePersist en la entidad
                .build();

        return repo.save(model);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCategoryModel getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductCategory not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryModel> getAll() {
        return List.of();
    }

    @Override
    public void disableById(UUID id) {
        var current = getById(id);
        var disabled = current.toBuilder()
                .enabled(false)
                .build();
        repo.save(disabled);
    }

    @Override
    public void deleteById(UUID id) {
        repo.deleteById(id);
    }
}