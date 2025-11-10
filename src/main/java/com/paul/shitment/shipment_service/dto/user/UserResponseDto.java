package com.paul.shitment.shipment_service.dto.user;

import java.util.UUID;

public record UserResponseDto(
    UUID id,
    String name,
    String phone,
    String ci,
    String username,
    String email,
    boolean active
) {}
