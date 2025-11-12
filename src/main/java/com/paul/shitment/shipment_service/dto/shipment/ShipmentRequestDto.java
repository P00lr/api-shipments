package com.paul.shitment.shipment_service.dto.shipment;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ShipmentRequestDto", description = "DTO para la creación de un envío")
public record ShipmentRequestDto(

        @Schema(description = "UUID de la oficina de origen", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        @NotNull
        UUID originOfficeId,

        @Schema(description = "UUID de la oficina de destino", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        @NotNull
        UUID destinationOfficeId,

        //----Remitente----
        @Schema(description = "CI/DNI del remitente", example = "12345678", required = true, pattern = "\\d{7,9}")
        @NotBlank(message = "El CI del remitente es obligatorio")
        @Pattern(regexp = "\\d{7,9}", message = "El DNI debe contener entre 7 y 9 dígitos numéricos")
        String senderCI,

        @Schema(description = "Nombre completo del remitente", example = "Juan Pérez", required = true)
        @NotBlank(message = "El nombre del remitente es obligatorio")
        String senderName,

        @Schema(description = "Número de teléfono del remitente", example = "77712345678", required = false, pattern = "\\d{8,15}")
        @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
        String senderPhone,

        //----Destinatario----
        @Schema(description = "CI/DNI del destinatario", example = "87654321", required = true, pattern = "\\d{7,9}")
        @NotBlank(message = "El CI del destinatario es obligatorio")
        @Pattern(regexp = "\\d{7,9}", message = "El DNI debe contener entre 7 y 9 dígitos numéricos")
        String recipientCI,

        @Schema(description = "Nombre completo del destinatario", example = "María López", required = true)
        @NotBlank(message = "El nombre del destinatario es obligatorio")
        String recipientName,

        @Schema(description = "Número de teléfono del destinatario", example = "77787654321", required = false, pattern = "\\d{8,15}")
        @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
        String recipientPhone,

        @Schema(description = "UUID del usuario que crea el envío", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        @NotNull
        UUID userId,

        @Schema(description = "Descripción del ítem a enviar", example = "Documentos importantes", required = true, pattern = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{4,260}")
        @NotBlank
        @Pattern(regexp = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{4,260}", message = "Solo se aceptan letras y números")
        String itemDescription,

        @Schema(description = "Costo del envío en moneda local", example = "50.0", required = true)
        @NotNull(message = "El costo de envío es obligatorio")
        @Positive(message = "El costo de envío debe ser mayor que 0")
        Double shippingCost
) { }

