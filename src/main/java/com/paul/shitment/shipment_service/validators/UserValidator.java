package com.paul.shitment.shipment_service.validators;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserPasswordUpdateDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserUpdateRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.PersonValidationException;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.exceptions.validation.UserValidationException;
import com.paul.shitment.shipment_service.mappers.UserMapper;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Person;
import com.paul.shitment.shipment_service.repositories.PersonRepository;
import com.paul.shitment.shipment_service.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final PersonValidator personValidator;
    private final UserMapper userMapper;
    private final PersonRepository personRepository;

    public AppUser getUserByIdOrTrhow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro al user con id: " + id));
    }

    public void validateUserExists(UUID id) {
        if(!userRepository.existsById(id))
            throw new ResourceNotFoundException("No existe el user con id: " + id);
    }

    public void validateForCreate(UserRequestDto userDto) {
        PersonRequestDto personRequest = userMapper.userRequestToPersonRequest(userDto);
        personValidator.validateForCreate(personRequest);

        emailUnique(userDto.email());
        usernameUnique(userDto.username());

    }

    public void validateUserForUpdate(UUID id, UserUpdateRequestDto userDto) {
        AppUser user = getUserByIdOrTrhow(id);
        Person person = user.getPerson();

        validatePersonOfUserForUpdate(person.getId(), userDto);

        if (!userDto.username().equals(user.getUsername()) && userRepository.existsByUsername(userDto.username())) {
            throw new UserValidationException("El username ya esta registrado en la db");
        }

        if (!userDto.email().equals(user.getEmail()) && userRepository.existsByEmail(userDto.email()))
            throw new UserValidationException("El email ya esta registrado en la db");
    }

    public void usernameUnique(String username) {

        if (userRepository.existsByUsername(username))
            throw new UserValidationException("El username ya esta en uso");
    }

    public void emailUnique(String email) {
        if (userRepository.existsByEmail(email))
            throw new UserValidationException("El email ya esta en uso");
    }

    public AppUser validateForUpdatePassword(UUID id, UserPasswordUpdateDto passwordDto) {
        AppUser user = getUserByIdOrTrhow(id);
        if (!passwordDto.oldPassword().trim().equals(user.getPassword()))
            throw new UserValidationException("La contraseña es incorrecta");

        return user;
    }

    private void validatePersonOfUserForUpdate(UUID id, UserUpdateRequestDto userDto) {
        Person person = personValidator.getPersonByIdOrThrow(id);

        if (!person.getCi().equals(userDto.ci())
                && personRepository.existsByCi(userDto.ci()))
            throw new PersonValidationException("El CI ya esta registrado");

        if (!person.getPhone().equals(userDto.phone())
                && personRepository.existsByPhone(userDto.phone())) {
            throw new PersonValidationException("El celular ya fue registrado");
        }
    }

}
