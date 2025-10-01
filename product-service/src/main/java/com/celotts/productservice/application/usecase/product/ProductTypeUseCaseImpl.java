package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.model.product.ProductTypeModel;
import com.celotts.productservice.domain.port.input.product.ProductTypeUseCase;
import com.celotts.productservice.domain.port.output.product.ProductTypeRepositoryPort;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductTypeUseCaseImpl implements ProductTypeUseCase {

    private final ProductTypeRepositoryPort repo;

    @Override
    @Transactional
    public ProductTypeModel create(ProductTypeModel model) {
        if (model.getCode() == null || model.getCode().isBlank()) {
            throw new IllegalArgumentException("ProductType.code is required");
        }
        if (repo.existsByCode(model.getCode())) {
            throw new IllegalStateException("ProductType.code already exists");
        }
        return repo.save(model);
    }

    @Override
    public Optional<ProductTypeModel> getById(UUID id) {
        return repo.findById(id);
    }

    @Override
    public Optional<ProductTypeModel> getByCode(String code) {
        return repo.findByCode(code);
    }

    @Override
    public Page<ProductTypeModel> getAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    @Transactional
    public ProductTypeModel update(UUID id, ProductTypeModel changes) {
        ProductTypeModel current = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductType not found: " + id));

        // Si cambian el code, valida duplicados
        if (changes.getCode() != null && !changes.getCode().equalsIgnoreCase(current.getCode())) {
            if (repo.existsByCode(changes.getCode())) {
                throw new IllegalStateException("ProductType.code already exists");
            }
            current = current.withCode(changes.getCode());
        }
        if (changes.getName() != null) current = current.withName(changes.getName());
        if (changes.getDescription() != null) current = current.withDescription(changes.getDescription());
        if (changes.getEnabled() != null) current = current.withEnabled(changes.getEnabled());

        return repo.save(current);
    }

    @Override
    @Transactional
    public ProductTypeModel patch(UUID id, ProductTypeModel partial) {
        // lo puedes implementar igual que update pero ignorando nulls
        return update(id, partial);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        repo.deleteById(id);
    }

    @PostConstruct
    public void log() {
        System.out.println("ProductTypeUseCaseImpl was loaded.");
    }

    // Helpers
    @Override public boolean existsByCode(String code) { return repo.existsByCode(code); }
    @Override public Optional<String> findNameByCode(String code) { return repo.findNameByCode(code); }
    @Override public List<String> findAllCodes() { return repo.findAllCodes(); }

}