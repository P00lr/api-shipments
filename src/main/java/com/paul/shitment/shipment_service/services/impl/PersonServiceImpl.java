package com.paul.shitment.shipment_service.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private final ServiceHelper serviceHelper;

    @Override
    public List<PersonResponseDto> getAllPersons() {
        log.info("Verficando existencia de personas");
        return personMapper.entitiesToDtos(personRepository.findAll());
    }

    @Override
    public PageResponse<PersonResponseDto> getAllPersonsPaged(int pageNo, int size, String sortBy) {
        log.info("Verficando existencia de personas");

        Sort sort = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(pageNo, size, sort);

        Page<Person> personPage = personRepository.findAll(pageRequest);

        List<PersonResponseDto> listPerson = new ArrayList<>();

        for (Person person : personPage.getContent()) {
            listPerson.add(personMapper.entityToDto(person));
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
    public PersonResponseDto getPersonById(UUID id) {
        log.info("Verificando existencia de: {}", id);
        Person person = personValidator.getPersonByIdOrThrow(id);
        return personMapper.entityToDto(person);
    }

    @Override
    public PersonResponseDto getPersonByCI(String ci) {
        log.info("Verificando si existe la persona por ci: {}", ci);
        return personMapper.entityToDto(personValidator.getPersonByCiOrThrow(ci));
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
        person.setActive(true);

        personRepository.save(person);
        log.info("Registro guardado correctamente");

        return personMapper.entityToDto(person);
    }

    @Override
    public PersonResponseDto updatePerson(UUID id, PersonRequestDto personDto) {

        log.info("Validando al nuevo registro {}", personDto);
        personValidator.validateForUpdate(personDto, id);

        Person person = personValidator.getPersonByIdOrThrow(id);

        if (!person.getName().equals(personDto.name()))
            person.setName(personDto.name());

        if (!person.getCi().equals(personDto.ci()))
            person.setCi(personDto.ci());

        if (!personDto.phone().equals(person.getPhone()))
            person.setPhone(personDto.phone());

        personRepository.save(person);
        log.info("Persona actualizado correctemente");

        return personMapper.entityToDto(person);

    }

    @Override
    public PersonResponseDto deletePerson(UUID id) {
        log.info("Verificando si existe el registro: {}", id);
        Person person = personValidator.getPersonByIdOrThrow(id);

        serviceHelper.updateStatusIfChanged(
                person,
                Person::isActive, // getter
                e -> e.setActive(false) // setter
        );
        personRepository.save(person);
        log.info("registro desactivado correctamente: {}", person);

        return personMapper.entityToDto(person);
    }

}
