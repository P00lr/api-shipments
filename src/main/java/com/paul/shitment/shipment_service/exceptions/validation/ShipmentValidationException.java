package com.paul.shitment.shipment_service.exceptions.validation;

public class ShipmentValidationException extends RuntimeException {
    public ShipmentValidationException(String message) {
        super(message);
    }
}
