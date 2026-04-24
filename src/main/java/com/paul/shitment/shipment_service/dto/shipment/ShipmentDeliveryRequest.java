package com.paul.shitment.shipment_service.dto.shipment;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "ShipmentDeliveryRequest", description = "DTO para marcar un envío como entregado, opcionalmente con CI del destinatario")
public record ShipmentDeliveryRequest(

        @Schema(
                description = "Numero de documento del destinatario del envío", 
                example = "12345678", 
                requiredMode = RequiredMode.REQUIRED)
        String documentNumber
) { }
