package com.paul.shitment.shipment_service.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.permission.PermissionRequestDto;
import com.paul.shitment.shipment_service.dto.permission.PermissionResponseDto;
import com.paul.shitment.shipment_service.mappers.PaginationMapper;
import com.paul.shitment.shipment_service.mappers.PermissionMapper;
import com.paul.shitment.shipment_service.models.entities.Permission;
import com.paul.shitment.shipment_service.repositories.PermissionRepository;
import com.paul.shitment.shipment_service.services.PermissionService;
import com.paul.shitment.shipment_service.validators.PermissionValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    
    private final PermissionValidator permissionValidator;
    private final PermissionMapper permissionMapper;
    private final PaginationMapper paginationMapper;
    private final PermissionRepository permissionRepository;

    @Override
    public PageResponse<PermissionResponseDto> getAllPermissionPaged(@NonNull Pageable pageable) {
        log.info("Verificando lista de permissions");
        Page<Permission> permissionsPaged = permissionRepository.findAll(pageable);
        return paginationMapper.toPageResponseDto(permissionsPaged, permissionMapper::toPermissionResponseDto);
    }

    @Override
    public PermissionResponseDto getPermissionById(@NonNull UUID id) {
        log.info("Verificando si existe el permission con ID: {}", id);
        Permission permission = permissionValidator.getPermissionByIdOrThrow(id);
        return permissionMapper.toPermissionResponseDto(permission);
    }

    @Transactional
    @Override
    public PermissionResponseDto createPermission(PermissionRequestDto permissionDto) {
        log.info("Validando datos para crear el permission");
        permissionValidator.validateForCreate(permissionDto);
        Permission permission = permissionMapper.toPermission(permissionDto);
        permissionRepository.save(permission);

        return permissionMapper.toPermissionResponseDto(permission);
    }

    @Transactional
    @Override
    public PermissionResponseDto updatePermission(@NonNull UUID id, PermissionRequestDto permissionDto) {
        log.info("Validando datos para actualizar el permission");
        Permission permission = permissionValidator.validateForUpdate(id, permissionDto);
        permission.updateFromPermissionRequestDto(permissionDto);
        permissionRepository.save(permission);

        return permissionMapper.toPermissionResponseDto(permission);
    }

    @Override
    @Transactional
    public void deactivatePermissionById(@NonNull UUID id) {
        Permission permission = permissionValidator.getPermissionByIdOrThrow(id);
        permission.deactivate();
        permissionRepository.save(permission);
    }


}
