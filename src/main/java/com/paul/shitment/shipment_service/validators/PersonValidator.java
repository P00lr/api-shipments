package com.paul.shitment.shipment_service.validators;

import java.util.UUID;

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

    public Person getPersonByIdOrThrow(UUID id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el registro con id: " + id));
    }

    public Person getPersonByCiOrThrow(String ci) {
        return personRepository.findByCi(ci)
                .orElseThrow(() -> new PersonValidationException("No se encontra el registro con CI: " + ci));
    }

    public Person getPersonByPhoneOrThrow(String phone) {
        return personRepository.findByPhone(phone)
                .orElseThrow(() -> new PersonValidationException("No se encontro el registro con phone: " + phone));
    }

    public void validateForCreate(PersonRequestDto personDto) {

        ciUnique(personDto.ci());
        phoneUnique(personDto.phone());

    }

    public void validateForUpdate(PersonRequestDto personDto, UUID id) {
        Person person = getPersonByIdOrThrow(id);

        if (!person.getCi().equals(personDto.ci()) 
                && personRepository.existsByCi(personDto.ci()))
            throw new PersonValidationException("El CI ya esta registrado");

        if (!personDto.phone().equals(person.getPhone())
                && personRepository.existsByPhone(personDto.phone())) {
            throw new PersonValidationException("El celular ya fue registrado");
        }
    }

    public void validateCiMatch(String storedCi, String inputCi) {
        if (inputCi == null || !storedCi.equals(inputCi)) {
            throw new ShipmentValidationException("CI incorrecto para confirmar la entrega.");
        }
    }

    // METODOS AUXILIARES
    public void ciUnique(String ci) {

        if (ci == null)
            throw new PersonValidationException("El CI es obligatorio");

        if (personRepository.existsByCi(ci)) {
            log.warn("Intento de registrar CI duplicado: {}", ci);
            throw new PersonValidationException("El CI ya esta registrado");
        }
    }

    public void phoneUnique(String phone) {

        if (phone == null)
            throw new PersonValidationException("El celular es obligatorio");

        if (phone != null && personRepository.existsByPhone(phone)) {
            log.warn("Intento de registrar phone duplicado: {}", phone);
            throw new PersonValidationException("El número ya fue registrado");
        }
    }

}
