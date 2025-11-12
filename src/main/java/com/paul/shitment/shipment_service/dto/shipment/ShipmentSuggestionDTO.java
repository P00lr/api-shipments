package com.paul.shitment.shipment_service.dto.shipment;

import java.util.UUID;

import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ShipmentSuggestionDTO", description = "DTO que representa información resumida de un envío para sugerencias/autocompletado")
public record ShipmentSuggestionDTO(

        @Schema(description = "Identificador único del envío", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        UUID id,

        @Schema(description = "Código de seguimiento del envío", example = "AB3-C4D", required = true)
        String trackingCode,

        @Schema(description = "Descripción del ítem enviado", example = "Documentos importantes", required = true)
        String itemDescription,

        @Schema(description = "Nombre completo del destinatario", example = "María López", required = true)
        String recipientName,

        @Schema(description = "CI/DNI del destinatario", example = "87654321", required = true)
        String recipientCi,

        @Schema(description = "Número de teléfono del destinatario", example = "77787654321", required = false)
        String recipientPhone,

        @Schema(description = "Estado actual del envío", example = "PENDING", required = true)
        ShipmentStatus status,

        @Schema(description = "Costo del envío", example = "50.0", required = true)
        Double shippingCost
) { }
