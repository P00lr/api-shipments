package com.paul.shitment.shipment_service.dto.role;

import jakarta.validation.constraints.NotBlank;

public record RoleResponseDto(
    @NotBlank
    String name
) {

}
