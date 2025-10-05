package com.paul.shitment.shipment_service.exceptions.validation;

public class UserValidationException extends RuntimeException{
    public UserValidationException(String message) {
        super(message);
    }
}
