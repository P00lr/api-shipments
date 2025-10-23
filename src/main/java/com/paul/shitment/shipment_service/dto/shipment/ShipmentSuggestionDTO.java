package com.paul.shitment.shipment_service.dto.shipment;

import java.util.UUID;

import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;

public record ShipmentSuggestionDTO(
    UUID id,
    String trackingCode,
    String itemDescription,
    String recipientName,
    String recipientCi,
    String recipientPhone,
    ShipmentStatus status,
    Double shippingCost
) {

}
