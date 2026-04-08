package com.paul.shitment.shipment_service.dto.shipment;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "ShipmentRequestDto", description = "DTO para la creación de un envío")
public record ShipmentRequestDto(


        @Schema(
                description = "UUID de la oficina de destino", 
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", 
                requiredMode = RequiredMode.REQUIRED)
        @NotNull
        UUID destinationOfficeId,


        ShipmentPersonRequestDto sender,

        ShipmentPersonRequestDto recipient,
        
        @Schema(
                description = "Descripción del ítem a enviar", 
                example = "Documentos importantes", 
                requiredMode = RequiredMode.REQUIRED, 
                pattern = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{4,260}")
        @NotBlank
        @Pattern(
                regexp = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{4,260}", 
                message = "Solo se aceptan letras y números")
        String itemDescription,

        @Schema(
                description = "Costo del envío en moneda local", 
                example = "50.0", 
                requiredMode = RequiredMode.REQUIRED)
        @NotNull(message = "El costo de envío es obligatorio")
        @Positive(message = "El costo de envío debe ser mayor que 0")
        Double shippingCost
) { }

