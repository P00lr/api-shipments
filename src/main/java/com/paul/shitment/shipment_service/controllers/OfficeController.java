package com.paul.shitment.shipment_service.controllers;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
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
import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.dto.office.OfficeResponseDto;
import com.paul.shitment.shipment_service.services.OfficeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/offices")
@AllArgsConstructor
@Tag(name = "Offices", description = "Endpoints para la gestión de oficinas")
public class OfficeController {

    private final OfficeService officeService;

    
    @Operation(summary = "Obtener oficinas paginadas", description = "Retorna un listado paginado de todas las oficinas registradas")
    @ApiResponse(responseCode = "200", description = "Listado de oficinas obtenido exitosamente")
    @GetMapping("/paged")
    public ResponseEntity<PageResponse<OfficeResponseDto>> getAllOffices(@NonNull Pageable pageable) {
        return ResponseEntity.ok(officeService.getAllOfficesPaged(pageable));
    }


    @Operation(summary = "Obtener oficina por ID", description = "Retorna los datos completos de una oficina específica por su UUID")
    @ApiResponse(responseCode = "200", description = "Oficina encontrada")
    @ApiResponse(responseCode = "404", description = "Oficina no encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> getOffice(
        @Parameter(description = "UUID de la oficina") @NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(officeService.getOfficeById(id));
    }

    @Operation(summary = "Crear nueva oficina", description = "Crea una nueva oficina con los datos proporcionados")
    @ApiResponse(responseCode = "200", description = "Oficina creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PostMapping
    public ResponseEntity<OfficeResponseDto> createOffice(
        @Parameter(description = "Datos de la oficina a crear") @Valid @NotNull @RequestBody OfficeRequestDto officeDto) {
        return ResponseEntity.ok(officeService.createOffice(officeDto));
    }

    @Operation(summary = "Actualizar oficina", description = "Actualiza los datos de una oficina existente")
    @ApiResponse(responseCode = "200", description = "Oficina actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Oficina no encontrada")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PutMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> updateOffice(
        @Parameter(description = "UUID de la oficina a actualizar") @NonNull @PathVariable UUID id,
        @Parameter(description = "Datos actualizados de la oficina") @Valid @NotNull @RequestBody OfficeRequestDto officeDto) {
        return ResponseEntity.ok(officeService.updateOffice(id, officeDto));
    }

    @Operation(summary = "Desactivar oficina", description = "Realiza una eliminación lógica (soft delete) de una oficina")
    @ApiResponse(responseCode = "200", description = "Oficina desactivada exitosamente")
    @ApiResponse(responseCode = "404", description = "Oficina no encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> deactivateOffice(
        @Parameter(description = "UUID de la oficina a desactivar")@NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(officeService.deactivateOfficeById(id));
    }
}

