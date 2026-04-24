package com.paul.shitment.shipment_service.dto.transportCooperative;

import java.util.List;
import java.util.UUID;

import com.paul.shitment.shipment_service.dto.office.OfficeResponse;

public record CooperativeWhithOfficesResponse(
    UUID cooperativeId,
    String cooperativeName,

    List<OfficeResponse> offices

) {

}
