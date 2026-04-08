package com.paul.shitment.shipment_service.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
    @NotBlank(message = "El username es obligatorio")
    String username,

    @NotBlank(message = "La contraseña es obligatorio")
    String password
) {

}
