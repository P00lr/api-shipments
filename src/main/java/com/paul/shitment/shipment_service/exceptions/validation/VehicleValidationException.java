package com.paul.shitment.shipment_service.exceptions.validation;

public class VehicleValidationException extends RuntimeException {
    public VehicleValidationException(String message) {
        super(message);
    }
}
