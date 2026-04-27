package com.paul.shitment.shipment_service.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "PermissionRequestDto", description = "DTO para la creación de un nuevo permiso en el sistema")
public record PermissionRequestDto(
    @Schema(
            description = "Nombre del permiso, entre 3 y 60 caracteres",
            example = "CREATE_USER",
            requiredMode = RequiredMode.REQUIRED,
            minLength = 3,
            maxLength = 60)
    @NotBlank(message = "El campo no debe estar vacio")
    @Size(min = 3, max = 60, message = "El campo debe tener entre 3 y 60 caracteres")
    String name
) {

}
