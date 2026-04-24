package com.paul.shitment.shipment_service.validators;

import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PersonValidator personValidator;
    private final UserMapper userMapper;
    private final PersonRepository personRepository;

    public AppUser getUserByIdOrTrhow(@NonNull UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro al user con id: " + id));
    }

    public AppUser getUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro al user con id: " + username));
    }

    public void validateUserExists(@NonNull UUID id) {
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("No existe el user con id: " + id);
    }

    public void validateForCreate(UserRequestDto userDto) {
        PersonRequestDto personRequest = userMapper.userRequestToPersonRequest(userDto);
        personValidator.validateForCreate(personRequest);

        emailUnique(userDto.email());
        usernameUnique(userDto.username());

    }

    public void validateUserForUpdate(@NonNull UUID id, UserUpdateRequestDto userDto) {
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

    public AppUser validateForUpdatePassword(@NonNull UUID id, UserPasswordUpdateDto passwordDto) {
        AppUser user = getUserByIdOrTrhow(id);

        if (!passwordEncoder.matches(passwordDto.oldPassword().trim(), user.getPassword()))
            throw new UserValidationException("La contraseña es completamente incorrecta");

        return user;
    }

    private void validatePersonOfUserForUpdate(@NonNull UUID id, UserUpdateRequestDto userDto) {
        Person person = personValidator.getPersonByIdOrThrow(id);

        if (!person.getDocumentNumber().equals(userDto.person().documentNumber())
                && personRepository.existsByDocumentNumber(userDto.person().documentNumber()))
            throw new PersonValidationException("El CI ya esta registrado");

        if (!person.getPhone().equals(userDto.person().phone())
                && personRepository.existsByPhone(userDto.person().phone())) {
            throw new PersonValidationException("El celular ya fue registrado");
        }
    }

}
