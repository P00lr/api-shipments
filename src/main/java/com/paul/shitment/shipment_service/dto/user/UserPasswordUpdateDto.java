package com.paul.shitment.shipment_service.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserPasswordUpdateDto(
@NotBlank(message = "La contraseña actual es obligatoria")
String oldPassword,

@Pattern(
        regexp = "[a-zA-Z0-9._\\-#$]{8,15}",
        message = "La nueva contraseña debe contener entre 8 y 15 caracteres válidos")
    @NotBlank(message = "La nueva contraseña es obligatoria")
String newPassword
) {

}
