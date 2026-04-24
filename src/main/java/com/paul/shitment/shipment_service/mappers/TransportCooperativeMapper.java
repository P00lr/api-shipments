package com.paul.shitment.shipment_service.mappers;

import com.paul.shitment.shipment_service.dto.transportCooperative.CooperativeOfficeResponse;
import com.paul.shitment.shipment_service.dto.transportCooperative.CooperativeWhithOfficesResponse;
import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;
import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeResponse;
import com.paul.shitment.shipment_service.models.entities.TransportCooperative;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransportCooperativeMapper {

    private final OfficeMapper officeMapper;

    public TransportCooperativeResponse entityToDto(TransportCooperative entity) {
        return new TransportCooperativeResponse(
                entity.getId(),
                entity.getName(),
                entity.isEnabled());
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

    public CooperativeOfficeResponse getCooperativeOfficeResponse(TransportCooperative cooperative) {
        return new CooperativeOfficeResponse(
                cooperative.getId(),
                cooperative.getName(),
                cooperative.getOffices().stream().map(officeMapper::toDto).toList());

    }

    public CooperativeWhithOfficesResponse tWhithOfficesResponse(TransportCooperative cooperative) {
        return new CooperativeWhithOfficesResponse(
                cooperative.getId(),
                cooperative.getName(),
                cooperative.getOffices().stream().map(officeMapper::officeForCooperative).toList());
    }
}
