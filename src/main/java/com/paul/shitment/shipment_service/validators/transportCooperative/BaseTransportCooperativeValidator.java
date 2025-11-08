package com.paul.shitment.shipment_service.validators.transportCooperative;

import java.util.UUID;

import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;
import com.paul.shitment.shipment_service.models.entities.TransportCooperative;

public interface BaseTransportCooperativeValidator {
    void validate(TransportCooperativeRequest request);
    TransportCooperative getFindById(UUID id);
}
