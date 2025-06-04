package com.celotts.productservice.infrastructure.adapter.output.postgres.repository;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {}