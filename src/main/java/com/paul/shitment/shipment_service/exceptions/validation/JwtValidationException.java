package com.paul.shitment.shipment_service.exceptions.validation;

public class JwtValidationException extends RuntimeException{
    public JwtValidationException(String message) {
        super(message);
    }

    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
