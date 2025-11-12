package com.paul.shitment.shipment_service.dto.office;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OfficeResponseDto", description = "DTO que representa la información de una oficina")
public record OfficeResponseDto(

        @Schema(description = "Identificador único de la oficina", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", required = true)
        UUID id,

        @Schema(description = "Nombre de la oficina", example = "Oficina Central", required = true)
        String name,

        @Schema(description = "Dirección de la oficina", example = "Av. Siempre Viva 123", required = false)
        String address,

        @Schema(description = "Número de teléfono de la oficina", example = "77712345678", required = true)
        String phone,

        @Schema(description = "Indica si la oficina está activa", example = "true", required = true)
        boolean active
) { }

