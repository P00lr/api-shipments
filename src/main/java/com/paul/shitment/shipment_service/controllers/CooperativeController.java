package com.paul.shitment.shipment_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.paul.shitment.shipment_service.dto.transportCooperative.*;
import com.paul.shitment.shipment_service.services.TransportCooperativeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/transportCooperatives")
@AllArgsConstructor
@Validated
@Tag(name = "Transport Cooperatives", description = "API para la gestión de cooperativas de transporte")
public class CooperativeController {

    private final TransportCooperativeService transportCooperativeService;

    @GetMapping
    @Operation(summary = "Obtener todas las cooperativas", description = "Retorna un listado completo de todas las cooperativas de transporte registradas")
    @ApiResponse(responseCode = "200", description = "Listado de cooperativas obtenido exitosamente")
    public ResponseEntity<List<TransportCooperativeResponse>> getAllCooperatives() {
        return ResponseEntity.ok(transportCooperativeService.getAllCooperatives());
    }

    @GetMapping("/{cooperativeUUID}/whith-offices")
    @Operation(summary = "Obtener cooperativa con sus oficinas", description = "Retorna una cooperativa específica junto con todas sus oficinas asignadas")
    @ApiResponse(responseCode = "200", description = "Cooperativa con oficinas obtenida exitosamente")
    @ApiResponse(responseCode = "404", description = "Cooperativa no encontrada")
    public ResponseEntity<CooperativeWhithOfficesResponse> getCooperativeWhithOffices(@PathVariable UUID cooperativeUUID) {
        return ResponseEntity.ok(transportCooperativeService.getCooperativeWhithOffices(cooperativeUUID));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cooperativa por ID", description = "Retorna los datos de una cooperativa específica por su UUID")
    @ApiResponse(responseCode = "200", description = "Cooperativa encontrada")
    @ApiResponse(responseCode = "404", description = "Cooperativa no encontrada")
    public ResponseEntity<TransportCooperativeResponse> getCooperativeById(
            @NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(transportCooperativeService.getCooperativeById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva cooperativa", description = "Crea una nueva cooperativa de transporte en el sistema")
    @ApiResponse(responseCode = "201", description = "Cooperativa creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos")
    public ResponseEntity<TransportCooperativeResponse> createCooperative(
            @Valid @NotNull @RequestBody TransportCooperativeRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transportCooperativeService.createCooperative(request));
    }

    @PostMapping("/assignOffices")
    @Operation(summary = "Asignar oficinas a cooperativa", description = "Asigna un conjunto de oficinas a una cooperativa específica")
    @ApiResponse(responseCode = "200", description = "Oficinas asignadas exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "404", description = "Cooperativa u oficina no encontrada")
    public ResponseEntity<CooperativeOfficeResponse> assignOffices(@Valid @RequestBody CooperativeOfficeRequest cooperativeOfficeRequest) {
        return ResponseEntity.ok(transportCooperativeService.assignOffices(cooperativeOfficeRequest));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cooperativa", description = "Actualiza los datos de una cooperativa existente")
    @ApiResponse(responseCode = "200", description = "Cooperativa actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Cooperativa no encontrada")
    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos")
    public ResponseEntity<TransportCooperativeResponse> updateCooperative(
            @NonNull @PathVariable UUID id,
            @Valid @RequestBody TransportCooperativeRequest cooperativeDetails) {
        return ResponseEntity.ok(transportCooperativeService.updateCooperative(id, cooperativeDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar cooperativa", description = "Realiza una eliminación lógica (soft delete) de una cooperativa")
    @ApiResponse(responseCode = "200", description = "Cooperativa desactivada exitosamente")
    @ApiResponse(responseCode = "404", description = "Cooperativa no encontrada")
    public ResponseEntity<TransportCooperativeResponse> deleteCooperative(@NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(transportCooperativeService.deactivateCooperative(id));
    }
}
