package com.paul.shitment.shipment_service.dto.transportCooperative;

import java.util.UUID;

public record TransportCooperativeResponse(
    UUID id, 
    String name, 
    boolean isActive
) {

}
