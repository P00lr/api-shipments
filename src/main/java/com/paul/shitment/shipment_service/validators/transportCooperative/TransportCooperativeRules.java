package com.paul.shitment.shipment_service.validators.transportCooperative;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;
import com.paul.shitment.shipment_service.exceptions.validation.TransportCooperativeException;
import com.paul.shitment.shipment_service.repositories.TransportCooperativeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransportCooperativeRules {
    private final TransportCooperativeRepository repository;

    public void validateUniqueName(TransportCooperativeRequest request) {
        if (repository.existsByNameIgnoreCase(request.name())) {
            throw new TransportCooperativeException("Ya existe una cooperativa registrada con el nombre: " + request.name());
        }
    }

    
}
