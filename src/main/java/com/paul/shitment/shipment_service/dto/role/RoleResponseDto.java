package com.paul.shitment.shipment_service.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "RoleResponseDto", description = "DTO que representa la información de un rol del sistema")
public record RoleResponseDto(
    @Schema(
            description = "Nombre del rol",
            example = "ROLE_ADMIN",
            requiredMode = RequiredMode.REQUIRED)
    @NotBlank
    String name
) {

}
