package com.paul.shitment.shipment_service.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(name = "PageResponse", description = "DTO genérico para respuestas paginadas")
@Data
@AllArgsConstructor
public class PageResponse<T> {

    @Schema(description = "Lista de elementos en la página actual")
    private List<T> content;

    @Schema(description = "Número de la página actual (base 0)", example = "0")
    private int pageNo;

    @Schema(description = "Cantidad de elementos por página", example = "5")
    private int size;

    @Schema(description = "Cantidad total de elementos disponibles", example = "100")
    private Long totalElements;

    @Schema(description = "Cantidad total de páginas disponibles", example = "20")
    private int totalPages;

    @Schema(description = "Indica si esta es la primera página", example = "true")
    private boolean first;

    @Schema(description = "Indica si esta es la última página", example = "false")
    private boolean last;
}
