package com.paul.shitment.shipment_service.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionRequestDto(
    @NotBlank(message = "El campo no debe estar vacio")
    @Size(min = 3, max = 60, message = "El campo debe tener entre 3 y 60 caracteres")
    String name
) {

}
