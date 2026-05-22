package com.paul.shitment.shipment_service.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleRequestDto;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleResponseDto;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleUpdateRequestDto;
import com.paul.shitment.shipment_service.mappers.VehicleMapper;
import com.paul.shitment.shipment_service.models.entities.TransportCooperative;
import com.paul.shitment.shipment_service.models.entities.Vehicle;
import com.paul.shitment.shipment_service.repositories.VehicleRepository;
import com.paul.shitment.shipment_service.services.VehicleService;
import com.paul.shitment.shipment_service.validators.TransportCooperativeValidator;
import com.paul.shitment.shipment_service.validators.VehicleValidator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final VehicleValidator vehicleValidator;
    private final TransportCooperativeValidator cooperativeValidator;

    @Override
    public List<VehicleResponseDto> getAllVehicles() {
        log.info("Obteniendo todos los vehículos");
        return vehicleMapper.entitiesToDto(vehicleRepository.findAll());
    }

    @Override
    public PageResponse<VehicleResponseDto> getAllVehiclesPaged(int pageNo, int size, String sortBy) {
        log.info("Obteniendo vehículos paginados: pageNo={}, size={}, sortBy={}", pageNo, size, sortBy);
        
        Sort sort = Sort.by(sortBy);
        PageRequest pageRequest = PageRequest.of(pageNo, size, sort);
        Page<Vehicle> vehiclePage = vehicleRepository.findAll(pageRequest);

        List<VehicleResponseDto> dtoList = new ArrayList<>();
        for (Vehicle vehicle : vehiclePage.getContent()) {
            dtoList.add(vehicleMapper.entityToDto(vehicle));
        }

        return new PageResponse<>(
                dtoList,
                vehiclePage.getNumber(),
                vehiclePage.getSize(),
                vehiclePage.getTotalElements(),
                vehiclePage.getTotalPages(),
                vehiclePage.isFirst(),
                vehiclePage.isLast(),
                vehiclePage.hasNext(),
                vehiclePage.hasPrevious());
    }

    @Override
    public VehicleResponseDto getVehicleById(UUID id) {
        log.info("Obteniendo vehículo con id: {}", id);
        Vehicle vehicle = vehicleValidator.getVehicleByIdOrThrow(id);
        return vehicleMapper.entityToDto(vehicle);
    }

    @Override
    @Transactional
    public VehicleResponseDto createVehicle(VehicleRequestDto vehicleDto) {
        log.info("Creando nuevo vehículo: {}", vehicleDto.internalCode());
        vehicleValidator.validateForCreate(vehicleDto);

        TransportCooperative cooperative = cooperativeValidator.getCooperativeByIdOrThrow(vehicleDto.cooperativeId());

        Vehicle vehicle = vehicleMapper.dtoToEntity(vehicleDto, cooperative);

        vehicleRepository.save(Objects.requireNonNull(vehicle));

        log.info("Vehículo creado exitosamente con id: {}", vehicle.getId());
        return vehicleMapper.entityToDto(vehicle);
    }

    @Override
    @Transactional
    public VehicleResponseDto updateVehicle(@NonNull UUID id, VehicleUpdateRequestDto vehicleDto) {
        log.info("Actualizando vehículo con id: {}", id);
        vehicleValidator.validateForUpdate(id, vehicleDto);

        Vehicle vehicle = vehicleValidator.getVehicleByIdOrThrow(id);
        TransportCooperative cooperative = cooperativeValidator.getCooperativeByIdOrThrow(vehicleDto.cooperativeId());

        vehicle.setInternalCode(vehicleDto.internalCode().trim());
        vehicle.setCooperative(cooperative);

        vehicleRepository.save(vehicle);

        log.info("Vehículo actualizado exitosamente");
        return vehicleMapper.entityToDto(vehicle);
    }

    @Override
    @Transactional
    public VehicleResponseDto deleteVehicle(@NonNull UUID id) {
        log.info("Eliminando vehículo con id: {}", id);
        Vehicle vehicle = vehicleValidator.getVehicleByIdOrThrow(id);

        vehicleRepository.delete(Objects.requireNonNull(vehicle));

        log.info("Vehículo eliminado exitosamente");
        return vehicleMapper.entityToDto(vehicle);
    }

    @Override
    public List<VehicleResponseDto> getVehiclesByCooperative(@NonNull UUID cooperativeId) {
        log.info("Obteniendo vehículos de la cooperativa: {}", cooperativeId);
        cooperativeValidator.getCooperativeByIdOrThrow(cooperativeId);
        
        List<Vehicle> vehicles = vehicleRepository.findByCooperativeId(cooperativeId);
        return vehicleMapper.entitiesToDto(vehicles);
    }
}
