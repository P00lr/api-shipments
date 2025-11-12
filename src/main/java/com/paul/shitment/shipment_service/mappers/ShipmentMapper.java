package com.paul.shitment.shipment_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.models.entities.Shipment;

@Component
public class ShipmentMapper {

    public ShipmentResponseDto entityToDto(Shipment shipment) {
        return new ShipmentResponseDto(
                shipment.getId(),

                shipment.getOriginOffice().getName(),
                shipment.getDestinationOffice().getName(),

                shipment.getSender().getName(),
                shipment.getSender().getCi(),
                shipment.getSender().getPhone(),

                shipment.getRecipient().getName(),
                shipment.getRecipient().getCi(),
                shipment.getRecipient().getPhone(),

                shipment.getCreatedBy().getPerson().getName(),

                shipment.getItemDescription(),
                shipment.getTrackingCode(),

                shipment.getCreatedAt(),
                shipment.getUpdatedAt(),
                shipment.getDeliveredAt(),

                shipment.getShippingCost(),
                shipment.getStatus());
    }

    public List<ShipmentResponseDto> entitiesToDto(List<Shipment> shipments) {
        return shipments.stream()
                .map((shipment) -> entityToDto(shipment))
                .collect(Collectors.toList());
    }

}
