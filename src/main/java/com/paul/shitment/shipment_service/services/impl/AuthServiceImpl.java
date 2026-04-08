package com.paul.shitment.shipment_service.services.impl;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.paul.shitment.shipment_service.dto.auth.LoginRequestDto;
import com.paul.shitment.shipment_service.dto.auth.LoginResponseDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserResponseDto;
import com.paul.shitment.shipment_service.jwt.JwtUtil;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.security.service.LoginAttemptService;
import com.paul.shitment.shipment_service.services.AuthService;
import com.paul.shitment.shipment_service.services.UserService;
import com.paul.shitment.shipment_service.validators.UserValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    // maneja lógica de usuarios (registro)
    private final UserService userService;

    // valida usuario + contraseña contra Spring Security
    private final AuthenticationManager authenticationManager;

    // genera y valida JWT
    private final JwtUtil jwtUtil;

    // intento de inicio de sesion
    private final LoginAttemptService loginAttemptService;

    private final UserValidator userValidator;


    @Override
    public UserResponseDto registerUser(UserRequestDto userDto) {

        log.info("Registrando nuevo usuario: {}", userDto.username());

        // delega creación al dominio de usuarios
        UserResponseDto user = userService.createUser(userDto);

        log.info("Usuario registrado correctamente");
        return user;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {

        // Extrae el username del request
        String username = request.username();

        // Verifica si el usuario está bloqueado por intentos fallidos
        if (loginAttemptService.isBlocked(username)) {
            throw new LockedException("Usuario bloqueado temporalmente por demasiados intentos");
        }

        try {
            // Intenta autenticar usando Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            request.password()));

            // Si llegó aquí, el login fue exitoso → se limpian intentos fallidos
            loginAttemptService.loginSucceeded(username);

            // Se guarda el usuario autenticado en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Se obtiene la información del usuario autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Se extraen los roles del usuario
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            AppUser user = userValidator.getUserByUsernameOrThrow(username);

            // Se genera el token JWT con id, username y roles
            String token = jwtUtil.generatedToken(
                    user.getId(),
                    userDetails.getUsername(),
                    user.getOffice().getId(),
                    roles);

            // Se devuelve el token al frontend
            return new LoginResponseDto(token);

        } catch (BadCredentialsException ex) {
            // Si las credenciales son incorrectas → registra intento fallido
            loginAttemptService.loginFailed(username);

            // Lanza excepción controlada para respuesta clara
            throw new AuthenticationServiceException(
                    "Usuario o contraseña incorrectos. Verifica tus datos y vuelve a intentar.");
        }
    }

    @Override
    public void logout(String username) {

        log.info("Logout solicitado para usuario: {}", username);

        SecurityContextHolder.clearContext();
        // limpia el contexto de seguridad

        // en JWT stateless no se invalida token en servidor
        // (si luego quieres blacklist → Redis)
    }
}
