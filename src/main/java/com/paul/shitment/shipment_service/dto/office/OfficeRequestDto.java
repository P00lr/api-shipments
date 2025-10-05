package com.paul.shitment.shipment_service.dto.office;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record OfficeRequestDto(

        @Pattern(regexp = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{4,60}",
        message = "El nombre debe contener entre 4 y 60 caracteres alfanuméricos, espacios, comas o guiones")
        @NotBlank(message = "El nombre es obligatorio") 
        String name,

        @Pattern(regexp = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-#/]{4,60}",
        message = "La dirección debe contener entre 4 y 60 caracteres válidos (letras, números, espacios, comas, puntos, guiones, # o /)")
        String address,

        @Pattern(regexp = "\\d{8,15}")
        @NotBlank(message = "El numero de celular es obligatorio")
        String phone,
        
        boolean active) {

        


}
