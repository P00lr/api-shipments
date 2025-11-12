package com.paul.shitment.shipment_service.dto.shipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "ShipmentUpdateRequestDto", description = "DTO para actualizar los datos de un envío existente")
public record ShipmentUpdateRequestDto(

        @Schema(description = "Nombre completo del remitente", example = "Juan Pérez", required = false)
        String senderName,

        @Schema(description = "Número de teléfono del remitente", example = "77712345678", required = false, pattern = "\\d{8,15}")
        @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
        String senderPhone,

        @Schema(description = "CI/DNI del remitente", example = "12345678", required = true, pattern = "\\d{6,9}")
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{6,9}", message = "El DNI debe contener exactamente entre 6 y 9 dígitos numéricos")
        String senderCI,

        @Schema(description = "Nombre completo del destinatario", example = "María López", required = false)
        String recipientName,

        @Schema(description = "Número de teléfono del destinatario", example = "77787654321", required = false, pattern = "\\d{8,15}")
        @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
        String recipientPhone,

        @Schema(description = "CI/DNI del destinatario", example = "87654321", required = true, pattern = "\\d{6,9}")
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{6,9}", message = "El DNI debe contener exactamente entre 6 y 9 dígitos numéricos")
        String recipientCI,

        @Schema(description = "Descripción del ítem a enviar", example = "Documentos importantes", required = false)
        String itemDescription,

        @Schema(description = "Costo del envío, debe ser mayor o igual a 0", example = "50.0", required = false)
        @PositiveOrZero
        Double shippingCost
) { }
