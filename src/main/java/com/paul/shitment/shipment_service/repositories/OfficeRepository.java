package com.paul.shitment.shipment_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paul.shitment.shipment_service.models.entities.Office;

public interface OfficeRepository extends JpaRepository<Office, UUID>{
    boolean existsByName(String name);
    boolean existsByAddress(String address);
    boolean existsByPhone(String phone);
}
