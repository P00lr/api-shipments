package com.paul.shitment.shipment_service.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "UserRequestDto", description = "DTO para la creación de un nuevo usuario")
public record UserRequestDto(

        @Schema(description = "Nombre completo del usuario", example = "Juan Pérez", required = true)
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @Schema(description = "CI/DNI del usuario", example = "12345678", required = true, pattern = "\\d{7,10}")
        @NotBlank(message = "El CI es obligatorio")
        @Pattern(regexp = "\\d{7,10}", message = "El CI debe contener entre 6 y 10 dígitos numéricos")
        String ci,

        @Schema(description = "Número de teléfono del usuario", example = "77712345678", required = false, pattern = "\\d{8,15}")
        @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
        String phone,

        @Schema(description = "Nombre de usuario (username)", example = "juanperez", required = true, pattern = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{3,15}")
        @Pattern(regexp = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{3,15}", message = "El username debe contener entre 3 y 15 caracteres")
        @NotBlank(message = "El username es obligatorio")
        String username,

        @Schema(description = "Contraseña del usuario, entre 8 y 15 caracteres válidos", example = "Pass1234#", required = true, pattern = "[a-zA-Z0-9._\\-#$]{8,15}")
        @Pattern(regexp = "[a-zA-Z0-9._\\-#$]{8,15}", message = "El password debe contener entre 8 y 15 caracteres válidos")
        @NotBlank(message = "El password es obligatorio")
        String password,

        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com", required = true)
        @Email(message = "El email no es válido")
        @NotBlank(message = "El email es obligatorio")
        String email
) { }
