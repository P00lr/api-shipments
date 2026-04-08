package com.paul.shitment.shipment_service.validators;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.role.RoleRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceAlreadyExistsException;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.models.entities.Role;
import com.paul.shitment.shipment_service.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleValidator {

    private final RoleRepository roleRepository;

    private static final String PREFIX = "ROLE_"; 

    public Role getRoleByIdOrThrow(@NonNull UUID id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontro el role con ID: " + id));
    }

    public Role getRoleByNameOrThrow(String name) {
        return roleRepository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontro el role con nombre: " + name));
    }

    public void validateForCreate(RoleRequestDto roleDto) {
        validateName(roleDto.name());
    }

    public Role validateForUpdate(@NonNull UUID id, RoleRequestDto roleDto) {
        Role role = getRoleByIdOrThrow(id);
        validateNameForUpdate(role.getName(), roleDto.name());
        return role;
    }

    private void validateName(String name) {
        String nameNormalized = nameNormalized(name);
        
        if(roleRepository.existsByName(nameNormalized))
            throw new ResourceAlreadyExistsException("Ya existe el rol: " + nameNormalized );
    }

    private void validateNameForUpdate(String currentName, String newName) {
        String nameNormalized = nameNormalized(newName);

        if(!currentName.equals(nameNormalized) &&
            roleRepository.existsByName(nameNormalized))
        throw new ResourceAlreadyExistsException("Ya existe el rol: " + nameNormalized);
    }

    private String nameNormalized(String name) {
        return PREFIX + name.trim().toUpperCase();
    }
}
