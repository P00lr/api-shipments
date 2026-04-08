package com.paul.shitment.shipment_service.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.permission.PermissionRequestDto;
import com.paul.shitment.shipment_service.dto.permission.PermissionResponseDto;

public interface PermissionService{
    PageResponse<PermissionResponseDto> getAllPermissionPaged(@NonNull Pageable pageable);
    PermissionResponseDto getPermissionById(@NonNull UUID id);
    PermissionResponseDto createPermission(PermissionRequestDto permissionDto);
    PermissionResponseDto updatePermission(@NonNull UUID id, PermissionRequestDto permissionDto);
    void deactivatePermissionById(@NonNull UUID id);
}
