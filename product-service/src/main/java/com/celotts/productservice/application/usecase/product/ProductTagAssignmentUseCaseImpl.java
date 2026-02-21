package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.domain.port.input.product.ProductTagAssignmentUseCase;
import com.celotts.productservice.domain.port.output.product.ProductTagAssignmentRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductTagAssignmentUseCaseImpl implements ProductTagAssignmentUseCase {

    private final ProductTagAssignmentRepositoryPort repository;

    @Override
    public ProductTagAssignmentModel create(ProductTagAssignmentModel model) {
        return repository.save(model);
    }

    @Override
    public ProductTagAssignmentModel update(UUID id, ProductTagAssignmentModel model) {
        // Ensure the entity exists before updating
        findById(id);
        model.setId(id); // Set the ID from the path variable to the model
        return repository.save(model);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public ProductTagAssignmentModel findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("tag.assignment.not-found", id));
    }

    @Override
    public Page<ProductTagAssignmentModel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<ProductTagAssignmentModel> findByEnabled(boolean enabled) {
        return repository.findByEnabled(enabled, Pageable.unpaged()).getContent();
    }

    @Override
    public long countEnabled() {
        return repository.countByEnabled(true);
    }

    @Override
    public ProductTagAssignmentModel enable(UUID id) {
        return repository.enable(id);
    }

    @Override
    public ProductTagAssignmentModel disable(UUID id) {
        return repository.disable(id);
    }
}