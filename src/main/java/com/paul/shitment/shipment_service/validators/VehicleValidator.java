package com.paul.shitment.shipment_service.validators;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.vehicle.VehicleRequestDto;
import com.paul.shitment.shipment_service.dto.vehicle.VehicleUpdateRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.exceptions.validation.VehicleValidationException;
import com.paul.shitment.shipment_service.models.entities.Vehicle;
import com.paul.shitment.shipment_service.repositories.VehicleRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
public class VehicleValidator {

    private final VehicleRepository vehicleRepository;

    public Vehicle getVehicleByIdOrThrow(@NonNull UUID id) {
        log.debug("Validando existencia de vehículo con id: {}", id);
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el vehículo con id: " + id));
    }

    public void validateVehicleExists(@NonNull UUID id) {
        if (!vehicleRepository.existsById(id))
            throw new ResourceNotFoundException("No existe el vehículo con id: " + id);
    }

    public void validateForCreate(VehicleRequestDto vehicleDto) {
        log.debug("Validando vehículo para creación: {}", vehicleDto.internalCode());
        validateInternalCodeUnique(vehicleDto.internalCode());
    }

    public void validateForUpdate(@NonNull UUID id, VehicleUpdateRequestDto vehicleDto) {
        log.debug("Validando vehículo para actualización: {}", id);
        Vehicle vehicle = getVehicleByIdOrThrow(id);
        
        if (!vehicle.getInternalCode().equals(vehicleDto.internalCode()) 
            && vehicleRepository.existsByInternalCode(vehicleDto.internalCode())) {
            throw new VehicleValidationException("El código interno '" + vehicleDto.internalCode() + "' ya está registrado");
        }
    }

    private void validateInternalCodeUnique(String internalCode) {
        if (vehicleRepository.existsByInternalCode(internalCode.trim()))
            throw new VehicleValidationException("El código interno '" + internalCode + "' ya está registrado");
    }
}
