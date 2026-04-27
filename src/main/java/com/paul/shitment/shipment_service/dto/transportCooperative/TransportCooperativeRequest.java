package com.paul.shitment.shipment_service.dto.transportCooperative;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Schema(name = "TransportCooperativeRequest", description = "DTO para la creación o actualización de una cooperativa de transporte")
public record TransportCooperativeRequest(

        @Schema(
                description = "Nombre de la cooperativa de transporte, entre 3 y 100 caracteres",
                example = "Cooperativa Central",
                requiredMode = RequiredMode.REQUIRED,
                minLength = 3,
                maxLength = 100)
        @NotBlank(message = "El nombre de la cooperativa no puede estar vacío.")
        @Size(
                min = 3,
                max = 100,
                message = "El nombre debe tener entre 3 y 100 caracteres.")
        String name,

        @Schema(
                description = "Indica si la cooperativa está habilitada para operar",
                example = "true",
                requiredMode = RequiredMode.REQUIRED)
        @NotNull(message = "El campo habilidado es obligatorio")
        boolean enabled
) { }
