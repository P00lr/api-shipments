package com.paul.shitment.shipment_service.security.service;

public interface LoginAttemptService {
    
    void loginFailed(String username);

    void loginSucceeded(String username);

    boolean isBlocked(String username);
}
