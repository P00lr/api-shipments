package com.paul.shitment.shipment_service.services;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.person.PersonResponseDto;

public interface PersonService {
    PageResponse<PersonResponseDto> getAllPersonsPaged(@NonNull Pageable pageable);
    PersonResponseDto getPersonById(@NonNull UUID id);
    PersonResponseDto getPersonByCI(String ci);
    boolean existsByPhone(String phone);
    PersonResponseDto createPerson(PersonRequestDto person);
    PersonResponseDto updatePerson(@NonNull UUID id, PersonRequestDto person);
    PersonResponseDto deletePerson(@NonNull UUID id);
}
