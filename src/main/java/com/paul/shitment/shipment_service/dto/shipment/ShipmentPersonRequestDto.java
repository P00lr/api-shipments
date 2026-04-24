package com.paul.shitment.shipment_service.dto.shipment;

import com.paul.shitment.shipment_service.models.enums.DocumentType;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ShipmentPersonRequestDto(

        @Schema(
                description = "Tipo de documento",
                example = "CI, NIT, LICENSE, PASSPORT",
                requiredMode = RequiredMode.REQUIRED)
        @NotNull(message = "El tipo de documento es obligatorio")
        DocumentType documentType,

        @Schema(
                description = "Número de documento",
                example = "12345678",
                requiredMode = RequiredMode.REQUIRED)
        @NotBlank(message = "El número de documento es obligatorio")
        @Pattern(
                regexp = "\\d{7,12}",
                message = "El documento debe contener solo números")
        String documentNumber,

        @Schema(
                description = "Nombre completo",
                example = "Juan Pérez",
                requiredMode = RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre es obligatorio")
        String fullName,

        @Schema(
                description = "Número de teléfono",
                example = "77771234",
                requiredMode = RequiredMode.NOT_REQUIRED)
        @Pattern(
                regexp = "\\d{8,15}",
                message = "El teléfono debe contener entre 8 y 15 dígitos")
        String phone
) {
}
