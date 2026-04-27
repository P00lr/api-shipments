package com.paul.shitment.shipment_service.dto.shipment;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "ShipmentPersonDto", description = "DTO que representa información de una persona asociada a un envío")
public record ShipmentPersonDto(

    @Schema(
            description = "Tipo de documento de la persona",
            example = "CI",
            requiredMode = RequiredMode.REQUIRED)
    String documentType,

    @Schema(
            description = "Número de documento de la persona",
            example = "12345678",
            requiredMode = RequiredMode.REQUIRED)
    String documentNumber,

    @Schema(
            description = "Nombre completo de la persona",
            example = "Juan Pérez",
            requiredMode = RequiredMode.REQUIRED)
    String fullName,

    @Schema(
            description = "Número de teléfono de la persona",
            example = "77771234",
            requiredMode = RequiredMode.NOT_REQUIRED)
    String phone
) {

}
