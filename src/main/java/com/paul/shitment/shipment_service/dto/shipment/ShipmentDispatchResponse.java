package com.paul.shitment.shipment_service.dto.shipment;

import java.util.Set;

public record ShipmentDispatchResponse(
    String internalCode,
    Set<String> trackingCode
) {

}
