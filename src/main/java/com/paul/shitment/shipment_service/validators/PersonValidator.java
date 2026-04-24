package com.paul.shitment.shipment_service.validators;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.PersonValidationException;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.exceptions.validation.ShipmentValidationException;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.repositories.PersonRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class PersonValidator {

    private final PersonRepository personRepository;

    public Person getPersonByIdOrThrow(@NonNull UUID id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el registro con id: " + id));
    }

    public Person getPersonByCiOrThrow(String ci) {
        return personRepository.findByDocumentNumber(ci)
                .orElseThrow(() -> new PersonValidationException("No se encontra el registro con CI: " + ci));
    }

    public Person getPersonByPhoneOrThrow(String phone) {
        return personRepository.findByPhone(phone)
                .orElseThrow(() -> new PersonValidationException("No se encontro el registro con phone: " + phone));
    }

    public void validateForCreate(PersonRequestDto personDto) {

        documentNumberUnique(personDto.documentNumber());
        phoneUnique(personDto.phone());
    }

    public void validateForUpdate(PersonRequestDto personDto, @NonNull UUID id) {
        Person person = getPersonByIdOrThrow(id);

        if (!person.getDocumentNumber().equals(personDto.documentNumber()) 
                && personRepository.existsByDocumentNumber(personDto.documentNumber()))
            throw new PersonValidationException("El numero de documento ya esta registrado");

        if (!personDto.phone().equals(person.getPhone())
                && personRepository.existsByPhone(personDto.phone())) {
            throw new PersonValidationException("El celular ya fue registrado");
        }
    }

    public void validateDocumentNumberMatch(String storedCi, String inputCi) {
        if (inputCi == null || !storedCi.equals(inputCi)) {
            throw new ShipmentValidationException("numero de documento incorrecto para confirmar la entrega.");
        }
    }

    // METODOS AUXILIARES
    public void documentNumberUnique(String documentNumber) {

        if (documentNumber == null)
            throw new PersonValidationException("El numero de documento es obligatorio");

        if (personRepository.existsByDocumentNumber(documentNumber)) {
            log.warn("Intento de registrar numero de documento duplicado: {}", documentNumber);
            throw new PersonValidationException("El numero de documento ya esta registrado");
        }
    }

    public void phoneUnique(String phone) {

        if (phone == null)
            throw new PersonValidationException("El telefono es obligatorio");

        if (phone != null && personRepository.existsByPhone(phone)) {
            log.warn("Intento de registrar phone duplicado: {}", phone);
            throw new PersonValidationException("El telefono ya fue registrado");
        }
    }

}
