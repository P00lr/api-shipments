package com.paul.shitment.shipment_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paul.shitment.shipment_service.models.entities.TransportCooperative;

public interface TransportCooperativeRepository extends JpaRepository<TransportCooperative, UUID> {
    public boolean existsByNameIgnoreCase(String name);
}
