package com.paul.shitment.shipment_service.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.person.PersonResponseDto;
import com.paul.shitment.shipment_service.mappers.PersonMapper;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.repositories.PersonRepository;
import com.paul.shitment.shipment_service.services.PersonService;
import com.paul.shitment.shipment_service.validators.person.PersonValidator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonValidator personValidator;

    @Override
    public List<PersonResponseDto> getAllPersons() {
        log.info("Verficando existencia de personas");
        List<Person> persons = personRepository.findAll();

        if (persons.isEmpty()) return List.of();

        log.info("Se encontro Lista de personas {}", persons.size());

        return PersonMapper.entitiesToDtos(persons);

    }

    @Override
    public PageResponse<PersonResponseDto> getAllPersonsPaged(int pageNo, int size, String sortBy) {
        log.info("Verficando existencia de personas");
        personValidator.validateExistsPersons();

        Sort sort = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(pageNo, size, sort);

        Page<Person> personPage = personRepository.findAll(pageRequest);

        List<PersonResponseDto> listPerson = new ArrayList<>();

        for (Person person : personPage.getContent()) {
            listPerson.add(PersonMapper.entityToDto(person));
        }

        return new PageResponse<>(
            listPerson, 
            personPage.getNumber(), 
            personPage.getSize(), 
            personPage.getTotalElements(), 
            personPage.getTotalPages(), 
            personPage.isFirst(), 
            personPage.isLast());

    }

    @Override
    public PersonResponseDto getPerson(UUID id) {
        log.info("Verificando existencia de: {}", id);
        Person person = personValidator.validateExistsPerson(id);

        return PersonMapper.entityToDto(person);
    }

    @Override
    public PersonResponseDto getPersonByCI(String ci) {
        log.info("Verificando si existe la persona por ci: {}", ci);
        return PersonMapper.entityToDto(personValidator.validateExistsPersonByCi(ci));
    }

    @Override
    public boolean existsByPhone(String phone) {
        log.info("Verificando si existe la persona por phone: {}", phone);
        return personRepository.existsByPhone(phone);
    }

    @Override
    public PersonResponseDto createPerson(PersonRequestDto personDto) {
        log.info("Validando al nuevo registro {}", personDto);
        personValidator.validatePerson(personDto);

        Person person = PersonMapper.dtoToEntity(personDto);
        person.setActive(true);


        try {
            personRepository.save(person);
            log.info("Registro guardado correctamente");
        } catch (DataAccessException e) {
            log.info("Error al registra {}", personDto);
            throw e;
        }

        return PersonMapper.entityToDto(person);
    }

    @Override
    public PersonResponseDto updatePerson(UUID id, PersonRequestDto personDto) {
        log.info("Validando al nuevo registro {}", personDto);

        Person person = personValidator.validateUpdate(personDto, id);

        if (!Objects.equals(person.getName(), personDto.name()))
            person.setName(personDto.name());

        if (!person.getCi().equals(personDto.ci()))
            person.setCi(personDto.ci());

        if (!Objects.equals(personDto.phone(), person.getPhone()))
            person.setPhone(personDto.phone());


        try {
            personRepository.save(person);
            log.info("Persona actualizado correctemente");
        } catch (DataAccessException e) {
            log.error("No se puedo guardar el registro para actualizar {}", personDto);
            throw e;
        }

        return PersonMapper.entityToDto(person);

    }

    @Override
    public PersonResponseDto deletePerson(UUID id) {
        log.info("Verificando si existe el registro: {}", id);
        Person person = personValidator.validateExistsPerson(id);

        person.setActive(false);

        try {
            personRepository.save(person);
            log.info("registro desactivado correctamente: {}", person);
        } catch (DataAccessException e) {
            throw e;
        }

        return PersonMapper.entityToDto(person);
    }

    

}
