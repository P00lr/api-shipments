package com.paul.shitment.shipment_service.dto.shipment;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ShipmentPersonRequestDto(
        @Schema(
                description = "CI/DNI", 
                example = "12345678", 
                requiredMode = RequiredMode.REQUIRED, 
                pattern = "\\d{7,9}")
        @NotBlank(message = "El CI es obligatorio")
        @Pattern(
                regexp = "\\d{7,9}", 
                message = "El CI/DNI debe contener entre 7 y 9 dígitos numéricos")
        String ci,

        @Schema(
                description = "Nombre completo", 
                example = "Juan Pérez", 
                requiredMode = RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @Schema(
                description = "Número de teléfono del remitente", 
                example = "77771234", 
                requiredMode = RequiredMode.NOT_REQUIRED, 
                pattern = "\\d{8,15}")
        @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
        String phone
) {

}
