package com.paul.shitment.shipment_service.security.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paul.shitment.shipment_service.dto.ErrorResponseDto;
import com.paul.shitment.shipment_service.security.service.impl.RateLimiterService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimiterService rateLimiterService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        String endpoint = request.getRequestURI();

        boolean allowed = rateLimiterService.allowRequest(ip, endpoint);

        if (!allowed) {

            ErrorResponseDto error = new ErrorResponseDto(
                    HttpStatus.TOO_MANY_REQUESTS.name(),
                    "Demasiadas peticiones",
                    LocalDateTime.now(),
                    HttpStatus.TOO_MANY_REQUESTS.value(),
                    request.getRequestURI());

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            response.getWriter()
                    .write(objectMapper.writeValueAsString(error));

            return;
        }

        filterChain.doFilter(request, response);
    }

}
