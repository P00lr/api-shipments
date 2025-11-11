package com.paul.shitment.shipment_service.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.user.UserPasswordUpdateDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserResponseDto;
import com.paul.shitment.shipment_service.dto.user.UserUpdateRequestDto;
import com.paul.shitment.shipment_service.mappers.PersonMapper;
import com.paul.shitment.shipment_service.mappers.UserMapper;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.repositories.PersonRepository;
import com.paul.shitment.shipment_service.repositories.UserRepository;
import com.paul.shitment.shipment_service.services.UserService;
import com.paul.shitment.shipment_service.validators.UserValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    private final UserMapper userMapper;

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public List<UserResponseDto> getAllUsers() {

        log.info("verificando existencia de registros");
        return userMapper.entitiesToDto(userRepository.findAll());

    }

    @Override
    public PageResponse<UserResponseDto> getAllUsersPaged(int pageNo, int size, String sortBy) {
        // Ordenamiento por el campo sortBy, por defecto ASC
        Sort sort = Sort.by(sortBy);

        // Paginación
        PageRequest pageRequest = PageRequest.of(pageNo, size, sort);

        // Obtener usuarios paginados
        Page<AppUser> userPage = userRepository.findAll(pageRequest);

        // Convertir cada User a UserResponseDto
        List<UserResponseDto> dtoList = new ArrayList<>();
        for (AppUser user : userPage.getContent()) {
            dtoList.add(userMapper.entityToDto(user));
        }

        // Crear Page<UserResponseDto> con la misma info de paginación
        return new PageResponse<>(
                dtoList,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isFirst(),
                userPage.isLast());
    }

    @Override
    public UserResponseDto getUserByid(UUID id) {
        log.info("Verificando existe al user con id: {}", id);
        AppUser user = userValidator.getUserByIdOrTrhow(id);
        return userMapper.entityToDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userDto) {
        log.info("Verificando que los datos sean correctos {}", userDto);
        userValidator.validateForCreate(userDto);

        Person person = personMapper.userDtoToEntityPerson(userDto);
        person.setRegistered(true);
        personRepository.save(person);

        AppUser user = userMapper.dtoToEntity(userDto, person);
        String email = userDto.email().trim();
        String password = user.getPassword().trim();

        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);

        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto updateUser(UUID id, UserUpdateRequestDto userDto) {
        log.info("Verificando datos entrante {}", userDto);
        userValidator.validateUserForUpdate(id, userDto);
        AppUser user = userValidator.getUserByIdOrTrhow(id);

        updateUserAttributes(userDto, user);
        userRepository.save(user);

        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto dactivateUser(UUID id) {
        log.info("Verificando el registro con id: {}", id);
        AppUser user = userValidator.getUserByIdOrTrhow(id);

        if (user.isActive() == true) {
            user.setActive(false);
            userRepository.save(user);
        }

        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto updateUserPassword(UUID id, UserPasswordUpdateDto passwordDto) {
        AppUser user = userValidator.validateForUpdatePassword(id, passwordDto);

        user.setPassword(passwordDto.newPassword());
        userRepository.save(user);

        return userMapper.entityToDto(user);
    }

    //METODOS AUXILIARES
    private void updateUserAttributes(UserUpdateRequestDto userDto, AppUser user) {

        if (!userDto.name().equals(user.getPerson().getName()))
            user.getPerson().setName(userDto.name().trim());

        if (!userDto.phone().equals(user.getPerson().getPhone()))
            user.getPerson().setPhone(userDto.phone().trim());

        if (!userDto.ci().equals(user.getPerson().getCi()))
            user.getPerson().setCi(userDto.ci().trim());

        if (!userDto.username().equals(user.getUsername()))
            user.setUsername(userDto.username().trim());

        if (!userDto.email().equals(user.getEmail()))
            user.setEmail(userDto.email().trim());
    }

    
}
