package com.paul.shitment.shipment_service.dto.person;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PersonRequestDto(
    @NotBlank(message = "El nombre es obligatorio")
    String name, 

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{6,9}", message = "El DNI debe contener exactamente 6 y 9 dígitos numéricos")
    String ci, 
    
    @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
    String phone
    ) {

}
