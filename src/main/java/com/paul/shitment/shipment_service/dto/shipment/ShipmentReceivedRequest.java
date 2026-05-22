package com.paul.shitment.shipment_service.dto.shipment;

import java.util.Set;
import java.util.UUID;

public record ShipmentReceivedRequest(
    Set<UUID> shipmentsId
) {

}
