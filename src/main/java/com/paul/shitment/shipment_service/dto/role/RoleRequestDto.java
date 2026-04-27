package com.paul.shitment.shipment_service.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "RoleRequestDto", description = "DTO para la creación de un nuevo rol en el sistema")
public record RoleRequestDto(
    @Schema(
            description = "Nombre del rol, entre 3 y 60 caracteres",
            example = "ROLE_ADMIN",
            requiredMode = RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 60)
    @NotBlank(message = "El campo no debe estar vacio")
    @Size(min = 3, max = 60, message = "El campo debe tener entre 3 y 60 caracteres")
    String name
) {

}
