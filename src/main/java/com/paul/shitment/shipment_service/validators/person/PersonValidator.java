package com.paul.shitment.shipment_service.validators.person;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.PersonValidationException;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.repositories.PersonRepository;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class PersonValidator {

    private final PersonRepository personRepository;

    public void validateExistsPersons() {
        if (personRepository.count() == 0) {
            throw new ResourceNotFoundException("No se encontraron registros");
        }
    }

    public Person validateExistsPerson(UUID id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el registro con id: " + id));
    }

    public Person validateExistsPersonByCi(String ci) {
        return personRepository.findByCi(ci)
            .orElseThrow(() -> new PersonValidationException("No se encontra el registro con CI: " + ci));
    }

    public void validatePerson(PersonRequestDto personDto) {

        personNotNull(personDto);
        ciUnique(personDto.ci());
        phoneUnique(personDto.phone());

    }

    public Person validateUpdate(PersonRequestDto personDto, UUID id) {
        Person person = validateExistsPerson(id);

        personNotNull(personDto);

        if (!Objects.equals(person.getCi(), personDto.ci()) && personRepository.existsByCi(personDto.ci()))
            throw new PersonValidationException("El CI ya esta registrado");

        if (!Objects.equals(personDto.phone(), person.getPhone())
                && personRepository.existsByPhone(personDto.phone())) {
            throw new PersonValidationException("El celular ya fue registrado");
        }

        return person;
    }

    //auxiliar
    public void ciUnique(String ci) {

        if (ci == null)
            throw new PersonValidationException("El CI es obligatorio");

        if (personRepository.existsByCi(ci)) {
            log.warn("Intento de registrar DNI duplicado: {}", ci);
            throw new PersonValidationException("El CI ya esta registrado");
        }
    }

    //auxiliar
    public void phoneUnique(String phone) {

        if (phone == null)
            throw new PersonValidationException("El celular es obligatorio");

        if (phone != null && personRepository.existsByPhone(phone)) {
            log.warn("Intento de registrar phone duplicado: {}", phone);
            throw new PersonValidationException("El número ya fue registrado");
        }
    }

    public Optional<Person> validateGetPersonByCi(String ci) {
        return personRepository.findByCi(ci);
    }

    private void personNotNull(PersonRequestDto person) {
        if (person == null) {
            throw new ValidationException("El objeto no puede estar vacio");
        }
    }
}
