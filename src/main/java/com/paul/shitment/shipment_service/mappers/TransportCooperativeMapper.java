package com.paul.shitment.shipment_service.mappers;

import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;
import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeResponse;
import com.paul.shitment.shipment_service.models.entities.TransportCooperative;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class TransportCooperativeMapper {
    
    public TransportCooperativeResponse entityToDto(TransportCooperative entity) {
        return new TransportCooperativeResponse(
            entity.getId(),
            entity.getName(),
            entity.isActive()
        );
    }

    public TransportCooperative requestToEntity(TransportCooperativeRequest request) {
        return new TransportCooperative(
            request.name(),
            request.isActive()
        );
    }

    public List<TransportCooperativeResponse> entitiesToDtos(List<TransportCooperative> cooperatives) {
        return cooperatives.stream()
            .map(this::entityToDto)
            .toList();
    }
}
