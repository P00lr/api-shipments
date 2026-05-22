package com.paul.shitment.shipment_service.validators;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.exceptions.validation.ShipmentValidationException;
import com.paul.shitment.shipment_service.models.entities.Shipment;
import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;
import com.paul.shitment.shipment_service.repositories.ShipmentRepository;

import jakarta.validation.ValidationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class ShipmentValidator {
    private final ShipmentRepository shipmentRepository;

    private final OfficeValidator officeValidation;


    public Shipment getShipmentbyIdOrThrow(@NonNull UUID id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el envio con id: " + id));
    }

    public Shipment getShipmentbyTrackingCodeOrThrow(String trackingCode) {
        return shipmentRepository.findByTrackingCode(trackingCode)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el envio con codigo de rastreo: " + trackingCode));
    }

    public void validateForCreate(ShipmentRequestDto shipmentDto) {

        officeValidation.validateOfficeExists(shipmentDto.destinationOfficeId());
    }

    public Shipment validateForUpdate(@NonNull UUID id, ShipmentUpdateRequestDto shipmentDto) {

        Shipment shipment = getShipmentbyIdOrThrow(id);

        if (shipment.getStatus() != ShipmentStatus.REGISTERED)
            throw new ShipmentValidationException("No es posible editar, el envio ya fue entregado");

        return shipment;

    }

    public String validateTerm(String term) {
        if (term == null || term.trim().isEmpty())
            throw new ShipmentValidationException("Ingrese un palabra para buscar, ej: Nro CI, telefono, codigo de envio");

        return term.trim();
    }

    public void validateDelivery(Shipment shipment, String documentNumber) {
        if(shipment.getParties().stream().noneMatch(p -> p.getDocumentNumber().equals(documentNumber)))
            throw new ValidationException("El numero de documento no coincide");
    }

}
