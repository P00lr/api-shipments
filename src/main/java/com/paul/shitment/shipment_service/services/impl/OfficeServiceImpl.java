package com.paul.shitment.shipment_service.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.dto.office.OfficeResponseDto;
import com.paul.shitment.shipment_service.mappers.OfficeMapper;
import com.paul.shitment.shipment_service.models.entities.Office;
import com.paul.shitment.shipment_service.repositories.OfficeRepository;
import com.paul.shitment.shipment_service.services.OfficeService;
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

    @Override
    public List<OfficeResponseDto> getAllOffices() {
        log.info("Obteniendo todas las oficinas");
        List<Office> offices = officeRepository.findAll();
        return offices.stream()
                .map(officeMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<OfficeResponseDto> getAllOfficesPaged(int pageNo, int size, String sortBy) {
        log.info("Obteniendo oficinas paginadas: page {}, size {}, sort {}", pageNo, size, sortBy);
        Page<Office> officePage = officeRepository.findAll(PageRequest.of(pageNo, size, Sort.by(sortBy)));

        List<OfficeResponseDto> officeList = officePage.getContent()
                .stream()
                .map(officeMapper::entityToDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                officeList,
                officePage.getNumber(),
                officePage.getSize(),
                officePage.getTotalElements(),
                officePage.getTotalPages(),
                officePage.isFirst(),
                officePage.isLast());
    }

    @Override
    public OfficeResponseDto getOfficeById(UUID id) {
        log.info("Obteniendo oficina con id: {}", id);
        Office office = officeValidator.getOfficeByIdOrThrow(id);
        return officeMapper.entityToDto(office);
    }

    @Override
    public OfficeResponseDto createOffice(OfficeRequestDto dto) {
        officeValidator.validateForCreation(dto);

        Office office = officeMapper.dtoToEntity(dto);
        officeRepository.save(office);
        return officeMapper.entityToDto(office);
    }

    @Override
    public OfficeResponseDto updateOffice(UUID id, OfficeRequestDto dto) {
        officeValidator.validateForUpdate(id, dto);

        Office office = officeValidator.getOfficeByIdOrThrow(id);
        officeMapper.updateEntity(office, dto);

        officeRepository.save(office);
        
        return officeMapper.entityToDto(office);
    }

    @Override
    public OfficeResponseDto deactivateOfficeById(UUID id) {
        log.info("Desactivando oficina con id: {}", id);
        Office office = officeValidator.getOfficeByIdOrThrow(id);

        if (office.isActive())
            office.setActive(false);

        officeRepository.save(office);
        log.info("Oficina desactivada correctamente: {}", office.getId());

        return officeMapper.entityToDto(office);
    }
}
