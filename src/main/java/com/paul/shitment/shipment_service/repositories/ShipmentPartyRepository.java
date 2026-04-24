package com.paul.shitment.shipment_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paul.shitment.shipment_service.models.entities.ShipmentParty;

public interface ShipmentPartyRepository extends JpaRepository<ShipmentParty, UUID> {

}
