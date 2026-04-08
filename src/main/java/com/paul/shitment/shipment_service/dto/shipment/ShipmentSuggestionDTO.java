package com.paul.shitment.shipment_service.dto.shipment;

import java.util.UUID;

import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "ShipmentSuggestionDTO", description = "DTO que representa información resumida de un envío para sugerencias/autocompletado")
public record ShipmentSuggestionDTO(

        @Schema(
                description = "Identificador único del envío", 
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", 
                requiredMode = RequiredMode.REQUIRED)
        UUID id,

        @Schema(
                description = "Código de seguimiento del envío", 
                example = "AB3-C4D", 
                requiredMode = RequiredMode.REQUIRED)
        String trackingCode,

        @Schema(
                description = "Descripción del ítem enviado", 
                example = "Documentos importantes", 
                requiredMode = RequiredMode.REQUIRED)
        String itemDescription,

        @Schema(
                description = "Nombre completo del destinatario", 
                example = "María López", 
                requiredMode = RequiredMode.REQUIRED)
        String recipientName,

        @Schema(
                description = "CI/DNI del destinatario", 
                example = "87654321", 
                requiredMode = RequiredMode.REQUIRED)
        String recipientCi,

        @Schema(
                description = "Número de teléfono del destinatario", 
                example = "77787654321", 
                requiredMode = RequiredMode.NOT_REQUIRED)
        String recipientPhone,

        @Schema(
                description = "Estado actual del envío", 
                example = "PENDING", 
                requiredMode = RequiredMode.REQUIRED)
        ShipmentStatus status,

        @Schema(
                description = "Costo del envío", 
                example = "50.0", 
                requiredMode = RequiredMode.REQUIRED)
        Double shippingCost
) { }
