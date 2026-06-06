package com.paul.shitment.shipment_service.services.impl;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentDeliveryRequest;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentDispatchRequest;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentDispatchResponse;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentReceivedRequest;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentRequestDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentResponseDto;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentSuggestionDTO;
import com.paul.shitment.shipment_service.mappers.PaginationMapper;
import com.paul.shitment.shipment_service.mappers.PersonMapper;
import com.paul.shitment.shipment_service.mappers.ShipmentMapper;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Office;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.models.entities.Shipment;
import com.paul.shitment.shipment_service.models.entities.ShipmentParty;
import com.paul.shitment.shipment_service.models.entities.Vehicle;
import com.paul.shitment.shipment_service.models.enums.ShipmentPartyRole;
import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;
import com.paul.shitment.shipment_service.repositories.PersonRepository;
import com.paul.shitment.shipment_service.repositories.ShipmentRepository;
import com.paul.shitment.shipment_service.security.service.impl.AuthenticatedUserResolver;
import com.paul.shitment.shipment_service.services.ShipmentService;
import com.paul.shitment.shipment_service.validators.OfficeValidator;
import com.paul.shitment.shipment_service.validators.PersonValidator;
import com.paul.shitment.shipment_service.validators.ShipmentValidator;
import com.paul.shitment.shipment_service.validators.UserValidator;
import com.paul.shitment.shipment_service.validators.VehicleValidator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private static final int MAX_RESULTS_CI_PHONE = 5;
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

    private final VehicleValidator vehicleValidator;

    private final AuthenticatedUserResolver authUserResolver; // ✅ inyectado

    @Override
    public PageResponse<ShipmentResponseDto> getAllShipmentsPaged(ShipmentStatus status, Pageable pageable) {
        log.info("Buscando registros de shipments paginados para el estado requerido: {}", status);

        if (status == null) {
            throw new IllegalArgumentException("El estado del envío es requerido para realizar el listado.");
        }

        AppUser user = getUserOfContext();
        UUID officeId = user.getOffice().getId();

        Pageable pageRequest = (pageable != null) ? pageable : PageRequest.of(0, 10, Sort.by("createdAt").descending());

        if (pageable != null && pageable.getSort().isUnsorted()) {
            pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by("createdAt").descending());
        }

        Page<Shipment> shipmentsPaged;

        if (status == ShipmentStatus.REGISTERED || status == ShipmentStatus.IN_TRANSIT) {
            // Control de Salidas
            shipmentsPaged = shipmentRepository.findByOriginOfficeIdAndStatus(officeId, status, pageRequest);

        } else if (status == ShipmentStatus.DELIVERED) {
            // Filtramos estrictamente por tu atributo deliveredAt entre las 00:00 y las
            // 23:59 de hoy
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

            shipmentsPaged = shipmentRepository.findByDestinationOfficeIdAndStatusAndDeliveredAtBetween(
                    officeId, status, startOfDay, endOfDay, pageRequest);

        } else {
            // Control de Entradas (WAITING_PICKUP)
            shipmentsPaged = shipmentRepository.findByDestinationOfficeIdAndStatus(officeId, status, pageRequest);
        }

        return paginationMapper.toPageResponseDto(shipmentsPaged, shipmentMapper::toShipmentResponseDto);
    }

    @Override
    public PageResponse<ShipmentSuggestionDTO> getSuggestions(String term, Pageable pageable) {
        log.info("Buscando sugerencias de envíos con término: {}", term);
        String validatedTerm = shipmentValidator.validateTerm(term);

        Pageable paginationRequest = PageRequest.of(pageable.getPageNumber(),
                Math.min(pageable.getPageSize(), MAX_RESULTS_CI_PHONE));

        Page<ShipmentSuggestionDTO> suggestionsPage = shipmentRepository.searchByTermPaged(validatedTerm,
                paginationRequest);
        log.info("Se encontraron {} sugerencias", suggestionsPage.getContent().size());
        return paginationMapper.toPageResponseDto(suggestionsPage, item -> item);
    }

    @Override
    public ShipmentResponseDto getShipment(@NonNull UUID id) {
        log.info("Verificando existencia de envio con id: {}", id);

        Shipment shipment = shipmentValidator.getShipmentbyIdOrThrow(id);

        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    @Override
    public ShipmentResponseDto getShipmentByTrackingCode(String trackingCode) {
        log.info("Verificando existencia de envio con id: {}", trackingCode);

        Shipment shipment = shipmentValidator.getShipmentbyTrackingCodeOrThrow(trackingCode.trim());

        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    @Override
    public AppUser getUserOfContext() {
        // Leemos el contexto de seguridad con precaución
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Verificamos si realmente hay un usuario autenticado con token
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        return userValidator.getUserByUsernameOrThrow(userDetails.getUsername());
    }

    @Override
    @Transactional
    public Set<ShipmentResponseDto> shipmentReceived(ShipmentReceivedRequest dto) {
        log.info("ID DE ENVIOS: {}", dto);
        Set<ShipmentResponseDto> shipments = new HashSet<>();

        for (UUID shipmentId : dto.shipmentsId()) {
            Shipment shipment = shipmentValidator.getShipmentbyIdOrThrow(shipmentId);
            shipment.markAsWaitingPickup();
            shipments.add(shipmentMapper.toShipmentResponseDto(shipment));
        }
        return shipments;
    }

    @Transactional
    @Override
    public ShipmentResponseDto createShipment(@NonNull ShipmentRequestDto shipmentDto) {
        log.info("Creando nuevo shipment...");

        AppUser user = authUserResolver.resolve();
        Office originOffice = officeValidation.getOfficeByIdOrThrow(user.getOffice().getId());
        Office destOffice = officeValidation.getOfficeByIdOrThrow(shipmentDto.destinationOfficeId());

        Shipment shipment = createProcessShipment(shipmentDto, originOffice, destOffice, user);

        shipmentRepository.save(shipment);
        log.info("Shipment [{}] creado correctamente", shipment.getInternalCode());

        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    private Shipment createProcessShipment(ShipmentRequestDto shipmentDto,
            Office originOffice,
            Office destinationOffice,
            AppUser user) {

        Person sender = handlePersonCreateOrUpdate(personMapper.shipmentDtoToPersonSenderDto(shipmentDto));
        Person recipient = handlePersonCreateOrUpdate(personMapper.shipmentDtoToPersonRecipientDto(shipmentDto));

        Shipment shipment = Shipment.builder()
                .internalCode(generateInternalCode())
                .trackingCode(generateTrackingCode())
                .itemDescription(shipmentDto.itemDescription())
                .shippingCost(shipmentDto.shippingCost())
                .status(ShipmentStatus.REGISTERED)
                .originOffice(originOffice)
                .destinationOffice(destinationOffice)
                .createdBy(user)
                .build();

        shipment.addParty(buildParty(sender, ShipmentPartyRole.SENDER));
        shipment.addParty(buildParty(recipient, ShipmentPartyRole.RECIPIENT));

        return shipment;
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
    public ShipmentResponseDto markAsDelivered(UUID shipmentUUID, ShipmentDeliveryRequest request) {
        log.info("Marcando envío [{}] como entregado", shipmentUUID);

        Shipment shipment = shipmentValidator.getShipmentbyIdOrThrow(shipmentUUID);

        shipmentValidator.validateDelivery(shipment, request.documentNumber());

        shipment.deliver();

        log.info("Envío [{}] entregado correctamente", shipmentUUID);
        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    @Transactional
    @Override
    public ShipmentResponseDto markWaitingPickuk(UUID shipmentId) {
        Shipment shipment = shipmentValidator.getShipmentbyIdOrThrow(shipmentId);
        shipment.markAsWaitingPickup();
        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    @Transactional
    @Override
    public ShipmentResponseDto canceledShipment(UUID id) {
        log.info("Cancelando envío [{}]", id);
        Shipment shipment = shipmentValidator.getShipmentbyIdOrThrow(id);

        shipment.cancel();

        shipmentRepository.save(shipment);
        log.info("Envío [{}] cancelado correctamente", id);

        return shipmentMapper.toShipmentResponseDto(shipment);
    }

    // METODOS AUXILIARES
    @Transactional
    private Person handlePersonCreateOrUpdate(PersonRequestDto personDto) {
        return personRepository.findByDocumentNumber(personDto.documentNumber())
                .map(existingPerson -> updateExistingPerson(existingPerson, personDto))
                .orElseGet(() -> createNewPerson(personDto));
    }

    private Person updateExistingPerson(Person existingPerson, PersonRequestDto dto) {
        log.info("Actualizando persona: {}", dto.documentNumber());
        personValidator.validateForUpdate(dto, existingPerson.getId());
        return personMapper.updateAtributtePerson(existingPerson, dto);
    }

    private Person createNewPerson(PersonRequestDto dto) {
        log.info("Creando nueva persona: {}", dto.documentNumber());
        personValidator.validateForCreate(dto);
        return personRepository.save(personMapper.dtoToEntity(dto));
    }

    private ShipmentParty buildParty(Person person, ShipmentPartyRole role) {

        ShipmentParty party = new ShipmentParty();

        party.setDocumentType(person.getDocumentType());
        party.setDocumentNumber(person.getDocumentNumber());
        party.setFullName(person.getFullName());
        party.setPhone(person.getPhone());
        party.setRole(role);
        party.setPerson(person);

        return party;
    }

    @Transactional
    @Override
    public ShipmentDispatchResponse dispatchShipments(UUID vehicleId, ShipmentDispatchRequest dispatches) {

        Vehicle vehicle = vehicleValidator.getVehicleByIdOrThrow(vehicleId);

        Set<String> trackingCodes = new HashSet<>();

        for (UUID shipmentId : dispatches.shipmentUuid()) {
            Shipment shipment = shipmentValidator.getShipmentbyIdOrThrow(shipmentId);
            shipment.assignToVehicle(vehicle);
            shipment.markInTransit();
            trackingCodes.add(shipment.getTrackingCode());
        }

        return new ShipmentDispatchResponse(vehicle.getInternalCode(), trackingCodes);
    }

    @Transactional
    @Override
    public ShipmentResponseDto cancelDispatch(UUID shipmentId) {
        Shipment shipment = shipmentValidator.getShipmentbyIdOrThrow(shipmentId);
        shipment.markRegistered();
        shipment.removeFromVehicle();
        return shipmentMapper.toShipmentResponseDto(shipment);

    }

}
