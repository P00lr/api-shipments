package com.paul.shitment.shipment_service.dto.transportCooperative;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CooperativeOfficeRequest(

    @NotNull(message = "El ID de la cooperativa es obligatorio")
    UUID cooperativeId,

    @NotEmpty(message = "Debes incluir al menos una oficina")
    Set<UUID> officesId
) {

}
