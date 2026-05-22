package com.paul.shitment.shipment_service.controllers;

import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleRequestDto;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleResponseDto;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleUpdateRequestDto;
import com.paul.shitment.shipment_service.services.VehicleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/vehicles")
@Tag(name = "Vehicles", description = "Endpoints para la gestión de vehículos")
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "Obtener todos los vehículos", description = "Retorna una lista completa de todos los vehículos sin paginación")
    @ApiResponse(responseCode = "200", description = "Listado de vehículos obtenido exitosamente")
    @GetMapping
    public ResponseEntity<List<VehicleResponseDto>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @Operation(summary = "Obtener vehículos paginados", description = "Retorna un listado paginado de vehículos con opciones de ordenamiento")
    @ApiResponse(responseCode = "200", description = "Listado paginado de vehículos obtenido exitosamente")
    @GetMapping("/paged")
    public ResponseEntity<PageResponse<VehicleResponseDto>> getAllVehiclesPaged(
            @RequestParam(defaultValue = "0")
            @Parameter(description = "Número de página (inicia en 0)")
            int pageNo,

            @RequestParam(defaultValue = "10")
            @Parameter(description = "Cantidad de registros por página")
            int size,

            @RequestParam(defaultValue = "id")
            @Parameter(description = "Campo por el cual ordenar")
            String sortBy) {

        // Validaciones básicas (defensivo)
        if (pageNo < 0) pageNo = 0;
        if (size <= 0) size = 10;
        if (size > 50) size = 50;

        return ResponseEntity.ok(vehicleService.getAllVehiclesPaged(pageNo, size, sortBy));
    }

    @Operation(summary = "Obtener vehículo por ID", description = "Retorna los datos completos de un vehículo específico por su UUID")
    @ApiResponse(responseCode = "200", description = "Vehículo encontrado")
    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> getVehicle(
            @Parameter(description = "UUID del vehículo") @NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @Operation(summary = "Obtener vehículos por cooperativa", description = "Retorna todos los vehículos pertenecientes a una cooperativa específica")
    @ApiResponse(responseCode = "200", description = "Vehículos encontrados")
    @ApiResponse(responseCode = "404", description = "Cooperativa no encontrada")
    @GetMapping("/cooperative/{cooperativeId}")
    public ResponseEntity<List<VehicleResponseDto>> getVehiclesByCooperative(
            @Parameter(description = "UUID de la cooperativa") @NonNull @PathVariable UUID cooperativeId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByCooperative(cooperativeId));
    }

    @Operation(summary = "Crear nuevo vehículo", description = "Crea un nuevo vehículo con los datos proporcionados")
    @ApiResponse(responseCode = "201", description = "Vehículo creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Cooperativa no encontrada")
    @PostMapping
    public ResponseEntity<VehicleResponseDto> createVehicle(
            @Parameter(description = "Datos del vehículo a crear") @Valid @RequestBody VehicleRequestDto vehicleDto) {
        VehicleResponseDto created = vehicleService.createVehicle(vehicleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Actualizar vehículo", description = "Actualiza los datos de un vehículo existente")
    @ApiResponse(responseCode = "200", description = "Vehículo actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Vehículo o cooperativa no encontrada")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> updateVehicle(
            @Parameter(description = "UUID del vehículo a actualizar") @NonNull @PathVariable UUID id,
            @Parameter(description = "Datos actualizados del vehículo") @Valid @RequestBody VehicleUpdateRequestDto vehicleDto) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDto));
    }

    @Operation(summary = "Eliminar vehículo", description = "Elimina un vehículo del sistema")
    @ApiResponse(responseCode = "200", description = "Vehículo eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> deleteVehicle(
            @Parameter(description = "UUID del vehículo a eliminar") @NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(vehicleService.deleteVehicle(id));
    }
}
