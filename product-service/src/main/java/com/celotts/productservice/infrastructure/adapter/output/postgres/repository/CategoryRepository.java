package com.celotts.productservice.infrastructure.adapter.output.postgres.repository;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    Optional<CategoryEntity> findByName(String name);

    @Query("SELECT c FROM CategoryEntity c WHERE c.name LIKE %:name%")
    List<CategoryEntity> findByNameContaining(@Param("name") String name);

    boolean existsByName(String name);

    @Query("SELECT c FROM CategoryEntity c ORDER BY c.createdAt DESC")
    List<CategoryEntity> findAllOrderByCreatedAtDesc();
}