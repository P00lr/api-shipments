package com.paul.shitment.shipment_service.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;
import com.paul.shitment.shipment_service.mappers.PersonMapper;
import com.paul.shitment.shipment_service.mappers.ShipmentMapper;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Office;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.models.entities.Shipment;
import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;
import com.paul.shitment.shipment_service.repositories.PersonRepository;
import com.paul.shitment.shipment_service.repositories.ShipmentRepository;
import com.paul.shitment.shipment_service.services.ShipmentService;
import com.paul.shitment.shipment_service.validators.office.OfficeValidation;
import com.paul.shitment.shipment_service.validators.person.PersonValidator;
import com.paul.shitment.shipment_service.validators.shipment.ShipmentValidator;
import com.paul.shitment.shipment_service.validators.user.UserValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentValidator shipmentValidator;
    private final PersonRepository personRepository;
    private final PersonValidator personValidator;
    private final UserValidator userValidator;
    private final OfficeValidation officeValidation;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ShipmentResponseDto> getAllShipments() {
        log.info("Buscando registros de shipments");
        shipmentValidator.existsShipments();

        List<Shipment> shipments = shipmentRepository.findAll();
        log.info("Si se encontraron registros");

        return ShipmentMapper.entitiesToDto(shipments);
    }

    @Override
    public PageResponse<ShipmentResponseDto> getAllShipmentsPaged(int pageNo, int pageSize, String sortBy) {
        log.info("Buscando registros de shipments paginados");
        shipmentValidator.existsShipments();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());

        Page<Shipment> shipments = shipmentRepository.findAll(pageRequest);
        log.info("Se encontraron registros");

        List<ShipmentResponseDto> listShipment = new ArrayList<>();
        for (Shipment shipment : shipments.getContent()) {
            listShipment.add(ShipmentMapper.entityToDto(shipment));
        }

        return new PageResponse<>(
                listShipment,
                shipments.getNumber(), // página actual
                shipments.getSize(), // tamaño de página
                shipments.getTotalElements(), // total de registros
                shipments.getTotalPages(), // total de páginas
                shipments.isFirst(), // ¿es la primera página?
                shipments.isLast() // ¿es la última página?
        );
    }

    @Override
    public ShipmentResponseDto getShipment(UUID id) {
        log.info("Verificando existencia de envio con id: {}", id);

        Shipment shipment = shipmentValidator.existsShipment(id);

        return ShipmentMapper.entityToDto(shipment);
    }

    @Override
    public ShipmentResponseDto createShipment(ShipmentRequestDto shipmentDto) {

        log.info("Verificando que esten correctos los datos");
        shipmentValidator.validateShipment(shipmentDto);

        Office originOffice = officeValidation.existsOffice(shipmentDto.originOfficeId());
        Office destinationOffice = officeValidation.existsOffice(shipmentDto.destinationOfficeId());

        Person sender = findSenderOrCreate(shipmentDto.senderCI(), shipmentDto);
        Person recipient = findRecipientOrCreate(shipmentDto.recipientCI(), shipmentDto);

        AppUser user = userValidator.existsUser(shipmentDto.userId());

        Shipment shipment = ShipmentMapper.toShipment(
                originOffice,
                destinationOffice,
                sender,
                recipient,
                user,
                shipmentDto.itemDescription(),
                shipmentDto.shippingCost(),
                generateInternalCode(),
                generateTrackingCode(),
                ShipmentStatus.REGISTERED);

        try {
            shipmentRepository.save(shipment);
            log.info("Se guardo correctamente el envio {}", shipment);
        } catch (DataAccessException e) {
            log.error("Error al guardar el envio {}", e);
            throw e;
        }

        return ShipmentMapper.entityToDto(shipment);
    }

    // requestUpdate contiene los datos completos para update
    @Override
    public ShipmentResponseDto updateShipment(UUID id, ShipmentUpdateRequestDto shipmentDto) {
        log.info("Verificando status REGISTERED para poder editar");
        Shipment shipment = shipmentValidator.validateUpdateShipemnt(id, shipmentDto);

        log.info("Actualizando registro");

        if (!shipmentDto.senderName().equals(shipment.getSender().getName()))
            shipment.getSender().setName(shipmentDto.senderName());

        if (!shipmentDto.senderCI().equals(shipment.getSender().getCi()) && !shipmentDto.senderCI().isEmpty())
            shipment.getSender().setCi(shipmentDto.senderCI());

        if (!shipmentDto.senderPhone().equals(shipment.getSender().getPhone()))
            shipment.getSender().setPhone(shipmentDto.senderPhone());

        if (!shipmentDto.recipientName().equals(shipment.getRecipient().getName()))
            shipment.getRecipient().setName(shipmentDto.recipientName());

        if (!shipmentDto.recipientCI().equals(shipment.getRecipient().getCi()) && !shipmentDto.recipientCI().isEmpty())
            shipment.getRecipient().setCi(shipmentDto.recipientCI());

        if (!shipmentDto.recipientPhone().equals(shipment.getRecipient().getPhone()))
            shipment.getRecipient().setPhone(shipmentDto.recipientPhone());

        if (!shipment.getItemDescription().equals(shipmentDto.itemDescription()))
            shipment.setItemDescription(shipmentDto.itemDescription());

        if (shipment.getShippingCost() != shipmentDto.shippingCost()) {
            shipment.setShippingCost(shipmentDto.shippingCost());
        }

        try {
            shipmentRepository.save(shipment);
            log.info("Se actualizo correctamente el registro {}", shipmentDto);
        } catch (DataAccessException e) {
            log.info("Error al guardar el registro {}", e);
            throw e;
        }

        return ShipmentMapper.entityToDto(shipment);
    }

    @Transactional
    @Override
    public ShipmentResponseDto canceledShipment(UUID id) {
        log.info("Verificando existencia del registro");
        Shipment shipment = shipmentValidator.existsShipment(id);

        if (shipment.getStatus() == ShipmentStatus.CANCELED) {
            return ShipmentMapper.entityToDto(shipment);
        }

        shipment.setStatus(ShipmentStatus.CANCELED);

        try {
            shipmentRepository.save(shipment);
            log.info("Se cancelo exitosamente el envio");
        } catch (DataAccessException e) {

            log.error("Error al cancelar el envio");
            throw e;
        }

        return ShipmentMapper.entityToDto(shipment);
    }

    private String generateInternalCode() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('shipment_seq')", Long.class);
        String today = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        return String.format("SHP-%s-%06d", today, nextVal);
    }

    private String generateTrackingCode() {
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase(); // 8 caracteres
        return "TRF-" + randomPart;
    }

    private Person findSenderOrCreate(String ci, ShipmentRequestDto shipmentDto) {

        Optional<Person> senderToBeEvaluated = personValidator.validateGetPersonByCi(ci);

        if (senderToBeEvaluated.isEmpty()) {
            Person sender = PersonMapper.shipmentDtoToPersonSender(shipmentDto);
            personRepository.save(sender);
            return sender;
        }

        return senderToBeEvaluated.get();
    }
    
    private Person findRecipientOrCreate(String ci, ShipmentRequestDto shipmentDto) {

        Optional<Person> recipientToBeEvaluated = personValidator.validateGetPersonByCi(shipmentDto.recipientCI());

        if (recipientToBeEvaluated.isEmpty()) {
            Person recipient = PersonMapper.shipmentDtoToPersonRecipient(shipmentDto);
            personRepository.save(recipient);
            return recipient;
        }

        return recipientToBeEvaluated.get();
    }

}
