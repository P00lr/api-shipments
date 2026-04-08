package com.paul.shitment.shipment_service.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserDetailsService userDetailsService;
    // servicio que sabe cómo buscar al usuario en la base de datos

    private final PasswordEncoder passwordEncoder;
    // encargado de comparar contraseñas de forma segura (hash)

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        // crea el portero encargado del login con usuario y contraseña

        authProvider.setPasswordEncoder(passwordEncoder);
        // le indica al portero cómo validar la contraseña sin verla en texto plano

        return authProvider;
        // portero listo para autenticar usuarios
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
        // administrador principal que decide qué portero usar para autenticar
    }
}
