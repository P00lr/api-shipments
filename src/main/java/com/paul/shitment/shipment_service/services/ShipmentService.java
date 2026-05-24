package com.paul.shitment.shipment_service.services;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentDeliveryRequest;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentDispatchRequest;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentDispatchResponse;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentReceivedRequest;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;

import lombok.NonNull;

public interface ShipmentService {

    PageResponse<ShipmentResponseDto> getAllShipmentsPaged(ShipmentStatus status, Pageable pageable);

    ShipmentResponseDto getShipment(@NonNull UUID id);

    ShipmentResponseDto getShipmentByTrackingCode(String trackingCode);
    
    PageResponse<ShipmentSuggestionDTO> getSuggestions(String term, @NonNull Pageable pageable);
    
    ShipmentResponseDto createShipment(ShipmentRequestDto shipmenttDto);
    
    ShipmentResponseDto canceledShipment(@NonNull UUID id);
    
    Set<ShipmentResponseDto> shipmentReceived(ShipmentReceivedRequest shipmentsId);
    
    ShipmentResponseDto markAsDelivered(@NonNull UUID shipmentUUID, ShipmentDeliveryRequest request);

    ShipmentResponseDto markWaitingPickuk(UUID shipmentId);

    ShipmentDispatchResponse dispatchShipments(UUID vehicleId, ShipmentDispatchRequest dispatches);

    ShipmentResponseDto cancelDispatch(UUID shipmentId);

    //quitar de aqui
    AppUser getUserOfContext();
    
}
