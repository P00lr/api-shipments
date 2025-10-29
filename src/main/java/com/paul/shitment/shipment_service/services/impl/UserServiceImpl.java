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
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserResponseDto;
import com.paul.shitment.shipment_service.mappers.PersonMapper;
import com.paul.shitment.shipment_service.mappers.UserMapper;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.repositories.PersonRepository;
import com.paul.shitment.shipment_service.repositories.UserRepository;
import com.paul.shitment.shipment_service.services.UserService;
import com.paul.shitment.shipment_service.validators.user.UserValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PersonRepository personRepository;

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public List<UserResponseDto> getAllUsers() {

        log.info("verificando existencia de registros {}", userRepository.count());
        List<AppUser> users = userRepository.findAll();

        if(users.isEmpty()) return List.of();
        log.info("Obteniendo los registros de la db {}", users.size());

        return UserMapper.entitiesToDto(users);

    }

    @Override
    public PageResponse<UserResponseDto> getAllUsersPaged(int pageNo, int size, String sortBy) {
        log.info("Verificando existencia de registros {}", userRepository.count());
        userValidator.existsUsers();

        // Ordenamiento por el campo sortBy, por defecto ASC
        Sort sort = Sort.by(sortBy);

        // Paginación
        PageRequest pageRequest = PageRequest.of(pageNo, size, sort);

        // Obtener usuarios paginados
        Page<AppUser> userPage = userRepository.findAll(pageRequest);

        // Convertir cada User a UserResponseDto
        List<UserResponseDto> dtoList = new ArrayList<>();
        for (AppUser user : userPage.getContent()) {
            dtoList.add(UserMapper.entityToDto(user));
        }

        // Crear Page<UserResponseDto> con la misma info de paginación
        return new PageResponse<>(
            dtoList, 
            userPage.getNumber(), 
            userPage.getSize(), 
            userPage.getTotalElements(),
            userPage.getTotalPages(),
            userPage.isFirst(), 
            userPage.isLast()
        );
    }

    @Override
    public UserResponseDto getUser(UUID id) {
        log.info("Verificando existe al user con id: {}", id);
        AppUser user = userValidator.existsUser(id);

        return UserMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {
        log.info("Verificando que los datos sean correctos");
        userValidator.validateCreateUser(userDto);

        Person person = PersonMapper.userDtoToEntityPerson(userDto);
        person.setRegistered(true);

        try {
            personRepository.save(person);
            log.info("Se guardo correctamente la persona: {}", person);
        } catch (DataAccessException e) {
            log.error("Error al guardar a la persona {}", e);
        }

        AppUser user = UserMapper.dtoToEntity(userDto, person);

        try {
            userRepository.save(user);
            log.info("Se guardo correctamente el user");
        } catch (DataAccessException e) {
            log.error("Error al guardar el usuario {}", user);
            throw e;
        }

        return UserMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto updateUser(UUID id, UserRequestDto userDto) {
        log.info("Verificando datos entrandte {}", userDto);
        AppUser user = userValidator.validiateUserUpdate(id, userDto);

        user.setUsername(userDto.username());
        user.setPassword(userDto.password());
        user.setEmail(userDto.email());

        try {
            userRepository.save(user);
            log.info("Se guardo correctamente el user: {}", user);
        } catch (DataAccessException e) {
            log.error("Eror al registrar en la db {}", e);
            throw e;
        }

        return UserMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto dactivateUser(UUID id) {
        log.info("Verificando el registro con id: {}", id);
        AppUser user = userValidator.existsUser(id);

        if (user.isActive() == true) {
            user.setActive(false);
        }

        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            log.error("Error al guardar el user desactivado {}", e);
            throw e;
        }
        ;

        return UserMapper.entityToDto(user);
    }

}
