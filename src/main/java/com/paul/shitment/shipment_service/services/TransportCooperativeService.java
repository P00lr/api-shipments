package com.paul.shitment.shipment_service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;

import com.paul.shitment.shipment_service.dto.transportCooperative.CooperativeOfficeRequest;
import com.paul.shitment.shipment_service.dto.transportCooperative.CooperativeOfficeResponse;
import com.paul.shitment.shipment_service.dto.transportCooperative.CooperativeWhithOfficesResponse;
import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;
import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeResponse;

public interface TransportCooperativeService {

    List<TransportCooperativeResponse> getAllCooperatives();

    CooperativeWhithOfficesResponse getCooperativeWhithOffices(UUID cooperativeId);
    
    TransportCooperativeResponse getCooperativeById(@NonNull UUID id);
    
    TransportCooperativeResponse createCooperative(TransportCooperativeRequest cooperative);
    
    TransportCooperativeResponse updateCooperative (@NonNull UUID id, TransportCooperativeRequest cooperativeDetails);
    
    TransportCooperativeResponse deactivateCooperative(@NonNull UUID id);

    CooperativeOfficeResponse assignOffices(CooperativeOfficeRequest  cooperativeOfficeRequest);
}
