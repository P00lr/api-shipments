package com.paul.shitment.shipment_service.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.dto.office.OfficeResponse;
import com.paul.shitment.shipment_service.dto.office.OfficeResponseDto;
import com.paul.shitment.shipment_service.mappers.OfficeMapper;
import com.paul.shitment.shipment_service.mappers.PaginationMapper;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Office;
import com.paul.shitment.shipment_service.repositories.OfficeRepository;
import com.paul.shitment.shipment_service.services.OfficeService;
import com.paul.shitment.shipment_service.services.ShipmentService;
import com.paul.shitment.shipment_service.validators.OfficeValidator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeValidator officeValidator;
    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;
    private final PaginationMapper paginationMapper;
    private final ShipmentService shipmentService;

    @Override
    public PageResponse<OfficeResponseDto> getAllOfficesPaged(@NonNull Pageable pageable) {
        log.info("Obteniendo todas las oficinas");
        
        Page<Office> officesPage = officeRepository.findAll(pageable);

        return paginationMapper.toPageResponseDto(officesPage, officeMapper::toDto);
    }

    @Override
    public List<OfficeResponse> getAllOffices() {
        log.info("Verificando lista de oficinas");
        return officeMapper.toOfficesDto(officeRepository.findAll());
    }

    @Override
    public List<OfficeResponse> getOfficeAvailableDestination() {
        log.info("Lista de oficinas disponible para destino");
        AppUser user = shipmentService.getUserOfContext();
        UUID officeId = user.getOffice().getId();

        //omitiendo la oficina de origen
        List<Office> offices = officeRepository.findByIdNot(officeId);
        
        return officeMapper.toOfficesDto(offices); 
    }

    @Override
    public OfficeResponseDto getOfficeById(@NonNull UUID id) {
        log.info("Obteniendo oficina con id: {}", id);
        Office office = officeValidator.getOfficeByIdOrThrow(id);
        return officeMapper.toDto(office);
    }

    @Override
    @Transactional
    public OfficeResponseDto createOffice(OfficeRequestDto dto) {
        officeValidator.validateForCreation(dto);

        Office office = officeMapper.toEntity(dto);

        officeRepository.save(office);

        return officeMapper.toDto(office);
    }

    @Override
    @Transactional
    public OfficeResponseDto updateOffice(@NonNull UUID id, OfficeRequestDto dto) {
        officeValidator.validateForUpdate(id, dto);

        Office office = officeValidator.getOfficeByIdOrThrow(id);
        office.updateFromRequestDto(dto);

        officeRepository.save(office);

        return officeMapper.toDto(office);
    }

    @Override
    public OfficeResponseDto deactivateOfficeById(@NonNull UUID id) {
        log.info("Desactivando oficina con id: {}", id);
        Office office = officeValidator.getOfficeByIdOrThrow(id);

        if (office.isActive())
            office.setActive(false);

        officeRepository.save(office);
        log.info("Oficina desactivada correctamente: {}", office.getId());

        return officeMapper.toDto(office);
    }

    
}
