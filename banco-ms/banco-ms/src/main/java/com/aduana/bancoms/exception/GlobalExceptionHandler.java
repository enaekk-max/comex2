package com.aduana.bancoms.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ CAPTURA: Atrapa cualquier RuntimeException (como fondos insuficientes o timeout de Feign)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        // Retorna un HTTP 400 Bad Request con el mensaje limpio para el cliente
        return ResponseEntity.badRequest().body(Map.of(
                "status", "ERROR_PROCESAMIENTO",
                "error", ex.getMessage()
        ));
    }
}