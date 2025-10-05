package com.paul.shitment.shipment_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paul.shitment.shipment_service.models.entities.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {
}
