package com.paul.shitment.shipment_service.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "PermissionResponseDto", description = "DTO que representa la información de un permiso del sistema")
public record PermissionResponseDto(
    @Schema(
            description = "Nombre del permiso",
            example = "CREATE_USER",
            requiredMode = RequiredMode.REQUIRED)
    @NotBlank
    String name
) {

}
