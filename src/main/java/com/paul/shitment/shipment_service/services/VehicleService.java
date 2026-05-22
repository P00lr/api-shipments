package com.paul.shitment.shipment_service.services;

import java.util.List;
import java.util.UUID;


import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleRequestDto;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleResponseDto;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleUpdateRequestDto;

import lombok.NonNull;

public interface VehicleService {

    List<VehicleResponseDto> getAllVehicles();

    PageResponse<VehicleResponseDto> getAllVehiclesPaged(
            int page,
            int size,
            String sortBy);

    VehicleResponseDto getVehicleById(@NonNull UUID id);

    VehicleResponseDto createVehicle(VehicleRequestDto vehicleDto);

    VehicleResponseDto updateVehicle(@NonNull UUID id, VehicleUpdateRequestDto vehicleDto);

    VehicleResponseDto deleteVehicle(@NonNull UUID id);

    List<VehicleResponseDto> getVehiclesByCooperative(@NonNull UUID cooperativeId);
}
