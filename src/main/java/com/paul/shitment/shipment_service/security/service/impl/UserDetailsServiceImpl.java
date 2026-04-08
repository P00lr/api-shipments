package com.paul.shitment.shipment_service.security.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Role;
import com.paul.shitment.shipment_service.validators.UserValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserValidator userValidator;
    // componente encargado de buscar y validar al usuario en la base de datos

    @Override
    @Transactional // permite cargar roles con lazy sin errores de sesión
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userValidator.getUserByUsernameOrThrow(username);
        // busca al usuario por su nombre verifica que exista en el sistema

        Set<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        // convierte los roles del usuario en permisos que Spring Security entiende

        //devuelve User de spirng por que tambien usa el contrato de UserDetails
        return User.builder()
                .username(user.getUsername())
                // nombre del usuario → nombre en el boleto
                .password(user.getPassword())
                // contraseña cifrada → verificación segura de identidad
                .authorities(authorities)
                // permisos del usuario → a qué áreas puede acceder
                .disabled(!user.isActive())
                // si el usuario está inactivo, el boleto queda automáticamente inválido
                .build();
        // usuario de seguridad listo para autenticación
    }
}
