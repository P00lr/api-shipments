package com.paul.shitment.shipment_service.security.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.shitment.shipment_service.dto.ErrorResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String message = authException.getMessage();

        if (message == null) {
            message = "No has iniciado sesión";
        }

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.name(),
                message,
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI());

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

}
