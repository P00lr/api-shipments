package com.paul.shitment.shipment_service.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Office;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.models.entities.Shipment;
import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;

@Component
public class ShipmentMapper {

    public ShipmentResponseDto entityToDto(Shipment shipment) {
        return new ShipmentResponseDto(
            shipment.getId(),
            shipment.getOriginOffice().getName(),
            shipment.getDestinationOffice().getName(),

            shipment.getSender().getName() == null ? "Nombre vacio" : shipment.getSender().getName(),
            shipment.getSender().getCi() == null ? "CI vacio" : shipment.getSender().getCi(),
            shipment.getSender().getPhone() == null ? "Numero vacio" : shipment.getSender().getPhone(),

            shipment.getRecipient().getName() == null ? "Nombre vacio" : shipment.getRecipient().getName(),
            shipment.getRecipient().getCi() == null ? "CI vacio" : shipment.getRecipient().getCi(),
            shipment.getRecipient().getPhone() == null ? "Numero vacio" : shipment.getRecipient().getPhone(),

            shipment.getCreatedBy().getPerson().getName(),

            shipment.getItemDescription(),
            shipment.getTrackingCode(),
            
            shipment.getCreatedAt(),
            shipment.getUpdatedAt(),
            shipment.getDeliveredAt() == null ? "Envio esperando ser recogido..." : shipment.getDeliveredAt().toString(),

            shipment.getShippingCost(),
            shipment.getStatus()
        );
    }

    public List<ShipmentResponseDto> entitiesToDto(List<Shipment> shipments) {
        return shipments.stream()
            .map((shipment) -> entityToDto(shipment))
            .collect(Collectors.toList());
    }

    public Shipment toShipment(
        Office originOffice, 
        Office destinationOffice, 
        Person sender, 
        Person recipient, 
        AppUser user, 
        String itemDescription,
        Double shippingCost,
        String internalCode,
        String trackingCode,
        ShipmentStatus status) {

            return new Shipment(
                originOffice,
                destinationOffice,
                sender,
                recipient,
                user,
                itemDescription,
                shippingCost,
                internalCode,
                trackingCode,
                status
            );
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
