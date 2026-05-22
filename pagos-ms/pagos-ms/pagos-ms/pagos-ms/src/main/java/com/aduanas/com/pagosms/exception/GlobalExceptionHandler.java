package com.aduanas.com.pagosms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice // ◄ ¡Obligatorio! Captura y sanitiza los errores de todos los controladores de pagos-ms
public class GlobalExceptionHandler {

    // =========================================================================
    // 1. CAPTURA ERRORES DE VALIDACIÓN (Ej: @NotBlank, @NotNull, @Positive)
    // =========================================================================
    // Si mandan un monto inválido o un idCarga nulo, devuelve el mapa exacto del campo y su porqué.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }

    // =========================================================================
    // 2. CAPTURA ERRORES DE NEGOCIO CONTROLADOS (RuntimeException)
    // =========================================================================
    // Captura tus "throw new RuntimeException(...)" del PagosService (Ej: "La carga no registra impuestos").
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("fecha", LocalDateTime.now());
        cuerpo.put("error", ex.getMessage());
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(cuerpo);
    }

    // =========================================================================
    // 3. CAPTURA ERRORES INESPERADOS DEL SERVIDOR (Pifias de Código Interno)
    // =========================================================================
    // Si explota un NullPointerException o algo feo en el código, esto evita mostrarle las tripas de Java al cliente.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("fecha", LocalDateTime.now());
        cuerpo.put("error", "Ocurrió un error interno en el servidor de pagos. Intente más tarde.");
        cuerpo.put("detalle", ex.getMessage()); // Te sirve para mirar la consola rápido
        cuerpo.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cuerpo);
    }
}