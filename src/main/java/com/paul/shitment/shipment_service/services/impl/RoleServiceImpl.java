package com.paul.shitment.shipment_service.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.role.RoleRequestDto;
import com.paul.shitment.shipment_service.dto.role.RoleResponseDto;
import com.paul.shitment.shipment_service.mappers.PaginationMapper;
import com.paul.shitment.shipment_service.mappers.RoleMappper;
import com.paul.shitment.shipment_service.models.entities.Role;
import com.paul.shitment.shipment_service.repositories.RoleRepository;
import com.paul.shitment.shipment_service.services.RoleService;
import com.paul.shitment.shipment_service.validators.RoleValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    
    private final RoleValidator roleValidator;
    private final RoleMappper roleMappper;
    private final PaginationMapper paginationMapper;
    private final RoleRepository roleRepository;

    @Override
    public PageResponse<RoleResponseDto> getAllRolePaged(@NonNull Pageable pageable) {
        log.info("Verificando lista de roles");
        Page<Role> rolesPaged = roleRepository.findAll(pageable);
        return paginationMapper.toPageResponseDto(rolesPaged, roleMappper::toRoleResponseDto);
    }

    @Override
    public RoleResponseDto getRoleById(@NonNull UUID id) {
        log.info("Verificando si existe el rol con ID: {}", id);
        Role role = roleValidator.getRoleByIdOrThrow(id);
        return roleMappper.toRoleResponseDto(role);
    }

    @Transactional
    @Override
    public RoleResponseDto createRole(RoleRequestDto roleDto) {
        log.info("Validando datos para crear el rol");
        roleValidator.validateForCreate(roleDto);
        Role role = roleMappper.toRole(roleDto);
        roleRepository.save(role);

        return roleMappper.toRoleResponseDto(role);
    }

    @Transactional
    @Override
    public RoleResponseDto updateRole(@NonNull UUID id, RoleRequestDto roleDto) {
        log.info("Validando datos para actualizar el rol");
        Role role = roleValidator.validateForUpdate(id, roleDto);
        role.updateFromRoleRequestDto(roleDto);
        roleRepository.save(role);

        return roleMappper.toRoleResponseDto(role);
    }

    @Override
    @Transactional
    public void deactivateRoleById(@NonNull UUID id) {
        Role role = roleValidator.getRoleByIdOrThrow(id);
        role.deactivate();
        roleRepository.save(role);
    }

}
