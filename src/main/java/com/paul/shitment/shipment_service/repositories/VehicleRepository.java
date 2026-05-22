package com.paul.shitment.shipment_service.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paul.shitment.shipment_service.models.entities.Vehicle;


public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    
    boolean existsByInternalCode(String internalCode);
    
    List<Vehicle> findByCooperativeId(UUID cooperativeId);
}
