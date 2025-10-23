package com.paul.shitment.shipment_service.services;

import java.util.List;
import java.util.UUID;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;

public interface ShipmentService {

    List<ShipmentResponseDto> getAllShipments();
    PageResponse<ShipmentResponseDto> getAllShipmentsPaged(int pageNo, int pageSize, String sortBy);
    ShipmentResponseDto getShipment(UUID id);
    ShipmentResponseDto createShipment(ShipmentRequestDto shipmenttDto);
    ShipmentResponseDto updateShipment(UUID id, ShipmentUpdateRequestDto shipmentDto);
    ShipmentResponseDto canceledShipment(UUID id);
    List<ShipmentSuggestionDTO> getSuggestions(String term);
    ShipmentResponseDto markAsDelivered(UUID id);
    
}
