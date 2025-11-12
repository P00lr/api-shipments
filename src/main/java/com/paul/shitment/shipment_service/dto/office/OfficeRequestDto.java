package com.paul.shitment.shipment_service.dto.office;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OfficeRequestDto", description = "Datos necesarios para crear o actualizar una oficina")
public record OfficeRequestDto(

        @Schema(
            description = "Nombre de la oficina",
            example = "Oficina Central",
            required = true,
            pattern = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{4,60}"
        )
        @Pattern(
            regexp = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-]{4,60}",
            message = "El nombre debe contener entre 4 y 60 caracteres alfanuméricos, espacios, comas o guiones"
        )
        @NotBlank(message = "El nombre es obligatorio") 
        String name,

        @Schema(
            description = "Dirección de la oficina",
            example = "Av. Siempre Viva 123",
            required = false,
            pattern = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-#/]{4,60}"
        )
        @Pattern(
            regexp = "[a-zA-Z0-9\\s,\\.áéíóúÁÉÍÓÚñÑ\\-#/]{4,60}",
            message = "La dirección debe contener entre 4 y 60 caracteres válidos (letras, números, espacios, comas, puntos, guiones, # o /)"
        )
        String address,

        @Schema(
            description = "Número de celular de la oficina",
            example = "77712345678",
            required = true,
            pattern = "\\d{8,15}"
        )
        @Pattern(
            regexp = "\\d{8,15}",
            message = "El numero de celular debe contener entre 8 y 15 dígitos"
        )
        @NotBlank(message = "El numero de celular es obligatorio")
        String phone,

        @Schema(
            description = "Estado activo de la oficina",
            example = "true",
            required = true
        )
        boolean active
) { }
