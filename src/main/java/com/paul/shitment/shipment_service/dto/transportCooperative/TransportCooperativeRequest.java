package com.paul.shitment.shipment_service.dto.transportCooperative;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Schema(name = "TransportCooperativeRequest", description = "DTO para la creación o actualización de una cooperativa de transporte")
public record TransportCooperativeRequest(

        @Schema(description = "Nombre de la cooperativa de transporte", example = "Cooperativa Central", required = true)
        @NotBlank(message = "El nombre de la cooperativa no puede estar vacío.")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.")
        String name,

        @Schema(description = "Indica si la cooperativa está activa", example = "true", required = true)
        boolean isActive
) { }
