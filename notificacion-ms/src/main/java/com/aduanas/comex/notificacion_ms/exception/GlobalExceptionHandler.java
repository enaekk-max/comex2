package com.aduanas.comex.notificacion_ms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ======================================================
    // 1. ERROR PERSONALIZADO (NotificacionException)
    // ======================================================
    @ExceptionHandler(NotificacionException.class)
    public ResponseEntity<Map<String, String>> manejarNotificacionException(NotificacionException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // ======================================================
    // 2. ERROR DE VALIDACIÓN DE DTOs (Campos obligatorios @NotBlank/@NotNull)
    // ======================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        // Mapea cada campo que falló con su respectivo mensaje personalizado
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String nombreCampo = ((FieldError) error).getField();
            String mensajeError = error.getDefaultMessage();
            errores.put(nombreCampo, mensajeError);
        });

        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST); // Retorna 400 Bad Request
    }

    // ======================================================
    // 3. ERROR GENERAL (Cualquier otra falla inesperada)
    // ======================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("mensaje", "Error interno del servidor: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}