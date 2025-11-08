package com.paul.shitment.shipment_service.validators.transportCooperative;

import java.util.UUID;

import org.springframework.stereotype.Component;
import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.models.entities.TransportCooperative;
import com.paul.shitment.shipment_service.repositories.TransportCooperativeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransportCooperativeValidator implements BaseTransportCooperativeValidator {

    private final TransportCooperativeRules cooperativeRules;
    private final TransportCooperativeRepository cooperativeRepository;

    @Override
    public void validate(TransportCooperativeRequest request) {
        cooperativeRules.validateUniqueName(request);
    }

    @Override
    public TransportCooperative getFindById(UUID id) {
        return cooperativeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("No existe una cooperativa con el ID '%s'.", id)));
    }

}
