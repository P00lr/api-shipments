package com.paul.shitment.shipment_service.security.service.impl;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.validators.UserValidator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserResolver {
    private final UserValidator userValidator;

    /**
     * Retorna el AppUser autenticado desde el JWT del contexto de seguridad.
     * Lanza excepción si no hay sesión activa.
     */
    public AppUser resolve() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException(
                    "No hay una sesión activa.");
        }

        String username = authentication.getName();
        return userValidator.getUserByUsernameOrThrow(username);
    }

    /**
     * Retorna solo el username sin ir a base de datos.
     * Útil para logs o validaciones ligeras.
     */
    public String resolveUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
