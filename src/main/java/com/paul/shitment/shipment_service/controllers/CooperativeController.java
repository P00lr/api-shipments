package com.paul.shitment.shipment_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.paul.shitment.shipment_service.dto.transportCooperative.*;
import com.paul.shitment.shipment_service.services.TransportCooperativeService;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Obtener todas las cooperativas de transporte")
    @ApiResponse(responseCode = "200", description = "Lista de cooperativas de transporte recuperada con éxito")
    public ResponseEntity<List<TransportCooperativeResponse>> getAllCooperatives() {
        return ResponseEntity.ok(transportCooperativeService.getAllCooperatives());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cooperativa de transporte por ID")
    @ApiResponse(responseCode = "200", description = "Cooperativa de transporte encontrada")
    @ApiResponse(responseCode = "404", description = "Cooperativa de transporte no encontrada")
    public ResponseEntity<TransportCooperativeResponse> getCooperativeById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(transportCooperativeService.getCooperativeById(id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva cooperativa de transporte")
    @ApiResponse(responseCode = "201", description = "Cooperativa de transporte creada con éxito")
    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos")
    public ResponseEntity<TransportCooperativeResponse> createCooperative(
            @Valid @NotNull @RequestBody TransportCooperativeRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transportCooperativeService.createCooperative(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cooperativa de transporte")
    @ApiResponse(responseCode = "200", description = "Cooperativa de transporte actualizada con éxito")
    @ApiResponse(responseCode = "404", description = "Cooperativa de transporte no encontrada")
    public ResponseEntity<TransportCooperativeResponse> updateCooperative(
            @PathVariable UUID id,
            @Valid @RequestBody TransportCooperativeRequest cooperativeDetails) {
        return ResponseEntity.ok(transportCooperativeService.updateCooperative(id, cooperativeDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cooperativa de transporte")
    @ApiResponse(responseCode = "204", description = "Cooperativa de transporte eliminada con éxito")
    @ApiResponse(responseCode = "404", description = "Cooperativa de transporte no encontrada")
    public ResponseEntity<Void> deleteCooperative(@PathVariable UUID id) {
        transportCooperativeService.deactivateCooperative(id);
        return ResponseEntity.noContent().build();
    }
}
