package com.paul.shitment.shipment_service.mappers;

import java.util.List;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.permission.PermissionRequestDto;
import com.paul.shitment.shipment_service.dto.permission.PermissionResponseDto;
import com.paul.shitment.shipment_service.models.entities.Permission;

@Component
public class PermissionMapper {

    public PermissionResponseDto toPermissionResponseDto(Permission permission) {
        return new PermissionResponseDto(
            permission.getName()
        );
    }

    public List<PermissionResponseDto> toPermissions(Set<Permission> permissions) {
        return permissions.stream()
            .map(this::toPermissionResponseDto)
            .toList();
    }

    @NonNull
    public Permission toPermission(PermissionRequestDto permissionDto) {
        Permission permission = new Permission();
        permission.setName(permissionDto.name());
        return permission;
    }

    
}
