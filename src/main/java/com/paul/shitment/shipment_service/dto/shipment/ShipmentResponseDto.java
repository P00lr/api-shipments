package com.paul.shitment.shipment_service.dto.shipment;

import java.time.LocalDateTime;
import java.util.UUID;

import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "ShipmentResponseDto", description = "DTO que representa la información completa de un envío")
public record ShipmentResponseDto(

        @Schema(
                description = "Identificador único del envío", 
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", 
                requiredMode = RequiredMode.REQUIRED)
        UUID id,

        @Schema(
                description = "Nombre de la oficina de origen", 
                example = "Oficina Central", 
                requiredMode = RequiredMode.REQUIRED)
        String nameOriginOffice,

        @Schema(
                description = "Nombre de la oficina de destino", 
                example = "Sucursal Norte", 
                requiredMode = RequiredMode.REQUIRED)
        String nameDestinationOffice,

        @Schema(
                description = "Nombre completo del remitente", 
                example = "Juan Pérez", 
                requiredMode = RequiredMode.REQUIRED)
        String senderName,

        @Schema(
                description = "CI/DNI del remitente", 
                example = "12345678", 
                requiredMode = RequiredMode.REQUIRED)
        String senderCi,

        @Schema(
                description = "Número de teléfono del remitente", 
                example = "77712345678", 
                requiredMode = RequiredMode.NOT_REQUIRED)
        String senderPhone,

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
                requiredMode = RequiredMode.REQUIRED)
        String recipientPhone,

        @Schema(
                description = "Nombre del usuario que registró el envío", 
                example = "admin", 
                requiredMode = RequiredMode.REQUIRED)
        String nameUser,

        @Schema(
                description = "Descripción del ítem enviado", 
                example = "Documentos importantes", 
                requiredMode = RequiredMode.REQUIRED)
        String itemDescription,

        @Schema(
                description = "Código de seguimiento único del envío", 
                example = "AB3-C4D", 
                requiredMode = RequiredMode.REQUIRED)
        String trackingCode,

        @Schema(
                description = "Fecha y hora de creación del envío", 
                example = "2025-11-12T14:30:00", 
                requiredMode = RequiredMode.REQUIRED)
        LocalDateTime createAt,

        @Schema(
                description = "Fecha y hora de última actualización del envío", 
                example = "2025-11-12T15:00:00", 
                requiredMode = RequiredMode.NOT_REQUIRED)
        LocalDateTime updateAt,

        @Schema(
                description = "Fecha y hora de entrega del envío", 
                example = "2025-11-13T10:00:00", 
                requiredMode = RequiredMode.REQUIRED)
        LocalDateTime deliveredAt,

        @Schema(
                description = "Costo del envío", 
                example = "50.0", 
                requiredMode = RequiredMode.REQUIRED)
        Double shippingCost,

        @Schema(
                description = "Estado actual del envío", 
                example = "DELIVERED", requiredMode = RequiredMode.REQUIRED)
        ShipmentStatus status
) { }
