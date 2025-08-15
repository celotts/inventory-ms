package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.model.ProductUnitModel;
import com.celotts.productservice.domain.port.product.unit.usecase.ProductUnitUseCase;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service                        // 👉 ahora Spring lo descubre como bean
@Transactional
@RequiredArgsConstructor
public class ProductUnitUseCaseImpl implements ProductUnitUseCase {

    private final ProductUnitRepositoryPort repo;   // puerto de SALIDA

    @Override
    public ProductUnitModel create(ProductUnitModel model) {
        // aquí puedes agregar validaciones de dominio si aplican
        return repo.save(model);
    }

    @Override
    public ProductUnitModel update(UUID id, ProductUnitModel model) {
        // recuperar el existente (si no existe, lanzamos IllegalArgumentException)
        ProductUnitModel existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductUnit not found: " + id));

        // aplicar cambios no nulos del request
        if (model.getName() != null)          existing.setName(model.getName());
        if (model.getDescription() != null)   existing.setDescription(model.getDescription());
        if (model.getCode() != null)          existing.setCode(model.getCode());
        if (model.getUpdatedBy() != null)     existing.setUpdatedBy(model.getUpdatedBy());
        existing.setUpdatedAt(model.getUpdatedAt());

        return repo.save(existing);
    }

    @Override
    public void delete(UUID id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("ProductUnit not found: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    public ProductUnitModel save(ProductUnitModel model) { return repo.save(model); }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductUnitModel> findById(UUID id) { return repo.findById(id); }

    @Override
    @Transactional(readOnly = true)
    public List<ProductUnitModel> findAll() {     /* tu lógica */
        // p.e. mapear repo.findAllEntities() a Model
        return repo.findAll();
    }

    @Override public void deleteById(UUID id){ repo.deleteById(id); }
    @Override public boolean existsById(UUID id){ return repo.findById(id).isPresent(); }
    @Override public boolean existsByCode(String c){ return repo.existsByCode(c); }
    @Override public Optional<String> findNameByCode(String c){ return repo.findNameByCode(c);}
    @Override public List<String> findAllCodes(){ return repo.findAllCodes(); }
}