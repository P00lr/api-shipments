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
import org.springframework.web.bind.annotation.RestController;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.user.UserPasswordUpdateDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserResponseDto;
import com.paul.shitment.shipment_service.dto.user.UserUpdateRequestDto;
import com.paul.shitment.shipment_service.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Endpoints para la gestión de usuarios")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Obtener usuarios paginados")
    @GetMapping("/paged")
    public ResponseEntity<PageResponse<UserResponseDto>> getAllUsersPaged(
            @Parameter(description = "Número de página (inicia en 0)") int pageNo,
            @Parameter(description = "Cantidad de registros por página") int size,
            @Parameter(description = "Campo por el cual ordenar") String sortBy) {
        return ResponseEntity.ok(userService.getAllUsersPaged(pageNo, size, sortBy));
    }

    @Operation(summary = "Obtener usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(
            @Parameter(description = "UUID del usuario") @PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserByid(id));
    }

    @Operation(summary = "Crear nuevo usuario")
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Parameter(description = "Datos del usuario a crear") @Valid @RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @Operation(summary = "Actualizar datos del usuario")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(description = "UUID del usuario a actualizar") @PathVariable UUID id,
            @Parameter(description = "Datos actualizados del usuario") @Valid @RequestBody UserUpdateRequestDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @Operation(summary = "Actualizar contraseña del usuario")
    @PutMapping("/password/{id}")
    public ResponseEntity<UserResponseDto> updatePassword(
            @Parameter(description = "UUID del usuario") @PathVariable UUID id,
            @Parameter(description = "Nueva contraseña") @Valid @RequestBody UserPasswordUpdateDto passwordDto) {
        return ResponseEntity.ok(userService.updateUserPassword(id, passwordDto));
    }

    @Operation(summary = "Desactivar usuario (eliminación lógica)")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deactivate(
            @Parameter(description = "UUID del usuario a desactivar") @PathVariable UUID id) {
        return ResponseEntity.ok(userService.dactivateUser(id));
    }
}

