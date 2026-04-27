package com.paul.shitment.shipment_service.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

import java.util.UUID;

@Schema(name = "UserResponseDto", description = "DTO que representa la información de un usuario")
public record UserResponseDto(

        @Schema(
                description = "Identificador único del usuario",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                requiredMode = RequiredMode.REQUIRED)
        UUID id,

        @Schema(
                description = "Tipo de documento de identidad del usuario",
                example = "CI",
                requiredMode = RequiredMode.REQUIRED)
        String documentType,

        @Schema(
                description = "CI/DNI del usuario",
                example = "12345678",
                requiredMode = RequiredMode.REQUIRED)
        String documentNumber,


        @Schema(
                description = "Nombre completo del usuario",
                example = "Juan Pérez",
                requiredMode = RequiredMode.REQUIRED)
        String fullName,

        @Schema(
                description = "Número de teléfono del usuario",
                example = "77712345678",
                requiredMode = RequiredMode.NOT_REQUIRED)
        String phone,


        @Schema(
                description = "Nombre de usuario (username)",
                example = "juanperez",
                requiredMode = RequiredMode.REQUIRED)
        String username,

        @Schema(
                description = "Correo electrónico del usuario",
                example = "juan.perez@email.com",
                requiredMode = RequiredMode.REQUIRED)
        String email,

        @Schema(
                description = "Nombre de la oficina asignada al usuario",
                example = "Oficina Central",
                requiredMode = RequiredMode.REQUIRED)
        String officeName,

        @Schema(
                description = "Indica si el usuario está activo",
                example = "true",
                requiredMode = RequiredMode.REQUIRED)
        boolean active
) { }
