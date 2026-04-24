package com.paul.shitment.shipment_service.dto.shipment;

public record ShipmentPersonDto(

    String documentType,
    String documentNumber,
    String fullName,
    String phone
) {

}
