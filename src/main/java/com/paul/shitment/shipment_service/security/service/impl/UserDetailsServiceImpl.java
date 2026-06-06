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
import com.paul.shitment.shipment_service.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional // permite cargar roles con lazy sin errores de sesión
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // convierte los roles del usuario en permisos que Spring Security entiende
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        //devuelve User de spirng por que tambien usa el contrato de UserDetails
        return User.builder()
        // nombre del usuario → nombre en el boleto
                .username(user.getUsername())

                // contraseña cifrada → verificación segura de identidad
                .password(user.getPassword())

                // permisos del usuario → a qué áreas puede acceder
                .authorities(authorities)

                // si el usuario está inactivo, el boleto queda automáticamente inválido
                .disabled(!user.isActive())
                
                // usuario de seguridad listo para autenticación
                .build();
    }
}
