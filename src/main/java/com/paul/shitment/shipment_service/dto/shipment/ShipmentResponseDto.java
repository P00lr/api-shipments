package com.paul.shitment.shipment_service.dto.shipment;

import java.time.LocalDateTime;
import java.util.UUID;

import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ShipmentResponseDto", description = "DTO que representa la información completa de un envío")
public record ShipmentResponseDto(

        @Schema(description = "Identificador único del envío", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        UUID id,

        @Schema(description = "Nombre de la oficina de origen", example = "Oficina Central", required = true)
        String nameOriginOffice,

        @Schema(description = "Nombre de la oficina de destino", example = "Sucursal Norte", required = true)
        String nameDestinationOffice,

        @Schema(description = "Nombre completo del remitente", example = "Juan Pérez", required = true)
        String senderName,

        @Schema(description = "CI/DNI del remitente", example = "12345678", required = true)
        String senderCi,

        @Schema(description = "Número de teléfono del remitente", example = "77712345678", required = false)
        String senderPhone,

        @Schema(description = "Nombre completo del destinatario", example = "María López", required = true)
        String recipientName,

        @Schema(description = "CI/DNI del destinatario", example = "87654321", required = true)
        String recipientCi,

        @Schema(description = "Número de teléfono del destinatario", example = "77787654321", required = false)
        String recipientPhone,

        @Schema(description = "Nombre del usuario que registró el envío", example = "admin", required = true)
        String nameUser,

        @Schema(description = "Descripción del ítem enviado", example = "Documentos importantes", required = true)
        String itemDescription,

        @Schema(description = "Código de seguimiento único del envío", example = "AB3-C4D", required = true)
        String trackingCode,

        @Schema(description = "Fecha y hora de creación del envío", example = "2025-11-12T14:30:00", required = true)
        LocalDateTime createAt,

        @Schema(description = "Fecha y hora de última actualización del envío", example = "2025-11-12T15:00:00", required = false)
        LocalDateTime updateAt,

        @Schema(description = "Fecha y hora de entrega del envío", example = "2025-11-13T10:00:00", required = false)
        LocalDateTime deliveredAt,

        @Schema(description = "Costo del envío", example = "50.0", required = true)
        Double shippingCost,

        @Schema(description = "Estado actual del envío", example = "DELIVERED", required = true)
        ShipmentStatus status
) { }
