package com.paul.shitment.shipment_service.services.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public List<ShipmentSuggestionDTO> getSuggestions(String term) {

        String validatedTerm = shipmentValidator.validateTerm(term);
        // Si tiene letras o guiones - buscar por trackingCode
        if (validatedTerm.matches(".*[a-zA-Z-].*")) {
            return shipmentRepository.searchByTrackingCode(validatedTerm);
        }

        // Si son solo números
        if (validatedTerm.matches("\\d{8}")) { // teléfono
            return shipmentRepository.searchByPhone(validatedTerm);
        }

        // Si son 7 u 8 dígitos buscar por CI
        if (validatedTerm.matches("\\d{7,8}(-[a-zA-Z]{1,2})?")) {
            return shipmentRepository.searchByCi(validatedTerm);
        }

        // Por defecto, usa busqueda general flexible
        return shipmentRepository.searchShipmentsByTerm(validatedTerm);
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

    @Transactional
    @Override
    public ShipmentResponseDto createShipment(ShipmentRequestDto shipmentDto) {
        log.info("Creando nuevo shipment...");

        shipmentValidator.validateShipment(shipmentDto);

        Office originOffice = officeValidation.existsOffice(shipmentDto.originOfficeId());
        Office destinationOffice = officeValidation.existsOffice(shipmentDto.destinationOfficeId());

        AppUser user = userValidator.existsUser(shipmentDto.userId());

        Person sender = updateAttributeSender(shipmentDto);
        personRepository.save(sender);

        Person recipient = updateAttributeRecipient(shipmentDto);
        personRepository.save(recipient);

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

    private String generateInternalCode() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('shipment_seq')", Long.class);
        String today = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        return String.format("SHP-%s-%06d", today, nextVal);
    }

    private String generateTrackingCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        String formatted = String.format("%s-%s", uuid.substring(0, 4), uuid.substring(4, 8));
        return "TRF-" + formatted;
    }

    /*
     * private boolean updateIfChanged(ShipmentRequestDto shipmentDto) {
     * Person person =
     * personValidator.validateExistsPersonByCi(shipmentDto.senderCI());
     * 
     * Person personDto = PersonMapper.shipmentDtoToPerson(shipmentDto);
     * 
     * return person.equals(personDto);
     * }
     */

    private Person updateAttributeSender(ShipmentRequestDto shipmentDto) {

        PersonRequestDto personDto = PersonMapper.shipmentDtoToPersonSenderDto(shipmentDto);

        Person personForId = personValidator.validateExistsPersonByCi(shipmentDto.senderCI());

        Person personSender = personValidator.validateUpdate(personDto, personForId.getId());

        personSender.setCi(!personSender.getCi().equals(personDto.ci()) ? personDto.ci() : personSender.getCi());
        personSender
                .setName(!personSender.getName().equals(personDto.name()) ? personDto.name() : personSender.getName());
        personSender.setPhone(
                !personSender.getPhone().equals(personDto.phone()) ? personDto.phone() : personSender.getPhone());

        return personSender;
    }

    private Person updateAttributeRecipient(ShipmentRequestDto shipmentDto) {
        PersonRequestDto PersonDto = PersonMapper.shipmentDtoToPersonRecipientDto(shipmentDto);

        Person personForId = personValidator.validateExistsPersonByCi(shipmentDto.recipientCI());

        Person personRecipient = personValidator.validateUpdate(PersonDto, personForId.getId());

        personRecipient
                .setCi(!personRecipient.getCi().equals(PersonDto.ci()) ? PersonDto.ci() : personRecipient.getCi());
        personRecipient.setName(
                !personRecipient.getName().equals(PersonDto.name()) ? PersonDto.name() : personRecipient.getName());
        personRecipient.setPhone(
                !personRecipient.getPhone().equals(PersonDto.phone()) ? PersonDto.phone() : personRecipient.getPhone());

        return personRecipient;
    }

    @Transactional
    @Override
    public ShipmentResponseDto markAsDelivered(UUID id) {
        log.info("Marcando envío [{}] como entregado", id);
        Shipment shipment = shipmentValidator.existsShipment(id);

        shipment.deliver(); // la logica encapsulada en la entidad

        shipmentRepository.save(shipment);
        log.info("Envío [{}] entregado correctamente", id);

        return ShipmentMapper.entityToDto(shipment);
    }

    @Transactional
    @Override
    public ShipmentResponseDto canceledShipment(UUID id) {
        log.info("Cancelando envío [{}]", id);
        Shipment shipment = shipmentValidator.existsShipment(id);

        shipment.cancel(); // lógica de negocio dentro del modelo

        shipmentRepository.save(shipment);
        log.info("Envío [{}] cancelado correctamente", id);

        return ShipmentMapper.entityToDto(shipment);
    }

    // metodo auxiliar
    /*
     * private Person handlePersonOverride(Person person, String name, String phone,
     * Boolean updateGlobally) {
     * boolean override = name != null || phone != null;
     * if (!override)
     * return person;
     * 
     * String tempName = name != null ? name : person.getName();
     * String tempPhone = phone != null ? phone : person.getPhone();
     * 
     * if (Boolean.TRUE.equals(updateGlobally)) {
     * person.setName(tempName);
     * person.setPhone(tempPhone);
     * personRepository.save(person);
     * }
     * 
     * return person;
     * }
     */

}
