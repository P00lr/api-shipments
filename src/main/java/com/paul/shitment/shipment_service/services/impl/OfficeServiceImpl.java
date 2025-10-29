package com.paul.shitment.shipment_service.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
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
import com.paul.shitment.shipment_service.validators.office.OfficeValidation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final OfficeValidation officeValidation;

    @Override
    public List<OfficeResponseDto> getAllOffices() {
        log.info("Verificando existencia de registros");
        List<Office> offices = officeRepository.findAll();

        if(offices.isEmpty()) return List.of();

        log.info("Listando oficinas");
        return OfficeMapper.entitiesToDto(offices);
    }

    @Override
    public PageResponse<OfficeResponseDto> getAllOfficesPaged(int pageNo, int size, String sortBy) {
        log.info("Verificando existencia de registros");

        officeValidation.existsOffices();

        Sort sort = Sort.by(sortBy);

        PageRequest pageRequest = PageRequest.of(pageNo, size, sort);

        Page<Office> officePage = officeRepository.findAll(pageRequest);

        List<OfficeResponseDto> officeList = new ArrayList<>();

        for (Office office : officePage.getContent()) {
            officeList.add(OfficeMapper.entityToDto(office));
        }

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
    public OfficeResponseDto getOffice(UUID id) {
        log.info("Verificando la existencia del registro con id: {}", id);
        Office office = officeValidation.existsOffice(id);

        return OfficeMapper.entityToDto(office);
    }

    @Override
    public OfficeResponseDto createOffice(OfficeRequestDto officeDto) {
        log.info("Verificando registro: {}", officeDto);
        officeValidation.validateOfficeCreate(officeDto);

        Office office = OfficeMapper.dtoToEntity(officeDto);
        log.info("Convirtiendo de dto a entidad");

        try {
            officeRepository.save(office);
            log.info("Office guardado correctamente");
        } catch (DataAccessException e) {
            throw e;
        }

        return OfficeMapper.entityToDto(office);
    }

    @Override
    public OfficeResponseDto updateOffice(UUID id, OfficeRequestDto officeDto) {
        log.info("Validando registro a actualizar {}", officeDto);
        Office office =  officeValidation.validateOfficeUpdate(id, officeDto);

        if (!office.getName().equals(officeDto.name()))
            office.setName(officeDto.name());
        
        if (!office.getAddress().equals(officeDto.address()))
            office.setAddress(officeDto.address());
            
        if (!office.getPhone().equals(officeDto.phone()))
            office.setPhone(officeDto.phone());

        if (office.isActive() != officeDto.active())
            office.setActive(true);
        

        try {
            officeRepository.save(office);
            log.info("Se actualizo el registro correctamente");            
        } catch (DataAccessException e) {
            log.error("Error al actualizar el office {}", officeDto);
            throw e;
        }

        return OfficeMapper.entityToDto(office);

    }

    @Override
    public OfficeResponseDto deactivateOffice(UUID id) {
        log.info("Verificando existencia del registro con id: {}", id);
        Office office = officeValidation.existsOffice(id);

        if (office.isActive() == true)
            office.setActive(false);

        try {
            officeRepository.save(office);
            log.info("Se guardo desactivo correctamente el registro {}", office);
        } catch (Exception e) {
            log.error("Error al actualizar el registro", e);
            throw e;
        }

        return OfficeMapper.entityToDto(office);
    }

}
