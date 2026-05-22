package com.paul.shitment.shipment_service.dto.vehicle;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "VehicleResponseDto", description = "DTO que representa la información de un vehículo")
public record VehicleResponseDto(
    @Schema(
            description = "Identificador único del vehículo",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = RequiredMode.REQUIRED)
    UUID id,

    @Schema(
            description = "Código interno del vehículo",
            example = "VEH-001",
            requiredMode = RequiredMode.REQUIRED)
    String internalCode,

    @Schema(
            description = "Nombre de la cooperativa de transporte",
            example = "Cooperativa Central",
            requiredMode = RequiredMode.REQUIRED)
    String cooperativeName
) {
}
