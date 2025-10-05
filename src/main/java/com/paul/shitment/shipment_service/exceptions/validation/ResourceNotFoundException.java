package com.paul.shitment.shipment_service.exceptions.validation;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
