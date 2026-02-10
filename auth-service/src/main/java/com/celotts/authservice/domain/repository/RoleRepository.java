package com.celotts.authservice.domain.repository;

import com.celotts.authservice.domain.model.ERole;
import com.celotts.authservice.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
