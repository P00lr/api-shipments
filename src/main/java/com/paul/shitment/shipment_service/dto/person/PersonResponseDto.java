package com.paul.shitment.shipment_service.dto.person;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PersonResponseDto", description = "DTO que representa la información de una persona")
public record PersonResponseDto(

        @Schema(description = "Identificador único de la persona", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        UUID id,

        @Schema(description = "Nombre completo de la persona", example = "Juan Pérez", required = true)
        String name,

        @Schema(description = "Número de CI/DNI de la persona", example = "12345678", required = true)
        String ci,

        @Schema(description = "Número de teléfono de la persona", example = "77712345678", required = false)
        String phone,

        @Schema(description = "Indica si la persona está registrada en el sistema", example = "true", required = true)
        boolean registered,

        @Schema(description = "Indica si la persona está activa", example = "true", required = true)
        boolean active
) { }











