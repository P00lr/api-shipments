package com.paul.shitment.shipment_service.dto.office;

import java.util.UUID;

import com.paul.shitment.shipment_service.models.entities.Office;

public record OfficeResponseDto(
    UUID id, 
    String name, 
    String address, 
    String phone,
    boolean active) {
        public static OfficeResponseDto fromEntity(Office office) {
            return new OfficeResponseDto(
                office.getId(), 
                office.getName(), 
                office.getAddress(), 
                office.getPhone(), 
                office.isActive());
        }
}
