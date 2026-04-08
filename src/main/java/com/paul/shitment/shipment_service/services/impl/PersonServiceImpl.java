package com.paul.shitment.shipment_service.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.person.PersonResponseDto;
import com.paul.shitment.shipment_service.mappers.PaginationMapper;
import com.paul.shitment.shipment_service.mappers.PersonMapper;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.repositories.PersonRepository;
import com.paul.shitment.shipment_service.services.PersonService;
import com.paul.shitment.shipment_service.validators.PersonValidator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonValidator personValidator;
    private final PersonMapper personMapper;
    private final PaginationMapper paginationMapper;

    @Override
    public PageResponse<PersonResponseDto> getAllPersonsPaged(@NonNull Pageable pageable) {
        log.info("Verficando existencia de personas");

        Page<Person> personsPaged = personRepository.findAll(pageable);

        return paginationMapper.toPageResponseDto(personsPaged, personMapper::toDto);

    }

    @Override
    public PersonResponseDto getPersonById(@NonNull UUID id) {
        log.info("Verificando existencia de: {}", id);
        Person person = personValidator.getPersonByIdOrThrow(id);
        return personMapper.toDto(person);
    }

    @Override
    public PersonResponseDto getPersonByCI(String ci) {
        log.info("Verificando si existe la persona por ci: {}", ci);
        return personMapper.toDto(personValidator.getPersonByCiOrThrow(ci));
    }

    @Override
    public boolean existsByPhone(String phone) {
        log.info("Verificando si existe la persona por phone: {}", phone);
        return personRepository.existsByPhone(phone);
    }

    @Override
    public PersonResponseDto createPerson(PersonRequestDto personDto) {
        log.info("Validando al nuevo registro {}", personDto);
        personValidator.validateForCreate(personDto);

        Person person = personMapper.dtoToEntity(personDto);

        personRepository.save(person);
        log.info("Registro guardado correctamente");

        return personMapper.toDto(person);
    }

    @Override
    public PersonResponseDto updatePerson(@NonNull UUID id, PersonRequestDto personDto) {

        log.info("Validando al nuevo registro {}", personDto);
        personValidator.validateForUpdate(personDto, id);

        Person person = personValidator.getPersonByIdOrThrow(id);

        person.updateFromRequestDto(personDto);

        personRepository.save(person);
        log.info("Persona actualizado correctemente");

        return personMapper.toDto(person);

    }

    @Override
    public PersonResponseDto deletePerson(@NonNull UUID id) {
        log.info("Verificando si existe el registro: {}", id);
        Person person = personValidator.getPersonByIdOrThrow(id);

        person.deactivate();
        
        personRepository.save(person);
        log.info("registro desactivado correctamente: {}", person);

        return personMapper.toDto(person);
    }

}
