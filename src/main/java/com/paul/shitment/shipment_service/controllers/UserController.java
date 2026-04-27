package com.paul.shitment.shipment_service.controllers;

import java.util.List;
import java.util.UUID;

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
import com.paul.shitment.shipment_service.dto.user.UserPasswordUpdateDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserResponseDto;
import com.paul.shitment.shipment_service.dto.user.UserUpdateRequestDto;
import com.paul.shitment.shipment_service.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Endpoints para la gestión de usuarios")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista completa de todos los usuarios sin paginación")
    @ApiResponse(responseCode = "200", description = "Listado de usuarios obtenido exitosamente")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Obtener usuarios paginados", description = "Retorna un listado paginado de usuarios con opciones de ordenamiento")
    @ApiResponse(responseCode = "200", description = "Listado paginado de usuarios obtenido exitosamente")
    @GetMapping("/paged")
    public ResponseEntity<PageResponse<UserResponseDto>> getAllUsersPaged(
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

        return ResponseEntity.ok(userService.getAllUsersPaged(pageNo, size, sortBy));
    }

    @Operation(summary = "Obtener usuario por ID", description = "Retorna los datos completos de un usuario específico por su UUID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(
            @Parameter(description = "UUID del usuario") @NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserByid(id));
    }

    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario con los datos proporcionados")
    @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Parameter(description = "Datos del usuario a crear") @Valid @RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @Operation(summary = "Actualizar datos del usuario", description = "Actualiza la información personal y de acceso de un usuario existente")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(description = "UUID del usuario a actualizar") @NonNull @PathVariable UUID id,
            @Parameter(description = "Datos actualizados del usuario") @Valid @RequestBody UserUpdateRequestDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @Operation(summary = "Actualizar contraseña", description = "Actualiza la contraseña de un usuario después de validar la contraseña actual")
    @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @ApiResponse(responseCode = "401", description = "Contraseña actual incorrecta")
    @PutMapping("/password/{id}")
    public ResponseEntity<UserResponseDto> updatePassword(
            @Parameter(description = "UUID del usuario")@NonNull  @PathVariable UUID id,
            @Parameter(description = "Nueva contraseña") @Valid @RequestBody UserPasswordUpdateDto passwordDto) {
        return ResponseEntity.ok(userService.updateUserPassword(id, passwordDto));
    }

    @Operation(summary = "Desactivar usuario", description = "Realiza una eliminación lógica (soft delete) de un usuario")
    @ApiResponse(responseCode = "200", description = "Usuario desactivado exitosamente")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDto> deactivate(
            @Parameter(description = "UUID del usuario a desactivar") @NonNull @PathVariable UUID id) {
        return ResponseEntity.ok(userService.dactivateUser(id));
    }
}

