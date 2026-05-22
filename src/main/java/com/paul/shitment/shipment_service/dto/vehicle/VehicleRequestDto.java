package com.paul.shitment.shipment_service.dto.vehicle;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "VehicleRequestDto", description = "DTO para la creación y actualización de vehículos")
public record VehicleRequestDto(
    @Schema(
            description = "Código interno del vehículo, entre 3 y 50 caracteres",
            example = "VEH-001",
            requiredMode = RequiredMode.REQUIRED,
            minLength = 1,
            maxLength = 5)
    @NotBlank(message = "El código interno no debe estar vacío")
    @Size(min = 1, max = 5, message = "El código interno debe tener entre 1 y 5 caracteres")
    @Pattern(
            regexp = "[a-zA-Z0-9\\-]{1,5}",
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
