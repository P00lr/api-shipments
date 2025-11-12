package com.paul.shitment.shipment_service.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "UserResponseDto", description = "DTO que representa la información de un usuario")
public record UserResponseDto(

        @Schema(description = "Identificador único del usuario", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        UUID id,

        @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", required = true)
        String name,

        @Schema(description = "Número de teléfono del usuario", example = "77712345678", required = false)
        String phone,

        @Schema(description = "CI/DNI del usuario", example = "12345678", required = true)
        String ci,

        @Schema(description = "Nombre de usuario (username)", example = "juanperez", required = true)
        String username,

        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com", required = true)
        String email,

        @Schema(description = "Indica si el usuario está activo", example = "true", required = true)
        boolean active
) { }
