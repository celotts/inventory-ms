package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUnitJpaRepository extends JpaRepository<ProductUnitEntity, UUID> {

    boolean existsByCode(String code);
    boolean existsByCodeIgnoreCase(String code);
    Optional<ProductUnitEntity> findById(UUID id);
    Optional<ProductUnitEntity> findByCode(String code);

    @Query("select pu.name from ProductUnitEntity pu where pu.code = :code")
    Optional<String> findNameByCode(@Param("code") String code);

    @Query("select pu.description from ProductUnitEntity pu where pu.code = :code")
    Optional<String> findDescriptionByCode(@Param("code") String code);

    @Query("select pu.code from ProductUnitEntity pu")
    List<String> findAllCodes();

}