package com.paul.shitment.shipment_service.dto.office;

import java.util.UUID;

public record OfficeResponse(
    UUID officeId,
    String officeName
) {

}
