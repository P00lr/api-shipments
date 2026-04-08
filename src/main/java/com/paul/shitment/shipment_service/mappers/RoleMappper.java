package com.paul.shitment.shipment_service.mappers;

import java.util.List;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.role.RoleRequestDto;
import com.paul.shitment.shipment_service.dto.role.RoleResponseDto;
import com.paul.shitment.shipment_service.models.entities.Role;

@Component
public class RoleMappper {

    public RoleResponseDto toRoleResponseDto(Role role) {
        return new RoleResponseDto(
            role.getName()
        );
    }

    public List<RoleResponseDto> toRoles(Set<Role> roles) {
        return roles.stream()
            .map(this::toRoleResponseDto)
            .toList();
    }

    @NonNull
    public Role toRole(RoleRequestDto roleDto) {
        Role role = new Role();
        role.setName("ROLE_" + roleDto.name().trim().toUpperCase());
        return role;
    }

    
}
