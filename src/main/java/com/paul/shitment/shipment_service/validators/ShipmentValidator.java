package com.paul.shitment.shipment_service.validators;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.PersonValidationException;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.exceptions.validation.ShipmentValidationException;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.models.entities.Shipment;
import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;
import com.paul.shitment.shipment_service.repositories.PersonRepository;
import com.paul.shitment.shipment_service.repositories.ShipmentRepository;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class ShipmentValidator {
    private final ShipmentRepository shipmentRepository;
    private final PersonRepository personRepository;

    private final OfficeValidator officeValidation;
    private final UserValidator userValidator;

    public void existsShipments() {
        if (shipmentRepository.count() == 0) {
            throw new ResourceNotFoundException("No se encontro ningun registro");
        }
    }

    public Shipment existsShipment(UUID id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el envio con id: " + id));
    }

    public void validateShipment(ShipmentRequestDto shipmentDto) {

        officeValidation.getOfficeByIdOrThrow(shipmentDto.originOfficeId());
        officeValidation.getOfficeByIdOrThrow(shipmentDto.destinationOfficeId());

        userValidator.getUserByIdOrTrhow(shipmentDto.userId());

        validationPrice(shipmentDto.shippingCost());
        validationDescription(shipmentDto.itemDescription());

    }

    // el objeto que ingrese contendra si o si todos los datos del
    // ShipmentUpdateRequestDto
    public Shipment validateUpdateShipemnt(UUID id, ShipmentUpdateRequestDto shipmentDto) {
        
        Shipment shipment = existsShipment(id);

        if (shipment.getStatus() != ShipmentStatus.REGISTERED)
            throw new ShipmentValidationException("No es posible editar, el envio ya fue entregado");

        Person sender = shipment.getSender();
        Person recipient = shipment.getRecipient();

        if (!Objects.equals(sender.getCi(), shipmentDto.senderCI()) 
            && personRepository.existsByCi(shipmentDto.senderCI()))
            throw new PersonValidationException("El CI ya esta registrado");

        if (!Objects.equals(sender.getPhone(), shipmentDto.senderPhone() )
                && personRepository.existsByPhone(shipmentDto.senderPhone())) {
            throw new PersonValidationException("El celular ya fue registrado");
        }

        if (!Objects.equals(recipient.getCi(), shipmentDto.recipientCI()) 
            && personRepository.existsByCi(shipmentDto.recipientCI()))
            throw new PersonValidationException("El CI ya esta registrado");

        if (!Objects.equals(recipient.getPhone(), shipmentDto.recipientPhone())
                && personRepository.existsByPhone(shipmentDto.recipientPhone())) {
            throw new PersonValidationException("El celular ya fue registrado");
        }


        validationPrice(shipmentDto.shippingCost());
        validationDescription(shipmentDto.itemDescription());

        return shipment;

    }
    public String validateTerm(String term) {
        if (term == null || term.trim().isEmpty())
            throw new ValidationException("Ingrese un palabra para buscar, ej: Nro CI, telefono, codigo de envio");

        return term.trim();
    }

    //metodos auxiliares

    private void validationPrice(Double price) {
        if (price <= 0)
            throw new ShipmentValidationException("El precio no debe ser menor a 0");
    }

    private void validationDescription(String description) {
        if (description.isBlank())
            throw new ShipmentValidationException("La descripcion no debe estar vacia");
    }
}
