package com.paul.shitment.shipment_service.controllers;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.person.PersonResponseDto;
import com.paul.shitment.shipment_service.services.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/persons")
@Tag(name = "Persons", description = "Endpoints para la gestión de personas")
public class PersonController {

    private final PersonService personService;

    @Operation(summary = "Obtener personas paginadas", description = "Retorna un listado paginado de todas las personas registradas")
    @ApiResponse(responseCode = "200", description = "Listado de personas obtenido exitosamente")
    @GetMapping
    public ResponseEntity<PageResponse<PersonResponseDto>> getAllPersonsPaged(@NonNull Pageable pageable) {
        return ResponseEntity.ok(personService.getAllPersonsPaged(pageable));
    }

    @Operation(summary = "Obtener persona por ID", description = "Retorna los datos completos de una persona específica por su UUID")
    @ApiResponse(responseCode = "200", description = "Persona encontrada")
    @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getPerson(
            @Parameter(description = "UUID de la persona")@NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @Operation(summary = "Obtener persona por CI", description = "Retorna los datos de una persona usando su número de documento (CI)")
    @ApiResponse(responseCode = "200", description = "Persona encontrada")
    @ApiResponse(responseCode = "404", description = "Persona con ese CI no existe")
    @GetMapping("/ci/{ci}")
    public ResponseEntity<PersonResponseDto> getPersonByCI(
            @Parameter(description = "CI de la persona") @PathVariable String ci) {
        return ResponseEntity.ok(personService.getPersonByCI(ci));
    }

    @Operation(summary = "Verificar existencia por teléfono", description = "Verifica si existe una persona registrada con el número de teléfono proporcionado")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada, retorna true o false")
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Boolean> existsByPhone(
            @Parameter(description = "Número de teléfono a verificar") @PathVariable String phone) {
        return ResponseEntity.ok(personService.existsByPhone(phone));
    }

    @Operation(summary = "Crear nueva persona", description = "Crea un nuevo registro de persona con los datos proporcionados")
    @ApiResponse(responseCode = "201", description = "Persona creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o persona ya existe")
    @PostMapping
    public ResponseEntity<PersonResponseDto> createPerson(
            @Parameter(description = "Datos de la persona a crear") @Valid @RequestBody PersonRequestDto personDto) {
        PersonResponseDto created = personService.createPerson(personDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar persona", description = "Actualiza los datos de una persona existente")
    @ApiResponse(responseCode = "200", description = "Persona actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDto> updatePerson(
            @Parameter(description = "Datos actualizados de la persona") @Valid @RequestBody PersonRequestDto personDto,
            @Parameter(description = "UUID de la persona a actualizar")@NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(personService.updatePerson(id, personDto));
    }

    @Operation(summary = "Desactivar persona", description = "Realiza una eliminación lógica (soft delete) de una persona")
    @ApiResponse(responseCode = "200", description = "Persona desactivada exitosamente")
    @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<PersonResponseDto> deactivatePerson(
            @Parameter(description = "UUID de la persona a desactivar")@NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(personService.deletePerson(id));
    }
}

