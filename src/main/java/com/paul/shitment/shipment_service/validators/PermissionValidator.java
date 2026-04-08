package com.paul.shitment.shipment_service.validators;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.permission.PermissionRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceAlreadyExistsException;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.models.entities.Permission;
import com.paul.shitment.shipment_service.repositories.PermissionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PermissionValidator {

    private final PermissionRepository permissionRepository;

    public Permission getPermissionByIdOrThrow(@NonNull UUID id) {
        return permissionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontro el permission con ID: " + id));
    }

    public void validateForCreate(PermissionRequestDto permissionDto) {
        validateName(permissionDto.name());
    }

    public Permission validateForUpdate(@NonNull UUID id, PermissionRequestDto permissionDto) {
        Permission permission = getPermissionByIdOrThrow(id);
        validateNameForUpdate(permission.getName(), permissionDto.name());
        return permission;
    }

    private void validateName(String name) {
        if(permissionRepository.findByName(name))
            throw new ResourceAlreadyExistsException("Ya existe el permission: " + name );
    }

    private void validateNameForUpdate(String currentName, String newName) {
        if(!currentName.equals(newName) &&
            permissionRepository.findByName(newName))
        throw new ResourceAlreadyExistsException("Ya existe el permission: " + newName);
    }
}
