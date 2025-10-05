package com.paul.shitment.shipment_service.dto.shipment;

import java.time.LocalDateTime;
import java.util.UUID;

import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;

public record ShipmentResponseDto(

    UUID id,
    String nameOriginOffice,
    String nameDestinationOffice,
    
    String senderName,
    String senderCi,
    String senderPhone,
    
    String recipientName,
    String recipientCi,
    String recipientPhone,

    String nameUser,

    String itemDescription,
    String trackingCode,

    LocalDateTime createAt,
    LocalDateTime updateAt,
    String deliveredAt,

    Double shippingCost,
    
    ShipmentStatus status

) {}
