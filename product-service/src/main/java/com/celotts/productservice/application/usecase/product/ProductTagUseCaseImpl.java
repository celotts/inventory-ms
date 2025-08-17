package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.model.product.ProductTagModel;
import com.celotts.productservice.domain.port.input.product.ProductTagUseCase;
import com.celotts.productservice.domain.port.output.product.ProductTagRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductTagUseCaseImpl implements ProductTagUseCase {

    private final ProductTagRepositoryPort repo;

    @Override
    public ProductTagModel create(ProductTagModel model) {
        if (repo.existsByName(model.getName())) {
            throw new ResourceAlreadyExistsException("Product", model.getName());
        }
        return repo.save(model);
    }

    @Override
    public ProductTagModel update(UUID id, ProductTagModel model) {
        ProductTagModel current = findById(id);
        ProductTagModel merged = current.toBuilder()
                .name(model.getName())
                .description(model.getDescription())
                .enabled(model.getEnabled())
                .updatedBy(model.getUpdatedBy())
                .build();
        return repo.save(merged);
    }

    @Override public void delete(UUID id) { repo.deleteById(id); }

    @Override public ProductTagModel enable(UUID id) {
        return repo.save(findById(id).withEnabled(true));
    }
    @Override public ProductTagModel disable(UUID id) {
        return repo.save(findById(id).withEnabled(false));
    }

    @Override
    public ProductTagModel findById(UUID id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Override public Optional<ProductTagModel> findByName(String name){ return repo.findByName(name); }
    @Override public boolean existsByName(String name){ return repo.existsByName(name); }

    @Override public Page<ProductTagModel> findAll(Pageable pageable){ return repo.findAll(pageable); }
    @Override public List<ProductTagModel> findAllEnabled(){ return repo.findByEnabled(true); }
    @Override public long countEnabled(){ return repo.countByEnabled(true); }
}