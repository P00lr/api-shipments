package com.paul.shitment.shipment_service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;

public interface ShipmentService {

    PageResponse<ShipmentResponseDto> getAllShipmentsPaged(@NonNull Pageable pageable);
    ShipmentResponseDto getShipment(@NonNull UUID id);
    ShipmentResponseDto createShipment(ShipmentRequestDto shipmenttDto);
    ShipmentResponseDto updateShipment(@NonNull UUID id, ShipmentUpdateRequestDto shipmentDto);
    ShipmentResponseDto canceledShipment(@NonNull UUID id);
    List<ShipmentSuggestionDTO> getSuggestions(String term);
    ShipmentResponseDto markAsDelivered(@NonNull UUID id, String inputCI);
    
}
