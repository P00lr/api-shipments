package com.paul.shitment.shipment_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paul.shitment.shipment_service.models.entities.Role;

public interface RoleRepository extends JpaRepository<Role, UUID>{
    boolean existsByName(String name);
    Optional<Role> findByName(String name);
}
