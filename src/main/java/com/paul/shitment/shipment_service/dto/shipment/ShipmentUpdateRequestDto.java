package com.paul.shitment.shipment_service.dto.shipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record ShipmentUpdateRequestDto(

    String senderName,

    @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
    String senderPhone,

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{6,9}", message = "El DNI debe contener exactamente 6 y 9 dígitos numéricos")
    String senderCI,
    
    String recipientName,

    @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
    String recipientPhone,
    
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{6,9}", message = "El DNI debe contener exactamente 6 y 9 dígitos numéricos")
    String recipientCI,

    String itemDescription,

    @PositiveOrZero
    Double shippingCost
) {}
