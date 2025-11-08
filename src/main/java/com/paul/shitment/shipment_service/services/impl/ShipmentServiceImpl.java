package com.paul.shitment.shipment_service.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO;
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
import com.paul.shitment.shipment_service.validators.OfficeValidator;
import com.paul.shitment.shipment_service.validators.PersonValidator;
import com.paul.shitment.shipment_service.validators.ShipmentValidator;
import com.paul.shitment.shipment_service.validators.UserValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private static final int MAX_RESULTS_CI_PHONE = 3;

    private final ShipmentRepository shipmentRepository;
    private final ShipmentValidator shipmentValidator;
    private final ShipmentMapper shipmentMapper;

    private final PersonRepository personRepository;
    private final PersonValidator personValidator;
    private final PersonMapper personMapper;

    private final UserValidator userValidator;
    private final OfficeValidator officeValidation;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<ShipmentResponseDto> getAllShipments() {
        log.info("Buscando registros de shipments ordenados por fecha DESC");
        List<Shipment> shipments = shipmentRepository.findAllOrdered();

        if (shipments.isEmpty()) {
            log.info("No se encontraron registros");
            return List.of(); // devolver lista vacía
        }

        return shipmentMapper.entitiesToDto(shipments);
    }

    @Override
    public PageResponse<ShipmentResponseDto> getAllShipmentsPaged(int pageNo, int pageSize) {
        log.info("Buscando registros de shipments paginados");

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<Shipment> shipments = shipmentRepository.findAllOrdered(pageRequest);

        if (shipments.isEmpty()) {
            return new PageResponse<>(List.of(), 0, pageSize, 0L, 0, true, true);
        }

        List<ShipmentResponseDto> shipmentDtos = shipments
                .getContent()
                .stream()
                .map(shipmentMapper::entityToDto)
                .toList();

        return new PageResponse<>(
                shipmentDtos,
                shipments.getNumber(),
                shipments.getSize(),
                shipments.getTotalElements(),
                shipments.getTotalPages(),
                shipments.isFirst(),
                shipments.isLast());
    }

    @Override
    public List<ShipmentSuggestionDTO> getSuggestions(String term) {
        String validatedTerm = shipmentValidator.validateTerm(term);

        Pageable limited = PageRequest.of(0, MAX_RESULTS_CI_PHONE);

        //Tracking code (letras o guiones) → resultado único, sin límite
        if (validatedTerm.matches(".*[a-zA-Z-].*")) {
            return shipmentRepository.searchByTerm(validatedTerm, Pageable.unpaged());
        }

        //CI → últimos 3 registros REGISTERED
        if (validatedTerm.matches("\\d{7,8}(-[a-zA-Z]{1,2})?")) {
            return shipmentRepository.searchByTerm(validatedTerm, limited);
        }

        //Teléfono → últimos 3 registros REGISTERED
        if (validatedTerm.matches("\\d{8}")) {
            return shipmentRepository.searchByTerm(validatedTerm, limited);
        }

        //Fallback general → últimos 3 registros REGISTERED
        return shipmentRepository.searchByTerm(validatedTerm, limited);
    }



    @Override
    public ShipmentResponseDto getShipment(UUID id) {
        log.info("Verificando existencia de envio con id: {}", id);

        Shipment shipment = shipmentValidator.existsShipment(id);

        return shipmentMapper.entityToDto(shipment);
    }

    @Transactional
    @Override
    public ShipmentResponseDto createShipment(ShipmentRequestDto shipmentDto) {
        log.info("Creando nuevo shipment...");

        shipmentValidator.validateShipment(shipmentDto);

        Office originOffice = officeValidation.getOfficeByIdOrThrow(shipmentDto.originOfficeId());
        Office destinationOffice = officeValidation.getOfficeByIdOrThrow(shipmentDto.destinationOfficeId());

        AppUser user = userValidator.existsUser(shipmentDto.userId());

        Person sender = handlePersonCreateOrUpdate(shipmentDto, true); // true para sender
        Person recipient = handlePersonCreateOrUpdate(shipmentDto, false); // false para recipient

        Shipment shipment = Shipment.builder()
                .originOffice(originOffice)
                .destinationOffice(destinationOffice)
                .sender(sender)
                .recipient(recipient)
                .createdBy(user)
                .itemDescription(shipmentDto.itemDescription())
                .shippingCost(shipmentDto.shippingCost())
                .internalCode(generateInternalCode())
                .trackingCode(generateTrackingCode())
                .status(ShipmentStatus.REGISTERED)
                .build();

        shipmentRepository.save(shipment);
        log.info("Shipment [{}] creado correctamente", shipment.getTrackingCode());

        return shipmentMapper.entityToDto(shipment);
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

        return shipmentMapper.entityToDto(shipment);
    }

    private String generateInternalCode() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('shipment_seq')", Long.class);
        String today = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        return String.format("SHP-%s-%06d", today, nextVal);
    }

    private String generateTrackingCode() {
        String uuid = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
        String formatted = String.format("%s-%s",
                uuid.substring(0, 4),
                uuid.substring(4, 8));
        return "TRF-" + formatted;
    }

    @Transactional
    @Override
    public ShipmentResponseDto markAsDelivered(UUID id, String ci) {
        log.info("Marcando envío [{}] como entregado", id);

        Shipment shipment = shipmentValidator.existsShipment(id);
        String dbCI = shipment.getRecipient().getCi();

        if (shipment.getShippingCost() >= 50) {
            personValidator.validateCiMatch(dbCI, ci != null ? ci.trim() : null);
        }

        shipment.deliver();
        shipmentRepository.save(shipment);

        log.info("Envío [{}] entregado correctamente", id);
        return shipmentMapper.entityToDto(shipment);
    }

    @Transactional
    @Override
    public ShipmentResponseDto canceledShipment(UUID id) {
        log.info("Cancelando envío [{}]", id);
        Shipment shipment = shipmentValidator.existsShipment(id);

        shipment.cancel(); // lógica de negocio dentro del modelo

        shipmentRepository.save(shipment);
        log.info("Envío [{}] cancelado correctamente", id);

        return shipmentMapper.entityToDto(shipment);
    }

    // PERSONA

    private Person handlePersonCreateOrUpdate(ShipmentRequestDto shipmentDto, boolean isSender) {
        PersonRequestDto personDto = isSender ? personMapper.shipmentDtoToPersonSenderDto(shipmentDto)
                : personMapper.shipmentDtoToPersonRecipientDto(shipmentDto);

        String ci = isSender ? shipmentDto.senderCI() : shipmentDto.recipientCI();

        try {
            // Intentar encontrar la persona por CI
            Person existingPerson = personRepository.findByCi(ci).orElse(null);

            if (existingPerson != null) {
                // Actualizar persona existente
                log.info("Actualizando persona existente con CI: {}", ci);
                return updatePersonAttributes(personDto, existingPerson.getId());
            } else {
                // Crear nueva persona
                log.info("Creando nueva persona con CI: {}", ci);
                personValidator.validateForCreate(personDto);
                Person newPerson = personMapper.dtoToEntity(personDto);
                return personRepository.save(newPerson);
            }
        } catch (DataAccessException e) {
            log.error("Error al procesar persona: {}", e.getMessage());
            throw e;
        }
    }

    private Person updatePersonAttributes(
            PersonRequestDto personDto,
            UUID personId) {

        personValidator.validateForUpdate(personDto, personId);

        Person person = personValidator.getPersonByIdOrThrow(personId);

        if (!person.getCi().equals(personDto.ci())) {
            person.setCi(personDto.ci());
        }
        if (!person.getName().equals(personDto.name())) {
            person.setName(personDto.name());
        }
        if (!person.getPhone().equals(personDto.phone())) {
            person.setPhone(personDto.phone());
        }

        return person;
    }

}
