package com.paul.shitment.shipment_service.services;

import java.util.List;
import java.util.UUID;


import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.dto.office.OfficeResponseDto;

public interface OfficeService {

    List<OfficeResponseDto> getAllOffices();
    PageResponse<OfficeResponseDto> getAllOfficesPaged(int pageNo, int size, String sortBy);
    OfficeResponseDto getOffice(UUID id);
    OfficeResponseDto createOffice(OfficeRequestDto officeDto);
    OfficeResponseDto updateOffice(UUID id, OfficeRequestDto officeDto);
    OfficeResponseDto deactivateOffice(UUID id);
}
