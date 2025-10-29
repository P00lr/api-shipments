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

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @GetMapping
    public ResponseEntity<List<ShipmentResponseDto>> getAllShipments() {
        return ResponseEntity.ok(shipmentService.getAllShipments());
    }

    @GetMapping("/paged")
    public ResponseEntity<PageResponse<ShipmentResponseDto>> getAllShipmentsPaged(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize) {

        // No pasamos sortBy porque el servicio maneja el orden personalizado
        // internamente
        PageResponse<ShipmentResponseDto> response = shipmentService.getAllShipmentsPaged(pageNo, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> getShipment(@PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.getShipment(id));
    }

    @PostMapping
    public ResponseEntity<ShipmentResponseDto> createShipment(@Valid @RequestBody ShipmentRequestDto shipmentDto) {
        return ResponseEntity.ok(shipmentService.createShipment(shipmentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> updateShipment(@PathVariable UUID id,
            @Valid @RequestBody ShipmentUpdateRequestDto shipmentDto) {
        return ResponseEntity.ok(shipmentService.updateShipment(id, shipmentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ShipmentResponseDto> canceledShipment(@PathVariable UUID id) {
        return ResponseEntity.ok(shipmentService.canceledShipment(id));
    }

    @GetMapping("/suggestions")
    public List<ShipmentSuggestionDTO> getSuggestions(
            @RequestParam(name = "term", defaultValue = "") String term) {
        return shipmentService.getSuggestions(term);
    }

    @PatchMapping("/{id}/deliver")
    public ResponseEntity<ShipmentResponseDto> markAsDelivered(
            @PathVariable UUID id,
            @RequestBody(required = false) ShipmentDeliveryRequest request) {

        String ci = request != null ? request.ci() : null;

        return ResponseEntity.ok(shipmentService.markAsDelivered(id, ci));
    }

}
