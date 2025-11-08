package com.paul.shitment.shipment_service.dto.transportCooperative;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record TransportCooperativeRequest(

        @NotBlank(message = "El nombre de la cooperativa no puede estar vacío.") 
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres.") 
        String name,
        boolean isActive) {
}
