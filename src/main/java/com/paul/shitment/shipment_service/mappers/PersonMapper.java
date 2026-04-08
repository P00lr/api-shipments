package com.paul.shitment.shipment_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.person.PersonResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserUpdateRequestDto;
import com.paul.shitment.shipment_service.models.entities.Person;

@Component
public class PersonMapper {

    public PersonResponseDto toDto(Person person) {
        return new PersonResponseDto(
                person.getId(),
                person.getName(),
                person.getCi(),
                person.getPhone() == null ? "Sin numero" : person.getPhone(),
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
        person.setName(personDto.name());
        person.setCi(personDto.ci());
        person.setPhone(personDto.phone());

        return person;
    }

    public Person userDtoToEntityPerson(UserRequestDto userDto) {
        Person person = new Person();
        person.setName(userDto.name());
        person.setCi(userDto.ci());
        person.setPhone(userDto.phone());

        return person;
    }

    public PersonRequestDto userDtoToEntityPerson(UserUpdateRequestDto userDto) {
        return new PersonRequestDto(
                userDto.name().trim(),
                userDto.ci().trim(),
                userDto.phone());
    }

    public PersonRequestDto shipmentDtoToPersonSenderDto(ShipmentRequestDto shipmentDto) {
        return new PersonRequestDto(
                shipmentDto.sender().name().trim(),
                shipmentDto.sender().ci().trim(),
                shipmentDto.sender().phone());
    }

    public PersonRequestDto shipmentDtoToPersonRecipientDto(ShipmentRequestDto shipmentDto) {
        return new PersonRequestDto(
                shipmentDto.recipient().name().trim(),
                shipmentDto.recipient().ci().trim(),
                shipmentDto.recipient().phone());
    }

    public Person shipmentDtoToPerson(ShipmentRequestDto shipmentDto) {
        Person person = new Person();
        person.setName(shipmentDto.recipient().name());
        person.setCi(shipmentDto.recipient().ci());
        person.setPhone(shipmentDto.recipient().phone());

        return person;
    }

    public Person shipmentDtoToPersonSender(ShipmentUpdateRequestDto shipmentDto) {
        Person person = new Person();
        person.setName(shipmentDto.senderName());
        person.setCi(shipmentDto.senderCI());
        person.setPhone(shipmentDto.senderPhone());

        return person;
    }

    public PersonRequestDto shipmentDtoToPersonSenderRequest(ShipmentUpdateRequestDto shipmentDto) {
        return new PersonRequestDto(
                shipmentDto.senderName(),
                shipmentDto.senderCI(),
                shipmentDto.senderPhone());
    }

    public PersonRequestDto shipmentDtoToPersonRecipientRequest(ShipmentUpdateRequestDto shipmentDto) {
        return new PersonRequestDto(
                shipmentDto.recipientName(),
                shipmentDto.recipientCI(),
                shipmentDto.recipientPhone());
    }

}
