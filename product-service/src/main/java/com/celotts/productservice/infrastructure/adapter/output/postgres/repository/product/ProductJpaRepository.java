package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    boolean existsByCode(String code);
    boolean existsByName(String name);
    long countByEnabled(boolean enabled);

    Optional<ProductEntity> findByCode(String code);

    Page<ProductEntity> findByEnabled(boolean enabled, Pageable pageable);

    Page<ProductEntity> findByCategoryId(UUID categoryId, Pageable pageable);
    Page<ProductEntity> findByBrandId(UUID brandId, Pageable pageable);

    Page<ProductEntity> findByCurrentStockLessThanEqual(int threshold, Pageable pageable);

    Page<ProductEntity> findByCategoryIdAndCurrentStockLessThanEqual(
            UUID categoryId, int threshold, Pageable pageable);

    @Query("""
        select p from ProductEntity p
        where (:code is null or p.code like concat('%', :code, '%'))
          and (:name is null or lower(p.name) like concat('%', lower(:name), '%'))
          and (:description is null or lower(p.description) like concat('%', lower(:description), '%'))
    """)
    Page<ProductEntity> findAllWithFilters(Pageable pageable,
                                           @Param("code") String code,
                                           @Param("name") String name,
                                           @Param("description") String description);
}