package com.paul.shitment.shipment_service.dto.transportCooperative;

import java.util.List;
import java.util.UUID;

import com.paul.shitment.shipment_service.dto.office.OfficeResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "CooperativeWhithOfficesResponse", description = "DTO que contiene información de una cooperativa con sus oficinas asociadas")
public record CooperativeWhithOfficesResponse(

    @Schema(
            description = "Identificador único de la cooperativa",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            requiredMode = RequiredMode.REQUIRED)
    UUID cooperativeId,

    @Schema(
            description = "Nombre de la cooperativa de transporte",
            example = "Cooperativa Central",
            requiredMode = RequiredMode.REQUIRED)
    String cooperativeName,

    @Schema(
            description = "Lista de oficinas pertenecientes a la cooperativa",
            requiredMode = RequiredMode.REQUIRED)
    List<OfficeResponse> offices

) {

}
