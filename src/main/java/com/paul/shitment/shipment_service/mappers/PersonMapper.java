package com.paul.shitment.shipment_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.person.PersonResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.models.entities.Person;

@Component
public class PersonMapper {


    public PersonResponseDto entityToDto(Person person) {
        return new PersonResponseDto(
            person.getId(), 
            person.getName(), 
            person.getCi() , 
            person.getPhone() == null ? "Sin numero" : person.getPhone(), 
            person.isRegistered(),
            person.isActive());
    }

    public List<PersonResponseDto> entitiesToDtos(List<Person> persons) {
        return persons.stream()
            .map((person -> entityToDto(person)))
            .collect(Collectors.toList());
    }

    public Person dtoToEntity(PersonRequestDto personDto) {
        return new Person(
            personDto.name().trim(),
            personDto.ci().trim(),
            personDto.phone()
        );
    }

    public Person userDtoToEntityPerson(UserRequestDto userDto) {
        return new Person(
            userDto.name().trim(),
            userDto.ci().trim(),
            userDto.phone()
        );
    }

    public PersonRequestDto shipmentDtoToPersonSenderDto(ShipmentRequestDto shipmentDto) {
        return new PersonRequestDto(
            shipmentDto.senderName().trim(),
            shipmentDto.senderCI().trim(),
            shipmentDto.senderPhone()
        );
    }

    public PersonRequestDto shipmentDtoToPersonRecipientDto(ShipmentRequestDto shipmentDto) {
        return new PersonRequestDto(
            shipmentDto.recipientName().trim(),
            shipmentDto.recipientCI().trim(),
            shipmentDto.recipientPhone()
        );
    }


    public Person shipmentDtoToPerson(ShipmentRequestDto shipmentDto) {
        return new Person(
            shipmentDto.senderName().trim(),
            shipmentDto.senderCI().trim(),
            shipmentDto.senderPhone()
        );
    }

    public Person shipmentDtoToPersonSender(ShipmentUpdateRequestDto shipmentDto) {
        return new Person(
            shipmentDto.senderName().trim(),
            shipmentDto.senderCI().trim(),
            shipmentDto.senderPhone().trim()
        );
    }

    public Person shipmentDtoToPersonRecipient(ShipmentRequestDto shipmentDto) {
        return new Person(
            shipmentDto.recipientName(),
            shipmentDto.recipientCI(),
            shipmentDto.recipientPhone()
        );
    }
    public Person shipmentDtoToPersonRecipient(ShipmentUpdateRequestDto shipmentDto) {
        return new Person(
            shipmentDto.recipientName(),
            shipmentDto.recipientCI(),
            shipmentDto.recipientPhone()
        );
    }

}
