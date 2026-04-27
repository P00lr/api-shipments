package com.paul.shitment.shipment_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentDeliveryRequest;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;
import com.paul.shitment.shipment_service.services.ShipmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/shipments")
@Tag(name = "Shipments", description = "Endpoints para la gestión de envíos")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Operation(summary = "Obtener envíos paginados", description = "Retorna un listado paginado de todos los envíos registrados")
    @ApiResponse(responseCode = "200", description = "Listado de envíos obtenido exitosamente")
    @GetMapping
    public ResponseEntity<PageResponse<ShipmentResponseDto>> getAllShipmentsPaged(@NonNull Pageable pageable) {

        PageResponse<ShipmentResponseDto> response = shipmentService.getAllShipmentsPaged(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener envío por ID", description = "Retorna los datos completos de un envío específico por su UUID")
    @ApiResponse(responseCode = "200", description = "Envío encontrado")
    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> getShipment(
            @Parameter(description = "UUID del envío") @NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.getShipment(id));
    }

    @Operation(summary = "Crear nuevo envío", description = "Crea un nuevo envío con los datos del remitente, destinatario e item")
    @ApiResponse(responseCode = "200", description = "Envío creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PostMapping
    public ResponseEntity<ShipmentResponseDto> createShipment(
            @Parameter(description = "Datos del envío a crear") @Valid @RequestBody ShipmentRequestDto shipmentDto) {
        return ResponseEntity.ok(shipmentService.createShipment(shipmentDto));
    }

    /* @Operation(summary = "Actualizar un envío existente")
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> updateShipment(
            @Parameter(description = "UUID del envío a actualizar") @NonNull @PathVariable UUID id,
            @Parameter(description = "Datos actualizados del envío") @Valid @RequestBody ShipmentUpdateRequestDto shipmentDto) {
        return ResponseEntity.ok(shipmentService.updateShipment(id, shipmentDto));
    } */

    @Operation(summary = "Cancelar envío", description = "Realiza una eliminación lógica (soft delete) de un envío")
    @ApiResponse(responseCode = "200", description = "Envío cancelado exitosamente")
    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> canceledShipment(
            @Parameter(description = "UUID del envío a cancelar")@NonNull  @PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.canceledShipment(id));
    }

    @Operation(summary = "Obtener sugerencias de envíos", description = "Retorna sugerencias de envíos basadas en un término de búsqueda con paginación")
    @ApiResponse(responseCode = "200", description = "Sugerencias obtenidas exitosamente")
    @GetMapping("/suggestions")
    public ResponseEntity<PageResponse<ShipmentSuggestionDTO>> getSuggestions(
            @Parameter(description = "Término de búsqueda") @RequestParam(name = "term", defaultValue = "") String term,
            @Parameter(description = "Configuración de paginación") Pageable pageable) {
        PageResponse<ShipmentSuggestionDTO> response = shipmentService.getSuggestions(term, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Marcar envío como entregado", description = "Actualiza el estado del envío a ENTREGADO y registra la información del destinatario")
    @ApiResponse(responseCode = "200", description = "Envío marcado como entregado exitosamente")
    @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    @ApiResponse(responseCode = "400", description = "Datos de entrega inválidos")
    @PatchMapping("/{shipmentUUID}/deliver")
    public ResponseEntity<ShipmentResponseDto> markAsDelivered(
            @Parameter(description = "UUID del envío") @NonNull @PathVariable UUID shipmentUUID,
            @Parameter(description = "Información de entrega con número de documento del destinatario") @RequestBody ShipmentDeliveryRequest request) {

        return ResponseEntity.ok(shipmentService.markAsDelivered(shipmentUUID, request));
    }
}

