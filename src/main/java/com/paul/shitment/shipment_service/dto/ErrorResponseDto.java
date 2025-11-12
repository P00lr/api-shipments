package com.paul.shitment.shipment_service.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(name = "ErrorResponseDto", description = "DTO que representa la estructura de respuesta en caso de error")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

    @Schema(description = "Tipo de error", example = "ValidationError", required = true)
    private String error;

    @Schema(description = "Mensaje detallado del error", example = "El nombre del usuario es obligatorio", required = true)
    private String message;

    @Schema(description = "Fecha y hora en que ocurrió el error", example = "2025-11-12T14:30:00", required = true)
    private LocalDateTime date;

    @Schema(description = "Código HTTP del error", example = "400", required = true)
    private Integer code;

    @Schema(description = "Ruta del endpoint donde ocurrió el error", example = "/api/v1/users", required = true)
    private String path;
}
