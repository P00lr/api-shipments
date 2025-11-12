package com.paul.shitment.shipment_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/persons")
@Tag(name = "Persons", description = "Endpoints para la gestión de personas")
public class PersonController {

    private final PersonService personService;

    @Operation(summary = "Obtener todas las personas")
    @GetMapping
    public ResponseEntity<List<PersonResponseDto>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @Operation(summary = "Obtener personas paginadas")
    @GetMapping("/paged")
    public ResponseEntity<PageResponse<PersonResponseDto>> getAllPersonsPaged(
            @Parameter(description = "Número de página (inicia en 0)") int pageNo,
            @Parameter(description = "Cantidad de registros por página") int size,
            @Parameter(description = "Campo por el cual ordenar") String sortBy) {
        return ResponseEntity.ok(personService.getAllPersonsPaged(pageNo, size, sortBy));
    }

    @Operation(summary = "Obtener persona por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getPerson(
            @Parameter(description = "UUID de la persona") @PathVariable UUID id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @Operation(summary = "Obtener persona por CI")
    @GetMapping("/ci/{ci}")
    public ResponseEntity<PersonResponseDto> getPersonByCI(
            @Parameter(description = "CI de la persona") @PathVariable String ci) {
        return ResponseEntity.ok(personService.getPersonByCI(ci));
    }

    @Operation(summary = "Verificar existencia por número de teléfono")
    @GetMapping("/phone/{phone}")
    public ResponseEntity<Boolean> existsByPhone(
            @Parameter(description = "Número de teléfono a verificar") @PathVariable String phone) {
        return ResponseEntity.ok(personService.existsByPhone(phone));
    }

    @Operation(summary = "Crear nueva persona")
    @PostMapping
    public ResponseEntity<PersonResponseDto> createPerson(
            @Parameter(description = "Datos de la persona a crear") @Valid @RequestBody PersonRequestDto personDto) {
        PersonResponseDto created = personService.createPerson(personDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar persona existente")
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDto> updatePerson(
            @Parameter(description = "Datos actualizados de la persona") @Valid @RequestBody PersonRequestDto personDto,
            @Parameter(description = "UUID de la persona a actualizar") @PathVariable UUID id) {
        return ResponseEntity.ok(personService.updatePerson(id, personDto));
    }

    @Operation(summary = "Desactivar persona (eliminación lógica)")
    @DeleteMapping("/{id}")
    public ResponseEntity<PersonResponseDto> deactivatePerson(
            @Parameter(description = "UUID de la persona a desactivar") @PathVariable UUID id) {
        return ResponseEntity.ok(personService.deletePerson(id));
    }
}

