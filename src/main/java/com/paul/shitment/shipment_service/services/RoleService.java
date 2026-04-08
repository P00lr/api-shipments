package com.paul.shitment.shipment_service.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.role.RoleRequestDto;
import com.paul.shitment.shipment_service.dto.role.RoleResponseDto;

public interface RoleService{
    PageResponse<RoleResponseDto> getAllRolePaged(@NonNull Pageable pageable);
    RoleResponseDto getRoleById(@NonNull UUID id);
    RoleResponseDto createRole(RoleRequestDto roleDto);
    RoleResponseDto updateRole(@NonNull UUID id, RoleRequestDto roleDto);
    void deactivateRoleById(@NonNull UUID id);
}
