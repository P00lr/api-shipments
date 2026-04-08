package com.paul.shitment.shipment_service.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.paul.shitment.shipment_service.exceptions.validation.JwtValidationException;
import com.paul.shitment.shipment_service.security.exception.RestAuthenticationEntryPoint;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // encargado de manejar y validar los boletos (tokens)
    private final UserDetailsService userDetailsService; // obtiene la información del usuario
    private final RestAuthenticationEntryPoint entryPoint; // maneja accesos no autorizados

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Saltar validación de JWT para endpoints públicos (login, register, logout)
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request); // saca el token del header Authorization

        try {
            // si hay token y el usuario aún no está autenticado en esta request
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtUtil.getUsernameFromToken(token);
                UUID officeId = jwtUtil.getOfficeIdFromToken(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.isTokenValid(token, userDetails)) {
                    // token válido → crear autenticación en el contexto de seguridad
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    Map<String, Object> details = new HashMap<>();
                    details.put("officeId", officeId);
                    details.put("request", new WebAuthenticationDetailsSource().buildDetails(request));

                    authToken.setDetails(details);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    throw new JwtValidationException("Tu token es inválido o ha expirado");
                }
            }

            // dejar pasar la request al siguiente filtro o endpoint
            filterChain.doFilter(request, response);

        } catch (JwtValidationException ex) {
            log.warn("JWT error de validación: {}", ex.getMessage());
            SecurityContextHolder.clearContext();

            // solo se dispara cuando hay token inválido
            entryPoint.commence(
                    request,
                    response,
                    new AuthenticationServiceException(ex.getMessage()));
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        // toma el header Authorization
        if (header == null || !header.startsWith("Bearer "))
            return null;
        // si no hay boleto o no tiene el formato correcto, devuelve null
        return header.substring(7);
        // quita "Bearer " y devuelve el token limpio
    }
}
