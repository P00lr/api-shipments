package com.paul.shitment.shipment_service.validators.office;


import java.util.UUID;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.OfficeValidationException;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.models.entities.Office;
import com.paul.shitment.shipment_service.repositories.OfficeRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OfficeValidation {

    private final OfficeRepository officeRepository;

    public void existsOffices() {
        if(officeRepository.count() == 0) {
            throw new ResourceNotFoundException("No existe registro de offices");
        }
    }

    public Office existsOffice(UUID id) {
        return officeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontro el registro de la oficina con id: " + id));
    }

    public void validateOfficeCreate(OfficeRequestDto officeDto) {
        notNull(officeDto);
        validateFields(officeDto);
        nameUnique(officeDto.name());
        phoneUnique(officeDto.phone());
        addressUnique(officeDto.address());

    }

    public Office validateOfficeUpdate(UUID id, OfficeRequestDto officeDto) {
        notNull(officeDto);
        Office office = existsOffice(id);

        if(!officeDto.name().equals(office.getName()) && officeRepository.existsByName(officeDto.name()))
            throw new OfficeValidationException("El nombre de la oficina ya existe");

        if(!officeDto.address().equals(office.getAddress()) && officeRepository.existsByAddress(officeDto.address()))
            throw new OfficeValidationException("La direccion de la oficina ya existe");
        
        if(!officeDto.phone().equals(office.getPhone()) && officeRepository.existsByPhone(officeDto.phone()))
            throw new OfficeValidationException("El numero de celular de la oficina ya existe");

        return office;
    }

    private void notNull(OfficeRequestDto officeDto) {
        if(officeDto == null) {
            throw new OfficeValidationException("El objeto de la oficina no puede estar vacio");
        }
    }

    private void nameUnique(String name) {
        if (officeRepository.existsByName(name)) {
            throw new OfficeValidationException("El nombre de la oficina ya existe");
        }
    }
    private void phoneUnique(String phone) {
        if (officeRepository.existsByPhone(phone)) {
            throw new OfficeValidationException("El celular de la oficina ya existe");
        }
    }

    private void addressUnique(String address) {
        if (officeRepository.existsByAddress(address)) {
            throw new OfficeValidationException("La direccion de la oficina ya existe");
        }
    }

    private void validateFields(OfficeRequestDto officeDto) {
        if(officeDto.name() == null || officeDto.address() == null || officeDto.phone() == null)
            throw new OfficeValidationException("Completa todos los campos por favor");
    }

}
