package com.paul.shitment.shipment_service.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.vehicle.VehicleRequestDto;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleResponseDto;
import com.paul.shitment.shipment_service.models.entities.TransportCooperative;
import com.paul.shitment.shipment_service.models.entities.Vehicle;

@Component
public class VehicleMapper {

    public VehicleResponseDto entityToDto(Vehicle vehicle) {
        return new VehicleResponseDto(
            vehicle.getId(),
            vehicle.getInternalCode(),
            vehicle.getCooperative().getName()
        );
    }

    public List<VehicleResponseDto> entitiesToDto(List<Vehicle> vehicles) {
        List<VehicleResponseDto> vehicleDtos = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            vehicleDtos.add(entityToDto(vehicle));
        }
        return vehicleDtos;
    }

    public Vehicle dtoToEntity(VehicleRequestDto vehicleDto, TransportCooperative cooperative) {
        Vehicle vehicle = new Vehicle();
        vehicle.setInternalCode(vehicleDto.internalCode().trim());
        vehicle.setCooperative(cooperative);
        return vehicle;
    }
}
