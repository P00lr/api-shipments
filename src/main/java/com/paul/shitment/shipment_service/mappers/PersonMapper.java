package com.paul.shitment.shipment_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.person.PersonResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.models.entities.Person;

public class PersonMapper {


    public static PersonResponseDto entityToDto(Person person) {
        return new PersonResponseDto(
            person.getId(), 
            person.getName(), 
            person.getCi() , 
            person.getPhone() == null ? "Sin numero" : person.getPhone(), 
            person.isRegistered(),
            person.isActive());
    }

    public static List<PersonResponseDto> entitiesToDtos(List<Person> persons) {
        return persons.stream()
            .map(PersonMapper::entityToDto)
            .collect(Collectors.toList());
    }

    public static Person dtoToEntity(PersonRequestDto personDto) {
        return new Person(
            personDto.name().trim(),
            personDto.ci().trim(),
            personDto.phone()
        );
    }

    public static Person userDtoToEntityPerson(UserRequestDto userDto) {
        return new Person(
            userDto.name().trim(),
            userDto.ci().trim(),
            userDto.phone()
        );
    }

    public static PersonRequestDto shipmentDtoToPersonSenderDto(ShipmentRequestDto shipmentDto) {
        return new PersonRequestDto(
            shipmentDto.senderName().trim(),
            shipmentDto.senderCI().trim(),
            shipmentDto.senderPhone()
        );
    }

    public static PersonRequestDto shipmentDtoToPersonRecipientDto(ShipmentRequestDto shipmentDto) {
        return new PersonRequestDto(
            shipmentDto.recipientName().trim(),
            shipmentDto.recipientCI().trim(),
            shipmentDto.recipientPhone()
        );
    }


    public static Person shipmentDtoToPerson(ShipmentRequestDto shipmentDto) {
        return new Person(
            shipmentDto.senderName().trim(),
            shipmentDto.senderCI().trim(),
            shipmentDto.senderPhone()
        );
    }

    public static Person shipmentDtoToPersonSender(ShipmentUpdateRequestDto shipmentDto) {
        return new Person(
            shipmentDto.senderName().trim(),
            shipmentDto.senderCI().trim(),
            shipmentDto.senderPhone().trim()
        );
    }

    public static Person shipmentDtoToPersonRecipient(ShipmentRequestDto shipmentDto) {
        return new Person(
            shipmentDto.recipientName(),
            shipmentDto.recipientCI(),
            shipmentDto.recipientPhone()
        );
    }
    public static Person shipmentDtoToPersonRecipient(ShipmentUpdateRequestDto shipmentDto) {
        return new Person(
            shipmentDto.recipientName(),
            shipmentDto.recipientCI(),
            shipmentDto.recipientPhone()
        );
    }

}
