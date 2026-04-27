package com.paul.shitment.shipment_service.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequestDto", description = "DTO para autenticar un usuario en el sistema")
public record LoginRequestDto(
    @Schema(
            description = "Nombre de usuario para acceder al sistema",
            example = "juanperez",
            requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "El username es obligatorio")
    String username,

    @Schema(
            description = "Contraseña del usuario",
            example = "Pass1234#",
            requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "La contraseña es obligatorio")
    String password
) {

}
