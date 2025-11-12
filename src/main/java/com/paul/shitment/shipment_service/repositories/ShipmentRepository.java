package com.paul.shitment.shipment_service.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO;
import com.paul.shitment.shipment_service.models.entities.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

    boolean existsByTrackingCode(String code);

    @Query("""
                SELECT s
                FROM Shipment s
                ORDER BY
                    CASE
                        WHEN s.status = 'REGISTERED' THEN 1
                        ELSE 2
                    END,
                    s.createdAt DESC
            """)
    Page<Shipment> findAllOrdered(Pageable pageable);

    @Query("""
                SELECT s FROM Shipment s
                ORDER BY
                    CASE
                        WHEN s.status = 'REGISTERED' THEN 1
                        ELSE 2
                    END,
                    s.createdAt DESC
            """)
    List<Shipment> findAllOrdered();

    @Query("""
                SELECT new com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO(
                    s.id, s.trackingCode, s.itemDescription, r.name, r.ci, r.phone, s.status, s.shippingCost)
                FROM Shipment s
                LEFT JOIN s.recipient r
                WHERE (:term IS NULL OR LOWER(s.trackingCode) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(r.ci) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(r.phone) LIKE LOWER(CONCAT('%', :term, '%')))
                    AND s.status = com.paul.shitment.shipment_service.models.enums.ShipmentStatus.REGISTERED
                ORDER BY s.createdAt DESC
            """)
    List<ShipmentSuggestionDTO> searchByTerm(@Param("term") String term, Pageable pageable);

}
