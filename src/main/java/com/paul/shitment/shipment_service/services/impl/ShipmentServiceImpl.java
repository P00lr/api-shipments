package com.paul.shitment.shipment_service.services.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;
import com.paul.shitment.shipment_service.mappers.PaginationMapper;
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
    private static final String ALLOWED_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private final ShipmentRepository shipmentRepository;
    private final ShipmentValidator shipmentValidator;
    private final ShipmentMapper shipmentMapper;

    private final PersonRepository personRepository;
    private final PersonValidator personValidator;
    private final PersonMapper personMapper;

    private final UserValidator userValidator;
    private final OfficeValidator officeValidation;
    private final JdbcTemplate jdbcTemplate;
    private final PaginationMapper paginationMapper;


    @Override
    public PageResponse<ShipmentResponseDto> getAllShipmentsPaged(@NonNull Pageable pageable) {
        log.info("Buscando registros de shipments paginados");
        Page<Shipment> shipmentsPaged = shipmentRepository.findAll(pageable);
        return paginationMapper.toPageResponseDto(shipmentsPaged, shipmentMapper::toShipmentResponseDto);
    }

    @Override
    public List<ShipmentSuggestionDTO> getSuggestions(String term) {
        String validatedTerm = shipmentValidator.validateTerm(term);

        Pageable limited = PageRequest.of(0, MAX_RESULTS_CI_PHONE);

        // Tracking code (letras o guiones) resultado único, sin límite
        if (validatedTerm.matches(".*[a-zA-Z-].*")) {
            return shipmentRepository.searchByTerm(validatedTerm, Pageable.unpaged());
        }

        // CI últimos 3 registros REGISTERED
        if (validatedTerm.matches("\\d{7,8}(-[a-zA-Z]{1,2})?")) {
            return shipmentRepository.searchByTerm(validatedTerm, limited);
        }

        // Teléfono últimos 3 registros REGISTERED
        if (validatedTerm.matches("\\d{8}")) {
            return shipmentRepository.searchByTerm(validatedTerm, limited);
        }

        // Fallback general → últimos 3 registros REGISTERED
        return shipmentRepository.searchByTerm(validatedTerm, limited);
    }

    @Override
    public ShipmentResponseDto getShipment(@NonNull UUID id) {
        log.info("Verificando existencia de envio con id: {}", id);

        Shipment shipment = shipmentValidator.getShipmentbyIdOrThrow(id);

        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    @Transactional
    @Override
    public ShipmentResponseDto createShipment(ShipmentRequestDto shipmentDto) {
        log.info("Creando nuevo shipment...");


        Office destinationOffice = officeValidation.getOfficeByIdOrThrow(shipmentDto.destinationOfficeId());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails)auth.getPrincipal();

        String username = userDetails.getUsername();
        
        AppUser user = userValidator.getUserByUsernameOrThrow(username);
        UUID officeId = user.getOffice().getId();

        Office originOffice = officeValidation.getOfficeByIdOrThrow(officeId);


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

        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    @Transactional
    @Override
    public ShipmentResponseDto updateShipment(@NonNull UUID id, ShipmentUpdateRequestDto shipmentDto) {
        log.info("Verificando status REGISTERED para poder editar");
        Shipment shipment = shipmentValidator.validateForUpdate(id, shipmentDto);

        log.info("Validando datos de sender y recipient");
        validatePersonInShipment(shipmentDto, shipment);

        log.info("Actualizando registro");
        shipment.updateFromShipmentUpdateRequestDto(shipmentDto);

        shipmentRepository.save(shipment);
        log.info("Se actualizo correctamente el registro {}", shipmentDto);

        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    private String generateInternalCode() {
        Long nextVal = jdbcTemplate.queryForObject("SELECT nextval('shipment_seq')", Long.class);
        String today = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        return String.format("SHP-%s-%06d", today, nextVal);
    }

    private String generateTrackingCode() {
        SecureRandom random = new SecureRandom();
        String code;
        final int totalCharacters = ALLOWED_CHARS.length();

        do {
            StringBuilder sb = new StringBuilder(6);
            for (int i = 0; i < 5; i++) {
                if (i == 2)
                    sb.append('-');

                int randomIndex = random.nextInt(totalCharacters);
                char randomChar = ALLOWED_CHARS.charAt(randomIndex);
                sb.append(randomChar);
            }
            code = sb.toString();
        } while (shipmentRepository.existsByTrackingCode(code));

        return code;
    }

    @Transactional
    @Override
    public ShipmentResponseDto markAsDelivered(@NonNull UUID id, String inputCI) {
        log.info("Marcando envío [{}] como entregado", id);

        Shipment shipment = shipmentValidator.getShipmentbyIdOrThrow(id);
        String recipientCI = shipment.getRecipient().getCi();

        shipmentValidator.requiresIDByAmount(shipment.getShippingCost(), recipientCI, inputCI);

        shipment.deliver();
        shipmentRepository.save(shipment);

        log.info("Envío [{}] entregado correctamente", id);
        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    @Transactional
    @Override
    public ShipmentResponseDto canceledShipment(@NonNull UUID id) {
        log.info("Cancelando envío [{}]", id);
        Shipment shipment = shipmentValidator.getShipmentbyIdOrThrow(id);

        shipment.cancel(); // lógica de negocio dentro del modelo

        shipmentRepository.save(shipment);
        log.info("Envío [{}] cancelado correctamente", id);

        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    @Transactional
    private Person handlePersonCreateOrUpdate(ShipmentRequestDto shipmentDto, boolean isSender) {
        PersonRequestDto personDto = isSender ? personMapper.shipmentDtoToPersonSenderDto(shipmentDto)
                : personMapper.shipmentDtoToPersonRecipientDto(shipmentDto);

        String ci = isSender ? shipmentDto.sender().ci() : shipmentDto.recipient().ci();

        try {
            // Intentar encontrar la persona por CI
            Person existingPerson = personRepository.findByCi(ci).orElse(null);

            if (existingPerson != null) {
                // Actualizar persona existente
                log.info("Actualizando persona existente con CI: {}", ci);
                updatePersonAttributes(personDto, existingPerson.getId());
                return existingPerson;

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

    private void updatePersonAttributes(
            PersonRequestDto personDto,
            @NonNull UUID personId) {

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
    }

    private void validatePersonInShipment(ShipmentUpdateRequestDto shipmentDto, Shipment shipment) {
        PersonRequestDto senderDto = personMapper.shipmentDtoToPersonSenderRequest(shipmentDto);
        UUID senderId = shipment.getSender().getId();
        personValidator.validateForUpdate(senderDto, senderId);

        PersonRequestDto recipientDto = personMapper.shipmentDtoToPersonRecipientRequest(shipmentDto);
        UUID recipientId = shipment.getRecipient().getId();
        personValidator.validateForUpdate(recipientDto, recipientId);
    }

}
