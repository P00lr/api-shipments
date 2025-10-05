package com.paul.shitment.shipment_service.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(

    @NotBlank(message = "El nombre es obligatorio")
    String name, 

    @NotBlank(message = "El CI es obligatorio")
    @Pattern(regexp = "\\d{7,10}", 
    message = "El CI debe contener entre 6 y 10 dígitos numéricos")
    String ci, 
    
    @Pattern(regexp = "\\d{8,15}", 
    message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
    String phone,

    @Pattern(regexp = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{3,15}",
    message = "El username debe contener entre 3 y 15 caracteres")
    @NotBlank(message = "El username es obligatorio")
    String username,

    @Pattern(
    regexp = "[a-zA-Z0-9._\\-#$]{8,15}",
    message = "El password debe contener entre 8 y 15 caracteres válidos")
    @NotBlank(message = "El password es obligatorio")
    String password,

    @Email(message = "El email no es válido")
    @NotBlank(message = "El email es obligatorio")
    String email
) {

}
