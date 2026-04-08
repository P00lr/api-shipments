package com.paul.shitment.shipment_service.security.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.shitment.shipment_service.dto.ErrorResponseDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

     // convierte objetos Java en JSON
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

         // 403 → el usuario está autenticado pero no tiene permisos
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // la respuesta será JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // mensaje claro indicando falta de permisos
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.FORBIDDEN.name(),
                "No tienes privilegios necesarios para realizar esta accion",
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                request.getRequestURI());

         // envía el error al cliente en formato JSON
        objectMapper.writeValue(response.getOutputStream(), error);
    }
}
