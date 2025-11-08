package com.paul.shitment.shipment_service.services;

import java.util.List;
import java.util.UUID;

import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;
import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeResponse;

public interface TransportCooperativeService {

    List<TransportCooperativeResponse> getAllCooperatives();
    TransportCooperativeResponse getCooperativeById(UUID id);
    TransportCooperativeResponse createCooperative(TransportCooperativeRequest cooperative);
    TransportCooperativeResponse updateCooperative(UUID id, TransportCooperativeRequest cooperativeDetails);
    TransportCooperativeResponse deactivateCooperative(UUID id);
}
