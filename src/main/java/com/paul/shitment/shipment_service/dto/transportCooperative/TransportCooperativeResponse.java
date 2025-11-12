package com.paul.shitment.shipment_service.dto.transportCooperative;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(name = "TransportCooperativeResponse", description = "DTO que representa la información de una cooperativa de transporte")
public record TransportCooperativeResponse(

        @Schema(description = "Identificador único de la cooperativa", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        UUID id,

        @Schema(description = "Nombre de la cooperativa de transporte", example = "Cooperativa Central", required = true)
        String name,

        @Schema(description = "Indica si la cooperativa está activa", example = "true", required = true)
        boolean isActive
) { }
