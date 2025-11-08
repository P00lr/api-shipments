package com.paul.shitment.shipment_service.validators;

import java.util.UUID;

import org.springframework.stereotype.Component;
import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.exceptions.validation.TransportCooperativeException;
import com.paul.shitment.shipment_service.models.entities.TransportCooperative;
import com.paul.shitment.shipment_service.repositories.TransportCooperativeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransportCooperativeValidator {

    private final TransportCooperativeRepository cooperativeRepository;
    
    public TransportCooperative getCooperativeByIdOrThrow(UUID id) {
        return cooperativeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("No existe una cooperativa con el ID '%s'.", id)));
    }
    
    public void validateForCreate(TransportCooperativeRequest request) {
        validateUniqueName(request);
    }

    public void validateForUpdate(UUID id, TransportCooperativeRequest request) {
        TransportCooperative existing = getCooperativeByIdOrThrow(id);
        validateNameUniquenessOnUpdate(existing.getName(), request.name());
    }



    //METODOS AUXILIARES
    private void validateUniqueName(TransportCooperativeRequest request) {
        if (cooperativeRepository.existsByNameIgnoreCase(request.name())) {
            throw new TransportCooperativeException("Ya existe una cooperativa registrada con el nombre: " + request.name());
        }
    }

    private void validateNameUniquenessOnUpdate(String nameCurrent, String nameNew) {
        if (!nameCurrent.equalsIgnoreCase(nameNew) && cooperativeRepository.existsByNameIgnoreCase(nameNew)) {
            throw new TransportCooperativeException("Ya existe una cooperativa registrada con el nombre: " + nameNew);
        }
    }
}
