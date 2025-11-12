package com.paul.shitment.shipment_service.dto.shipment;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ShipmentDeliveryRequest", description = "DTO para marcar un envío como entregado, opcionalmente con CI del destinatario")
public record ShipmentDeliveryRequest(

        @Schema(description = "CI del destinatario del envío", example = "12345678", required = false)
        String ci
) { }
