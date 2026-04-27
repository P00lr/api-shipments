package com.paul.shitment.shipment_service.dto.transportCooperative;

import java.util.Set;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(name = "CooperativeOfficeRequest", description = "DTO para asignar oficinas a una cooperativa de transporte")
public record CooperativeOfficeRequest(

    @Schema(
            description = "Identificador único de la cooperativa",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            requiredMode = RequiredMode.REQUIRED)
    @NotNull(message = "El ID de la cooperativa es obligatorio")
    UUID cooperativeId,

    @Schema(
            description = "Conjunto de UUIDs de oficinas a asignar a la cooperativa",
            example = "[\"3fa85f64-5717-4562-b3fc-2c963f66afa6\", \"4gb86f65-6818-4663-c4gd-3d974g77bgb7\"]",
            requiredMode = RequiredMode.REQUIRED)
    @NotEmpty(message = "Debes incluir al menos una oficina")
    Set<UUID> officesId
) {

}
