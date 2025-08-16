package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTagEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTagJpaRepository extends JpaRepository<ProductTagEntity, UUID> {
    Optional<ProductTagEntity> findByName(String name);
    boolean existsByName(String name);
    List<ProductTagEntity> findByEnabled(boolean enabled);
    Page<ProductTagEntity> findAll(Pageable pageable); // (opcional: ya lo hereda, puedes quitarlo)
    long countByEnabled(boolean enabled);
}