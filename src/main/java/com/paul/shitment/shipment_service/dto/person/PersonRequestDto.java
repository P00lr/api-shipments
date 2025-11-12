package com.paul.shitment.shipment_service.dto.person;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PersonRequestDto", description = "DTO para la creación o actualización de una persona")
public record PersonRequestDto(

        @Schema(description = "Nombre completo de la persona", example = "Juan Pérez", required = true)
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @Schema(description = "Número de CI/DNI de la persona", example = "12345678", required = true, pattern = "\\d{7,9}")
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{7,9}", message = "El DNI debe contener exactamente entre 7 y 9 dígitos numéricos")
        String ci,

        @Schema(description = "Número de teléfono de la persona", example = "77712345678", required = false, pattern = "\\d{8,15}")
        @Pattern(regexp = "\\d{8,15}", message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
        String phone
) {

    @Override
    public String toString() {
        return "PersonRequestDto {" +
                "Nombre='" + name + '\'' +
                ", CI='" + ci + '\'' +
                ", Teléfono='" + phone + '\'' +
                '}';
    }
}
