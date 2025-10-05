package com.paul.shitment.shipment_service.dto.person;

import java.util.UUID;

public record PersonResponseDto(
    UUID id,
    String name, 
    String ci, 
    String phone,
    boolean registered,
    boolean active) {

        
}
