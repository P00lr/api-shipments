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
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/offices")
@AllArgsConstructor
public class OfficeController {

    private final OfficeService officeService;

    @GetMapping
    public ResponseEntity<List<OfficeResponseDto>> getAllOffices() {
        return ResponseEntity.ok(officeService.getAllOffices());
    }

    @GetMapping("/paged")
    public ResponseEntity<PageResponse<OfficeResponseDto>> getAllOfficesPaged(
        @RequestParam(defaultValue = "0") int pageNo, 
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "username") String sortBy) {
        return ResponseEntity.ok(officeService.getAllOfficesPaged(pageNo, size, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> getOffice(@PathVariable UUID id) {
        return ResponseEntity.ok(officeService.getOffice(id));
    }

    @PostMapping
    public ResponseEntity<OfficeResponseDto> createOffice(@Valid @RequestBody OfficeRequestDto officeDto) {
        return ResponseEntity.ok(officeService.createOffice(officeDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> updateOffice(@PathVariable UUID id,@Valid @RequestBody OfficeRequestDto officeDto) {
        return ResponseEntity.ok(officeService.updateOffice(id, officeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> deactivateOffice(@PathVariable UUID id) {
        return ResponseEntity.ok(officeService.deactivateOffice(id));
    }

}
