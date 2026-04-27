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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles", description = "Endpoints para la gestión de roles del sistema")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "Obtener roles paginados", description = "Retorna un listado paginado de todos los roles del sistema")
    @ApiResponse(responseCode = "200", description = "Listado de roles obtenido exitosamente")
    public ResponseEntity<PageResponse<RoleResponseDto>> getAllRolesPaged(@NonNull Pageable pageable) {
        return ResponseEntity.ok(roleService.getAllRolePaged(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por ID", description = "Retorna los datos de un rol específico por su UUID")
    @ApiResponse(responseCode = "200", description = "Rol encontrado")
    @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    public ResponseEntity<RoleResponseDto> getRoleById(@NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    } 

    @PostMapping
    @Operation(summary = "Crear nuevo rol", description = "Crea un nuevo rol en el sistema")
    @ApiResponse(responseCode = "201", description = "Rol creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<RoleResponseDto> createRole(@Valid @RequestBody RoleRequestDto roleDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(roleDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol", description = "Actualiza los datos de un rol existente")
    @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    public ResponseEntity<RoleResponseDto> updateRole(
        @NonNull @PathVariable UUID id,
        @Valid  @RequestBody RoleRequestDto roleDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.updateRole(id, roleDto));

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar rol", description = "Realiza una eliminación lógica (soft delete) de un rol")
    @ApiResponse(responseCode = "200", description = "Rol desactivado exitosamente")
    @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    public ResponseEntity<String> deactivateRoleById(@NonNull UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body("Se desactivo correctamente el rol: " +  id);
    }
}
