package com.paul.shitment.shipment_service.dto.shipment;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record ShipmentRequestDto(


    @NotNull
    UUID originOfficeId,

    @NotNull
    UUID destinationOfficeId,


    //----sender---
    @NotBlank(message = "El CI del remitente es obligatorio")
    @Pattern(regexp = "\\d{7,9}", message = "El DNI debe contener exactamente 7 y 9 dígitos numéricos")
    String senderCI,

    @NotBlank(message = "El nombre del remitente es obligatorio")
    String senderName,

    @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
    String senderPhone,

    //----recipinte---
    @NotBlank(message = "El CI del destinatario es obligatorio")
    @Pattern(regexp = "\\d{7,9}", message = "El DNI debe contener exactamente 7 y 9 dígitos numéricos")
    String recipientCI,

    @NotBlank(message = "El nombre del destinatario es obligatorio")
    String recipientName,

    @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
    String recipientPhone,

    @NotNull
    UUID userId,

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{4,260}",
    message = "Solo se acepta letras y numeros")
    String itemDescription,

    @NotNull(message = "El costo de envío es obligatorio")
    @Positive(message = "El costo de envío debe ser mayor que 0")
    Double shippingCost
) {

}
