package com.paul.shitment.shipment_service.dto.user;

import java.util.UUID;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(name = "UserRequestDto", description = "DTO para la creación de un nuevo usuario")
public record UserRequestDto(

        @Valid
        PersonRequestDto person,

        @Schema(
                description = "Nombre de usuario (username)", 
                example = "juanperez", 
                requiredMode = RequiredMode.REQUIRED, 
                pattern = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{3,15}")
        @Pattern(
                regexp = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{3,15}", 
                message = "El username debe contener entre 3 y 15 caracteres")
        @NotBlank(message = "El username es obligatorio")
        String username,

        @Schema(
                description = "Contraseña del usuario, entre 8 y 15 caracteres válidos", 
                example = "Pass1234#", 
                requiredMode = RequiredMode.REQUIRED, 
                pattern = "[a-zA-Z0-9._\\-#$]{8,15}")
        @Pattern(
                regexp = "[a-zA-Z0-9._\\-#$]{8,15}", 
                message = "El password debe contener entre 8 y 15 caracteres válidos")
        @NotBlank(message = "El password es obligatorio")
        String password,

        @Schema(
                description = "Correo electrónico del usuario", 
                example = "juan.perez@email.com", 
                requiredMode = RequiredMode.REQUIRED)
        @Email(message = "El email no es válido")
        @NotBlank(message = "El email es obligatorio")
        String email,

        @NotNull(message = "La oficina es obligatoria")
        UUID officeId
) { }
