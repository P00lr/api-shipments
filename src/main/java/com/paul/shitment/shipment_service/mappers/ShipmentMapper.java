package com.paul.shitment.shipment_service.mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.shipment.ShipmentPersonDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.models.entities.Shipment;
import com.paul.shitment.shipment_service.models.entities.ShipmentParty;
import com.paul.shitment.shipment_service.models.enums.ShipmentPartyRole;

@Component
public class ShipmentMapper {

    public ShipmentResponseDto toShipmentResponseDto(Shipment shipment) {

        return new ShipmentResponseDto(
                shipment.getId(),

                shipment.getOriginOffice().getName(),
                shipment.getDestinationOffice().getName(),

                shipment.getCreatedBy().getPerson().getFullName(),

                mapParty(shipment.getParties(), ShipmentPartyRole.SENDER),
                mapParty(shipment.getParties(), ShipmentPartyRole.RECIPIENT),
                mapParty(shipment.getParties(), ShipmentPartyRole.RECEIVED_BY),

                shipment.getItemDescription(),
                shipment.getTrackingCode(),

                shipment.getCreatedAt(),
                shipment.getUpdatedAt(),
                shipment.getDeliveredAt(),

                shipment.getShippingCost(),
                shipment.getStatus());
    }

    public List<ShipmentResponseDto> toShipmentDtos(List<Shipment> shipments) {
        return shipments.stream()
                .map((shipment) -> toShipmentResponseDto(shipment))
                .collect(Collectors.toList());
    }

    private ShipmentPersonDto mapParty(Set<ShipmentParty> parties, ShipmentPartyRole role) {
        return parties.stream()
                .filter(p -> p.getRole() == role)
                .findFirst()
                .map(p -> new ShipmentPersonDto(
                        p.getDocumentType().name(),
                        p.getDocumentNumber(),
                        p.getFullName(),
                        p.getPhone()))
                .orElse(null);
    }
    
}
