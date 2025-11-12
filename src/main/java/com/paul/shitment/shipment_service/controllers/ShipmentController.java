package com.paul.shitment.shipment_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/shipments")
@Tag(name = "Shipments", description = "Endpoints para la gestión de envíos")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Operation(summary = "Obtener todos los envíos")
    @GetMapping
    public ResponseEntity<List<ShipmentResponseDto>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @Operation(summary = "Obtener envíos paginados")
    @GetMapping("/paged")
    public ResponseEntity<PageResponse<ShipmentResponseDto>> getAllShipmentsPaged(
            @Parameter(description = "Número de página (inicia en 0)") @RequestParam(defaultValue = "0") int pageNo,
            @Parameter(description = "Cantidad de registros por página") @RequestParam(defaultValue = "5") int pageSize) {

        PageResponse<ShipmentResponseDto> response = shipmentService.getAllShipmentsPaged(pageNo, pageSize);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener un envío por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> getShipment(
            @Parameter(description = "UUID del envío") @PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.getShipment(id));
    }

    @Operation(summary = "Crear un nuevo envío")
    @PostMapping
    public ResponseEntity<ShipmentResponseDto> createShipment(
            @Parameter(description = "Datos del envío a crear") @Valid @RequestBody ShipmentRequestDto shipmentDto) {
        return ResponseEntity.ok(shipmentService.createShipment(shipmentDto));
    }

    @Operation(summary = "Actualizar un envío existente")
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> updateShipment(
            @Parameter(description = "UUID del envío a actualizar") @PathVariable UUID id,
            @Parameter(description = "Datos actualizados del envío") @Valid @RequestBody ShipmentUpdateRequestDto shipmentDto) {
        return ResponseEntity.ok(shipmentService.updateShipment(id, shipmentDto));
    }

    @Operation(summary = "Cancelar un envío (eliminación lógica)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> canceledShipment(
            @Parameter(description = "UUID del envío a cancelar") @PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.canceledShipment(id));
    }

    @Operation(summary = "Sugerencias de envíos por término")
    @GetMapping("/suggestions")
    public List<ShipmentSuggestionDTO> getSuggestions(
            @Parameter(description = "Término de búsqueda") @RequestParam(name = "term", defaultValue = "") String term) {
        return shipmentService.getSuggestions(term);
    }

    @Operation(summary = "Marcar un envío como entregado")
    @PatchMapping("/{id}/deliver")
    public ResponseEntity<ShipmentResponseDto> markAsDelivered(
            @Parameter(description = "UUID del envío") @PathVariable UUID id,
            @Parameter(description = "CI del destinatario (opcional)") @RequestBody(required = false) ShipmentDeliveryRequest request) {

        String ci = request != null ? request.ci() : null;
        return ResponseEntity.ok(shipmentService.markAsDelivered(id, ci));
    }
}

