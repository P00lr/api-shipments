package com.paul.shitment.shipment_service.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.dto.office.OfficeResponseDto;

public interface OfficeService {

    PageResponse<OfficeResponseDto> getAllOfficesPaged(@NonNull Pageable pageable);

    OfficeResponseDto getOfficeById(@NonNull UUID id);

    OfficeResponseDto createOffice(OfficeRequestDto dto);

    OfficeResponseDto updateOffice(@NonNull UUID id, OfficeRequestDto dto);

    OfficeResponseDto deactivateOfficeById(@NonNull UUID id);
}
