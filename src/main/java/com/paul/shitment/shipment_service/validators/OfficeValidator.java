package com.paul.shitment.shipment_service.validators;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.OfficeValidationException;
import com.paul.shitment.shipment_service.models.entities.Office;
import com.paul.shitment.shipment_service.repositories.OfficeRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OfficeValidator {

    private final OfficeRepository officeRepository;

    public Office getOfficeByIdOrThrow(UUID id) {
        return officeRepository.findById(id)
            .orElseThrow(() -> new ResourceAccessException("No se encontro la oficina con id: " + id));
    }

    public void validateOfficeExists(UUID id) {
        if (!officeRepository.existsById(id)) {
            throw new ResourceAccessException("No se encontro la oficina con id: " + id);
        }
    }

    public void validateForCreation(OfficeRequestDto officeDto) {
        validateNameUniqueness(officeDto.name());
        validatePhoneUniqueness(officeDto.phone());
    }

    public void validateForUpdate(UUID id, OfficeRequestDto officeDto) {
        Office existingOffice = getOfficeByIdOrThrow(id);

        validateNameUniquenessOnUpdate(existingOffice.getName(), officeDto.name());
        validatePhoneUniquenessOnUpdate(existingOffice.getPhone(), officeDto.phone());
    }



    //METODOS AUXILIARES
    private void validateNameUniqueness(String name) {
        if (officeRepository.existsByName(name)) {
            throw new OfficeValidationException("Ya existe una oficina con el nombre: " + name);
        }

    }

    private void validatePhoneUniqueness(String phone) {
        if (officeRepository.existsByPhone(phone)) {
            throw new OfficeValidationException("Ya existe una oficina con el telefono: " + phone);
        }
    }

    public void validateNameUniquenessOnUpdate(String nameCurrent, String nameNew) {
        if (!nameCurrent.equals(nameNew) && officeRepository.existsByName(nameNew)) {
            throw new OfficeValidationException("Ya existe una oficina con el nombre: " + nameNew);
        }
    }

    public void validatePhoneUniquenessOnUpdate(String phoneCurrent, String phoneNew) {
        if (!phoneCurrent.equals(phoneNew) && officeRepository.existsByPhone(phoneNew)) {
            throw new OfficeValidationException("Ya existe una oficina con el telefono: " + phoneNew);
        }
    }



}
