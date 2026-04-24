package com.paul.shitment.shipment_service.dto.transportCooperative;

import java.util.List;
import java.util.UUID;

import com.paul.shitment.shipment_service.dto.office.OfficeResponseDto;

public record CooperativeOfficeResponse(
    UUID cooperativeId,
    String cooperativeName,

    List<OfficeResponseDto> officeResponseDtos
    
) {

}
