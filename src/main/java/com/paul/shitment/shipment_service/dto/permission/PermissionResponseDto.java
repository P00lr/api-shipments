package com.paul.shitment.shipment_service.dto.permission;

import jakarta.validation.constraints.NotBlank;

public record PermissionResponseDto(
    @NotBlank
    String name
) {

}
