package com.paul.shitment.shipment_service.dto.person;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "PersonResponseDto", description = "DTO que representa la información de una persona")
public record PersonResponseDto(

        @Schema(
                description = "Identificador único de la persona", 
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", 
                requiredMode = RequiredMode.REQUIRED)
        UUID id,

        @Schema(
                description = "Nombre completo de la persona", 
                example = "Juan Pérez", 
                requiredMode = RequiredMode.REQUIRED)
        String name,

        @Schema(
                description = "Número de CI/DNI de la persona", 
                example = "12345678", 
                requiredMode = RequiredMode.REQUIRED)
        String ci,

        @Schema(
                description = "Número de teléfono de la persona", 
                example = "77712345678", 
                requiredMode = RequiredMode.REQUIRED)
        String phone,

        @Schema(
                description = "Indica si la persona está registrada en el sistema", 
                example = "true", 
                requiredMode = RequiredMode.REQUIRED)
        boolean registered,

        @Schema(
                description = "Indica si la persona está activa", 
                example = "true", 
                requiredMode = RequiredMode.REQUIRED)
        boolean active
) { }











