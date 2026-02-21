package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.exception.DomainException;
import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.model.product.ProductTypeModel;
import com.celotts.productservice.domain.port.input.product.ProductTypeUseCase;
import com.celotts.productservice.domain.port.output.product.ProductTypeRepositoryPort;
import com.celotts.productservice.infrastructure.common.error.ErrorCode;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final MessageSource messageSource;

    private String getLocalizedMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    public ProductTypeModel create(ProductTypeModel model) {
        if (model.getCode() == null || model.getCode().isBlank()) {
            throw new DomainException(ErrorCode.BAD_REQUEST, 400, "product-type.code.required");
        }
        if (repo.existsByCode(model.getCode())) {
            throw new ResourceAlreadyExistsException("product-type.code.exists", model.getCode());
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
        // 1. Traducción para "No encontrado"
        ProductTypeModel current = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("product-type.not-found", id));

        // Si cambian el code, valida duplicados
        if (changes.getCode() != null && !changes.getCode().equalsIgnoreCase(current.getCode())) {
            if (repo.existsByCode(changes.getCode())) {
                // 2. Traducción para "Código ya existe"
                throw new ResourceAlreadyExistsException("product-type.code.exists", changes.getCode());
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
        if (!repo.findById(id).isPresent()) {
            throw new ResourceNotFoundException("product-type.not-found", id);
        }
        repo.deleteById(id);
    }

    @PostConstruct
    public void log() {
        // Obtenemos el mensaje traducido
        String message = getLocalizedMessage("product-type.service.loaded");
        System.out.println(message);
    }

    // Helpers
    @Override public boolean existsByCode(String code) { return repo.existsByCode(code); }
    @Override public Optional<String> findNameByCode(String code) { return repo.findNameByCode(code); }
    @Override public List<String> findAllCodes() { return repo.findAllCodes(); }

}