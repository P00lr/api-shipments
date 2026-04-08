package com.paul.shitment.shipment_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paul.shitment.shipment_service.models.entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission, UUID>{
    boolean findByName(String name);
}
