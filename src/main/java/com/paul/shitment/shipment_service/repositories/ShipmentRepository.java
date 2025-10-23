package com.paul.shitment.shipment_service.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO;
import com.paul.shitment.shipment_service.models.entities.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

    @Query("""
                SELECT new com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO(
                    s.id, s.trackingCode, s.itemDescription, r.name, r.ci, r.phone, s.status, s.shippingCost)
                FROM Shipment s
                LEFT JOIN s.recipient r
                WHERE LOWER(s.trackingCode) LIKE LOWER(CONCAT('%', :term, '%'))
                AND s.status = com.paul.shitment.shipment_service.models.enums.ShipmentStatus.REGISTERED
                ORDER BY s.createdAt DESC
            """)
    List<ShipmentSuggestionDTO> searchByTrackingCode(@Param("term") String term);

    @Query("""
                SELECT new com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO(
                    s.id, s.trackingCode, s.itemDescription, r.name, r.ci, r.phone, s.status, s.shippingCost)
                FROM Shipment s
                LEFT JOIN s.recipient r
                WHERE LOWER(r.ci) LIKE LOWER(CONCAT('%', :term, '%'))
                AND s.status = com.paul.shitment.shipment_service.models.enums.ShipmentStatus.REGISTERED
                ORDER BY s.createdAt DESC
            """)
    List<ShipmentSuggestionDTO> searchByCi(@Param("term") String term);

    @Query("""
                SELECT new com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO(
                    s.id, s.trackingCode, s.itemDescription, r.name, r.ci, r.phone, s.status, s.shippingCost)
                FROM Shipment s
                LEFT JOIN s.recipient r
                WHERE LOWER(r.phone) LIKE LOWER(CONCAT('%', :term, '%'))
                AND s.status = com.paul.shitment.shipment_service.models.enums.ShipmentStatus.REGISTERED
                ORDER BY s.createdAt DESC
            """)
    List<ShipmentSuggestionDTO> searchByPhone(@Param("term") String term);

    @Query("""
                SELECT new com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO(
                    s.id, s.trackingCode, s.itemDescription, r.name, r.ci, r.phone, s.status, s.shippingCost)
                FROM Shipment s
                LEFT JOIN s.recipient r
                WHERE (
                    LOWER(s.trackingCode) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(r.ci) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(r.phone) LIKE LOWER(CONCAT('%', :term, '%'))
                )
                AND s.status = com.paul.shitment.shipment_service.models.enums.ShipmentStatus.REGISTERED
                ORDER BY s.createdAt DESC
            """)
    List<ShipmentSuggestionDTO> searchShipmentsByTerm(@Param("term") String term);

}
