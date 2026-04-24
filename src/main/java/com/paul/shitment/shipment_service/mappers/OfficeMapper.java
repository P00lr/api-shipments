package com.paul.shitment.shipment_service.mappers;


import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.office.OfficeResponse;
import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.dto.office.OfficeResponseDto;
import com.paul.shitment.shipment_service.models.entities.Office;


@Component
public class OfficeMapper {

    public OfficeResponseDto toDto(Office office) {
        return new OfficeResponseDto(
                office.getId(),
                office.getName(),
                office.getAddress(),
                office.getPhone(),
                office.isActive());
    }

    @NonNull//siempre devolvera un Office y no algo null
    public Office toEntity(OfficeRequestDto dto) {
        Office office = new Office();
            office.setName(dto.name());
            office.setAddress(dto.address());
            office.setPhone(dto.phone()); 
        return office;
    }

    public OfficeResponse officeForCooperative(Office office) {
        return new OfficeResponse(
            office.getId(),
            office.getName()
        );
    }
}
