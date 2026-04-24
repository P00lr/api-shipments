package com.paul.shitment.shipment_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.person.PersonResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserUpdateRequestDto;
import com.paul.shitment.shipment_service.models.entities.Person;

@Component
public class PersonMapper {

    public PersonResponseDto toDto(Person person) {
        return new PersonResponseDto(
                person.getId(),
                person.getDocumentType().name(), // saco el nombre
                person.getDocumentNumber(),
                person.getFullName(),
                person.getPhone(),
                person.isRegistered(),
                person.isActive());
    }

    public List<PersonResponseDto> entitiesToDtos(List<Person> persons) {
        return persons.stream()
                .map((person -> toDto(person)))
                .collect(Collectors.toList());
    }

    @NonNull
    public Person dtoToEntity(PersonRequestDto personDto) {
        Person person = new Person();
        person.setDocumentType(personDto.documentType());
        person.setDocumentNumber(personDto.documentNumber());
        person.setFullName(personDto.fullName());
        person.setPhone(personDto.phone());

        return person;
    }

    public Person userDtoToEntityPerson(UserRequestDto userDto) {
        Person person = new Person();
        person.setDocumentType(userDto.person().documentType());
        person.setDocumentNumber(userDto.person().documentNumber());
        person.setFullName(userDto.person().fullName());
        person.setPhone(userDto.person().phone());

        return person;
    }

    public PersonRequestDto userDtoToEntityPerson(UserUpdateRequestDto userDto) {
        return new PersonRequestDto(
                userDto.person().documentType(),
                userDto.person().documentNumber().trim(),
                userDto.person().fullName().trim(),
                userDto.person().phone().trim());
    }

    public PersonRequestDto shipmentDtoToPersonSenderDto(ShipmentRequestDto shipmentDto) {
        return new PersonRequestDto(
                shipmentDto.sender().documentType(),
                shipmentDto.sender().documentNumber().trim(),
                shipmentDto.sender().fullName().trim(),
                shipmentDto.sender().phone());
    }

    public PersonRequestDto shipmentDtoToPersonRecipientDto(ShipmentRequestDto shipmentDto) {
        return new PersonRequestDto(
                shipmentDto.recipient().documentType(),
                shipmentDto.recipient().documentNumber().trim(),
                shipmentDto.recipient().fullName().trim(),
                shipmentDto.recipient().phone());
    }

    public Person shipmentDtoToPersonRecipient(ShipmentRequestDto shipmentDto) {
        Person person = new Person();
        person.setDocumentType(shipmentDto.recipient().documentType());
        person.setDocumentNumber(shipmentDto.recipient().documentNumber());
        person.setFullName(shipmentDto.recipient().fullName());
        person.setPhone(shipmentDto.recipient().phone());

        return person;
    }
}
