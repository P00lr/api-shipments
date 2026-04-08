package com.paul.shitment.shipment_service.controllers;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.role.RoleRequestDto;
import com.paul.shitment.shipment_service.dto.role.RoleResponseDto;
import com.paul.shitment.shipment_service.services.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<PageResponse<RoleResponseDto>> getAllRolesPaged(@NonNull Pageable pageable) {
        return ResponseEntity.ok(roleService.getAllRolePaged(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDto> getRoleById(@NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    } 

    @PostMapping
    public ResponseEntity<RoleResponseDto> createRole(@Valid @RequestBody RoleRequestDto roleDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(roleDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDto> updateRole(
        @NonNull @PathVariable UUID id,
        @Valid  @RequestBody RoleRequestDto roleDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.updateRole(id, roleDto));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateRoleById(@NonNull UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body("Se desactivo correctamente el rol: " +  id);
    }
}
