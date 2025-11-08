package com.paul.shitment.shipment_service.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;
import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeResponse;
import com.paul.shitment.shipment_service.mappers.TransportCooperativeMapper;
import com.paul.shitment.shipment_service.models.entities.TransportCooperative;
import com.paul.shitment.shipment_service.repositories.TransportCooperativeRepository;
import com.paul.shitment.shipment_service.services.TransportCooperativeService;
import com.paul.shitment.shipment_service.validators.TransportCooperativeValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransportCooperativeImpl implements TransportCooperativeService {

    private final TransportCooperativeRepository cooperativeRepository;
    private final TransportCooperativeValidator cooperativeValidator;
    private final TransportCooperativeMapper cooperativeMapper;

    @Override
    public List<TransportCooperativeResponse> getAllCooperatives() {
        log.info("Obteniendo todas las cooperativas");
        return cooperativeMapper.entitiesToDtos(cooperativeRepository.findAll());
    }

    @Override
    public TransportCooperativeResponse getCooperativeById(UUID id) {
        log.info("Obteniendo cooperativa con ID: {}", id);
        return cooperativeMapper.entityToDto(cooperativeValidator.getCooperativeByIdOrThrow(id));
    }

    @Override
    @Transactional
    public TransportCooperativeResponse createCooperative(TransportCooperativeRequest request) {
        log.info("Creando nueva cooperativa: {}", request);
        cooperativeValidator.validateForCreate(request);

        TransportCooperative cooperative = cooperativeMapper.dtoToEntity(request);
        cooperativeRepository.save(cooperative);

        log.info("Cooperativa creada con ID: {}", cooperative.getId());
        return cooperativeMapper.entityToDto(cooperative);
    }

    @Override
    @Transactional
    public TransportCooperativeResponse updateCooperative(UUID id, TransportCooperativeRequest request) {
        log.info("Actualizando cooperativa con ID: {}", id);

        TransportCooperative existing = cooperativeValidator.getCooperativeByIdOrThrow(id);

        cooperativeValidator.validateForUpdate(id, request);

        cooperativeMapper.updateEntity(existing, request);

        cooperativeRepository.save(existing);
        log.info("Cooperativa actualizada con éxito: {}", id);

        return cooperativeMapper.entityToDto(existing);
    }

    @Override
    @Transactional
    public TransportCooperativeResponse deactivateCooperative(UUID id) {
        log.info("Desactivando cooperativa con ID: {}", id);

        TransportCooperative cooperative = cooperativeValidator.getCooperativeByIdOrThrow(id);

        if (!cooperative.isActive()) {
            log.info("Cooperativa ya estaba desactivada: {}", id);
            return cooperativeMapper.entityToDto(cooperative);
        }

        cooperative.setActive(false);

        cooperativeRepository.save(cooperative);
        log.info("Cooperativa desactivada con éxito: {}", id);

        return cooperativeMapper.entityToDto(cooperative);
    }

}
