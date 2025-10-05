package com.paul.shitment.shipment_service.mappers;

import java.util.ArrayList;
import java.util.List;

import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.dto.office.OfficeResponseDto;
import com.paul.shitment.shipment_service.models.entities.Office;

public class OfficeMapper {

    public static OfficeResponseDto entityToDto(Office office) {
        return new OfficeResponseDto(
                office.getId(),
                office.getName(),
                office.getAddress(),
                office.getPhone(),
                office.isActive());
    }

    public static List<OfficeResponseDto> entitiesToDto(List<Office> offices) {
        List<OfficeResponseDto> listOffices = new ArrayList<OfficeResponseDto>();

        for (Office office : offices) {
            OfficeResponseDto officeDto = new OfficeResponseDto(
                office.getId(),
                office.getName(),
                office.getAddress(),
                office.getPhone(),
                office.isActive()
            );

            listOffices.add(officeDto);
        }

        return listOffices;
    }

    public static Office dtoToEntity(OfficeRequestDto officeDto) {
        return new Office(
            officeDto.name(),
            officeDto.address(),
            officeDto.phone()
        );
    }
}
