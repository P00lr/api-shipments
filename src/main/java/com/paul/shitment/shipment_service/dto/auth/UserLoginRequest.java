package com.paul.shitment.shipment_service.dto.auth;

import java.util.Set;

public record UserLoginRequest(
    String username,
    Set<String> roles
) {

}
