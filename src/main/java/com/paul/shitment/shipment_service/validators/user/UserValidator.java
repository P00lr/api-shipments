package com.paul.shitment.shipment_service.validators.user;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.exceptions.validation.UserValidationException;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.repositories.UserRepository;
import com.paul.shitment.shipment_service.validators.person.PersonValidator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final PersonValidator personValidator;


    public void existsUsers() {
        if (userRepository.count() == 0) {
            throw new ResourceNotFoundException("No se encontraron registros");
        }
    }

    public AppUser existsUser(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encontro al user con id: " + id));
    }

    public void validateCreateUser(UserRequestDto userDto) {
        notNull(userDto);

        if (userDto.name() == null)
            throw new UserValidationException("El nombre es obligatorio");

        if (userDto.password() == null) 
            throw new UserValidationException("El password es obligatorio");
            
            personValidator.ciUnique(userDto.ci());
            personValidator.phoneUnique(userDto.phone());
            usernameUnique(userDto.username());
            

        emailUnique(userDto.email());
    }   

    public AppUser validiateUserUpdate(UUID id, UserRequestDto userDto) {
        notNull(userDto);
        AppUser user = existsUser(id);

        if(!userDto.username().equals(user.getUsername()) && userRepository.existsByUsername(userDto.username())) {
            throw new UserValidationException("El username ya esta registrado en la db");
        }

        if(!userDto.email().equals(user.getEmail()) && userRepository.existsByEmail(userDto.email()))
            throw new UserValidationException("El email ya esta registrado en la db");

        return user;
    }

    private void notNull(UserRequestDto userDto) {
        if (userDto == null)
            throw new UserValidationException("El objeto no puede ser null");
    }

    public void usernameUnique(String username) {
        if (username == null) 
            throw new UserValidationException("El username es obligatorio");
        

        if (userRepository.existsByUsername(username)) 
            throw new UserValidationException("El username ya esta en uso");
    }

    public void emailUnique(String email) {
        if (email == null) {
            throw new UserValidationException("El email es obligatorio");
        }

        if (userRepository.existsByEmail(email)) 
            throw new UserValidationException("El email ya esta en uso");
    }



}
