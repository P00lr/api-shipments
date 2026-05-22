package com.paul.shitment.shipment_service.dto.vehicle;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "VehicleUpdateRequestDto", description = "DTO para la actualización de vehículos")
public record VehicleUpdateRequestDto(
    @Schema(
            description = "Código interno del vehículo, entre 3 y 50 caracteres",
            example = "VEH-001",
            requiredMode = RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 50)
    @NotBlank(message = "El código interno no debe estar vacío")
    @Size(min = 3, max = 50, message = "El código interno debe tener entre 3 y 50 caracteres")
    @Pattern(
            regexp = "[a-zA-Z0-9\\-]{3,50}",
            message = "El código interno solo puede contener letras, números y guiones")
    String internalCode,

    @Schema(
            description = "UUID de la cooperativa de transporte a la que pertenece el vehículo",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = RequiredMode.REQUIRED)
    @NotNull(message = "El UUID de la cooperativa no debe ser nulo")
    UUID cooperativeId
) {
}
