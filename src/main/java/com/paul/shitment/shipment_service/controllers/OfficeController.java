package com.paul.shitment.shipment_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.dto.office.OfficeResponseDto;
import com.paul.shitment.shipment_service.services.OfficeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/offices")
@AllArgsConstructor
@Tag(name = "Offices", description = "Endpoints para la gestión de oficinas")
public class OfficeController {

    private final OfficeService officeService;

    @Operation(summary = "Obtener todas las oficinas", description = "Devuelve la lista completa de oficinas activas")
    @GetMapping
    public ResponseEntity<List<OfficeResponseDto>> getAllOffices() {
        return ResponseEntity.ok(officeService.getAllOffices());
    }

    @Operation(summary = "Obtener oficinas paginadas", description = "Devuelve oficinas con paginación, tamaño y orden configurable")
    @GetMapping("/paged")
    public ResponseEntity<PageResponse<OfficeResponseDto>> getAllOfficesPaged(
        @Parameter(description = "Número de página, empieza en 0") @RequestParam(defaultValue = "0") int pageNo, 
        @Parameter(description = "Cantidad de registros por página") @RequestParam(defaultValue = "5") int size,
        @Parameter(description = "Campo por el cual ordenar") @RequestParam(defaultValue = "username") String sortBy) {
        return ResponseEntity.ok(officeService.getAllOfficesPaged(pageNo, size, sortBy));
    }

    @Operation(summary = "Obtener oficina por ID", description = "Devuelve los datos de una oficina específica según su UUID")
    @GetMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> getOffice(
        @Parameter(description = "UUID de la oficina") @PathVariable UUID id) {
        return ResponseEntity.ok(officeService.getOfficeById(id));
    }

    @Operation(summary = "Crear una nueva oficina", description = "Crea una oficina con los datos proporcionados")
    @PostMapping
    public ResponseEntity<OfficeResponseDto> createOffice(
        @Parameter(description = "Datos de la oficina a crear") @Valid @NotNull @RequestBody OfficeRequestDto officeDto) {
        return ResponseEntity.ok(officeService.createOffice(officeDto));
    }

    @Operation(summary = "Actualizar oficina existente", description = "Actualiza los datos de una oficina según su UUID")
    @PutMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> updateOffice(
        @Parameter(description = "UUID de la oficina a actualizar") @PathVariable UUID id,
        @Parameter(description = "Datos actualizados de la oficina") @Valid @NotNull @RequestBody OfficeRequestDto officeDto) {
        return ResponseEntity.ok(officeService.updateOffice(id, officeDto));
    }

    @Operation(summary = "Desactivar oficina", description = "Realiza una eliminación lógica de la oficina por su UUID")
    @DeleteMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> deactivateOffice(
        @Parameter(description = "UUID de la oficina a desactivar") @PathVariable UUID id) {
        return ResponseEntity.ok(officeService.deactivateOfficeById(id));
    }
}

