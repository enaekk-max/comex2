package com.aduanas.comex.riesgo_ms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice // ◄ Intercepta de forma automática los errores de riesgo-ms
public class GlobalExceptionHandler {

    // =========================================================================
    // 1. CAPTURA ERRORES DE VALIDACIÓN (Ej: @NotBlank, @Min, @Max del DTO)
    // =========================================================================
    // Si mandan un puntaje mayor a 100 o un idCarga vacío, devuelve campo y motivo.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }

    // =========================================================================
    // 2. MANEJAR RIESGO EXCEPTION (Errores de negocio controlados)
    // =========================================================================
    // Cuando lanzas un "throw new RiesgoException("Riesgo no encontrado")"
    @ExceptionHandler(RiesgoException.class)
    public ResponseEntity<Map<String, Object>> manejarRiesgoException(RiesgoException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("fecha", LocalDateTime.now());
        error.put("error", ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // =========================================================================
    // 3. MANEJAR ERRORES GENERALES INESPERADOS (500 Internal Server Error)
    // =========================================================================
    // Captura caídas imprevistas de código o problemas de conectividad de red.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarException(Exception ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("fecha", LocalDateTime.now());
        error.put("error", "Error interno en el servidor de evaluación de riesgos.");
        error.put("detalle", ex.getMessage()); // Muestra el mensaje técnico en consola/Postman para debug rápido
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}