package com.paul.shitment.shipment_service.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "UserPasswordUpdateDto", description = "DTO para actualizar la contraseña de un usuario")
public record UserPasswordUpdateDto(

        @Schema(description = "Contraseña actual del usuario", example = "OldPass123", required = true)
        @NotBlank(message = "La contraseña actual es obligatoria")
        String oldPassword,

        @Schema(description = "Nueva contraseña del usuario, entre 8 y 15 caracteres válidos", example = "NewPass123#", required = true, pattern = "[a-zA-Z0-9._\\-#$]{8,15}")
        @Pattern(
                regexp = "[a-zA-Z0-9._\\-#$]{8,15}",
                message = "La nueva contraseña debe contener entre 8 y 15 caracteres válidos")
        @NotBlank(message = "La nueva contraseña es obligatoria")
        String newPassword
) { }