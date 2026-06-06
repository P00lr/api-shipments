package com.paul.shitment.shipment_service.services.impl;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.paul.shitment.shipment_service.dto.auth.LoginRequestDto;
import com.paul.shitment.shipment_service.dto.auth.LoginResponseDto;
import com.paul.shitment.shipment_service.dto.auth.UserLoginRequest;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserResponseDto;
import com.paul.shitment.shipment_service.jwt.JwtUtil;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Role;
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

        UserResponseDto user = userService.createUser(userDto);

        log.info("Usuario registrado correctamente");
        return user;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request, String clientIp) {

        checkIfBlocked(clientIp); // bloquea por IP

        Authentication auth = authenticate(clientIp, request.username().trim(), request.password());

        return buildLoginResponse(request.username().trim(), auth);
    }

    private void checkIfBlocked(String username) {
        boolean blocked = loginAttemptService.isBlocked(username);
        log.info("Usuario [{}] bloqueado: {}", username, blocked);
        if (blocked) {
            throw new LockedException(
                    "Usuario bloqueado temporalmente. Intenta en unos minutos.");
        }
    }

    // ─── Paso 2: Autenticar con Spring Security ───────────────────────────────
    private Authentication authenticate(String clientIp, String username, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            loginAttemptService.loginSucceeded(clientIp);
            return auth;

        } catch (BadCredentialsException ex) {
            loginAttemptService.loginFailed(clientIp); // registra fallo por IP
            throw new AuthenticationServiceException(
                    "Usuario o contraseña incorrectos. Verifica tus datos y vuelve a intentar.");
        }
    }

    // ─── Paso 3: Construir respuesta ──────────────────────────────────────────
    private LoginResponseDto buildLoginResponse(String username, Authentication authentication) {
        AppUser user = userValidator.getUserByUsernameOrThrow(username);

        // Fuente única de verdad: los roles vienen de AppUser
        Set<String> roles = extractRoles(user);

        String token = jwtUtil.generatedToken(
                user.getId(),
                username,
                user.getOffice().getId(),
                new ArrayList<>(roles));

        UserLoginRequest userData = new UserLoginRequest(username, roles);

        return new LoginResponseDto(token, userData);
    }

    // ─── Utilidad: extracción de roles (única fuente de verdad) ──────────────
    private Set<String> extractRoles(AppUser user) {
        return user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
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
