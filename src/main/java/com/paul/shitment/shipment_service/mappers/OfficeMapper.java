package com.paul.shitment.shipment_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.dto.office.OfficeResponseDto;
import com.paul.shitment.shipment_service.models.entities.Office;

@Component
public class OfficeMapper {

    public OfficeResponseDto entityToDto(Office office) {
        return new OfficeResponseDto(
                office.getId(),
                office.getName(),
                office.getAddress(),
                office.getPhone(),
                office.isActive());
    }

    public List<OfficeResponseDto> entitiesToDto(List<Office> offices) {
        return offices.stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    public Office dtoToEntity(OfficeRequestDto dto) {
        return new Office(dto.name(), dto.address(), dto.phone());
    }

    public void updateEntity(Office office, OfficeRequestDto dto) {
        office.setName(dto.name());
        office.setAddress(dto.address());
        office.setPhone(dto.phone());
    }
}
