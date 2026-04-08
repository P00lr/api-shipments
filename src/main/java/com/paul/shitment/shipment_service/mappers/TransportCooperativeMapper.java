package com.paul.shitment.shipment_service.mappers;

import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;
import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeResponse;
import com.paul.shitment.shipment_service.models.entities.TransportCooperative;

import java.util.List;

import org.springframework.lang.NonNull;
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

    @NonNull
    public TransportCooperative dtoToEntity(TransportCooperativeRequest dto) {
        TransportCooperative transportCooperative = new TransportCooperative();
        transportCooperative.setName(dto.name());
        return transportCooperative;
    }

    public List<TransportCooperativeResponse> entitiesToDtos(List<TransportCooperative> cooperatives) {
        return cooperatives.stream()
            .map(this::entityToDto)
            .toList();
    }
}
