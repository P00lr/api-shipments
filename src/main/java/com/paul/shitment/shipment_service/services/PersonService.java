package com.paul.shitment.shipment_service.services;

import java.util.List;
import java.util.UUID;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.person.PersonResponseDto;

public interface PersonService {
    List<PersonResponseDto> getAllPersons();
    PageResponse<PersonResponseDto> getAllPersonsPaged(int pageNo, int size, String sortBy);
    PersonResponseDto getPersonById(UUID id);
    PersonResponseDto getPersonByCI(String ci);
    boolean existsByPhone(String phone);
    PersonResponseDto createPerson(PersonRequestDto person);
    PersonResponseDto updatePerson(UUID id, PersonRequestDto person);
    PersonResponseDto deletePerson(UUID id);
}
