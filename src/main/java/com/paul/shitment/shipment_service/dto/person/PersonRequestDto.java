package com.paul.shitment.shipment_service.dto.person;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import com.paul.shitment.shipment_service.models.enums.DocumentType;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(name = "PersonRequestDto", description = "DTO para la creación o actualización de una persona")
public record PersonRequestDto(

        @Schema(
            description = "Tipo de  documento de la persona", 
            example = " CI, NIT, PASSPORT", 
            requiredMode = RequiredMode.REQUIRED)
        @NotNull(message = "El campo tipo de documento es obligatorio")
        DocumentType documentType,

        @Schema(
            description = "Número de documento de la persona", 
            example = "1234567", 
            requiredMode = RequiredMode.REQUIRED, 
            pattern = "\\d{7,9}")
        @NotBlank(message = "El numero de documento es obligatorio")
        @Pattern(regexp = "\\d{7,9}", message = "El numero de documento debe contener exactamente entre 7 y 9 dígitos numéricos")
        String documentNumber,

        @Schema(
            description = "Nombre completo de la persona", 
            example = "Juan Pérez", 
            requiredMode = RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre es obligatorio")
        String fullName,


        @Schema(
            description = "Número de teléfono de la persona", 
            example = "77712345678", 
            requiredMode = RequiredMode.REQUIRED, 
            pattern = "\\d{8,15}")
        @Pattern(
            regexp = "\\d{8,15}", 
            message = "El teléfono debe contener entre 8 y 15 dígitos numéricos")
        String phone
) {

}
