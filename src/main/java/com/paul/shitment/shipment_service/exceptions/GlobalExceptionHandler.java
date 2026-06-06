package com.paul.shitment.shipment_service.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.paul.shitment.shipment_service.dto.ErrorResponseDto;
import com.paul.shitment.shipment_service.exceptions.validation.OfficeValidationException;
import com.paul.shitment.shipment_service.exceptions.validation.PersonValidationException;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceAlreadyExistsException;
import com.paul.shitment.shipment_service.exceptions.validation.ResourceNotFoundException;
import com.paul.shitment.shipment_service.exceptions.validation.ShipmentValidationException;
import com.paul.shitment.shipment_service.exceptions.validation.TransportCooperativeException;
import com.paul.shitment.shipment_service.exceptions.validation.UserValidationException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlerResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.info("Recurso no encontrado en {}: {}", request.getRequestURI(), ex.getMessage());

        ErrorResponseDto error = new ErrorResponseDto();

        error.setMessage(ex.getMessage());
        error.setError(HttpStatus.NOT_FOUND.name());
        error.setDate(LocalDateTime.now());
        error.setCode(HttpStatus.NOT_FOUND.value());
        error.setPath(request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setError(HttpStatus.BAD_REQUEST.name());
        errorResponse.setMessage(errors);
        errorResponse.setDate(LocalDateTime.now());
        errorResponse.setCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersonValidationException.class)
    public ResponseEntity<ErrorResponseDto> handlerPersonValidationException(
            PersonValidationException ex, HttpServletRequest request) {
        ErrorResponseDto error = new ErrorResponseDto();

        error.setError(HttpStatus.CONFLICT.name());
        error.setMessage(ex.getMessage());
        error.setDate(LocalDateTime.now());
        error.setPath(request.getRequestURI());
        error.setCode(HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OfficeValidationException.class)
    public ResponseEntity<ErrorResponseDto> handlerOfficeValidationException(
            OfficeValidationException ex, HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto();

        error.setMessage(ex.getMessage());
        error.setCode(HttpStatus.CONFLICT.value());
        error.setError(HttpStatus.CONFLICT.name());
        error.setPath(request.getRequestURI());
        error.setDate(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ErrorResponseDto> handlerUserValidationException(
            UserValidationException ex, HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto();

        error.setMessage(ex.getMessage());
        error.setCode(HttpStatus.CONFLICT.value());
        error.setError(HttpStatus.CONFLICT.name());
        error.setPath(request.getRequestURI());
        error.setDate(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ShipmentValidationException.class)
    public ResponseEntity<ErrorResponseDto> handlerShipmentValidationException(
            ShipmentValidationException ex, HttpServletRequest request) {
        ErrorResponseDto error = new ErrorResponseDto();
        error.setMessage(ex.getMessage());
        error.setCode(HttpStatus.CONFLICT.value());
        error.setError(HttpStatus.CONFLICT.name());
        error.setPath(request.getRequestURI());
        error.setDate(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TransportCooperativeException.class)
    public ResponseEntity<ErrorResponseDto> handleTransportCooperativeException(
            TransportCooperativeException ex, HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handlerResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                ex.getMessage(),
                HttpStatus.CONFLICT.name(),
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        log.warn("JSON mal formado o tipo inválido: {}", ex.getMessage());

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.BAD_REQUEST.name(),
                "El formato del request es inválido",
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado: ", ex);

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "Ocurrió un error inesperado",
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationServiceException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(
            AuthenticationServiceException ex,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.name(),
                ex.getMessage(),
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponseDto> handleLockedException(
            LockedException ex,
            HttpServletRequest request) {

        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.LOCKED.name(),
                ex.getMessage(),
                LocalDateTime.now(),
                HttpStatus.LOCKED.value(),
                request.getRequestURI());

        return new ResponseEntity<>(error, HttpStatus.LOCKED);
    }
}
