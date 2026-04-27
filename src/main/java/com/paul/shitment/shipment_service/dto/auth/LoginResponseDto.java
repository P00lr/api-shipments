package com.paul.shitment.shipment_service.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "LoginResponseDto", description = "DTO que contiene el token JWT después de la autenticación exitosa")
public record LoginResponseDto(
    @Schema(
            description = "Token JWT para autenticar solicitudes posteriores",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            requiredMode = RequiredMode.REQUIRED)
    String token
) {

}
