package com.aduanas.comex.notificacion_ms.controller;

import com.aduanas.comex.notificacion_ms.dto.NotificacionRequestDTO;
import com.aduanas.comex.notificacion_ms.dto.NotificacionResponseDTO;
import com.aduanas.comex.notificacion_ms.service.NotificacionService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    // CREAR NOTIFICACIÓN
    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crear(
            @Valid @RequestBody NotificacionRequestDTO dto
    ) {
        return ResponseEntity.ok(notificacionService.crear(dto));
    }

    // LISTAR TODAS LAS NOTIFICACIONES
    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> listar() {
        return ResponseEntity.ok(notificacionService.listar());
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(notificacionService.buscarPorId(id));
    }

    // BUSCAR POR ESTADO
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<NotificacionResponseDTO>> buscarPorEstado(
            @PathVariable String estado
    ) {
        return ResponseEntity.ok(notificacionService.buscarPorEstado(estado));
    }

    // ACTUALIZAR NOTIFICACIÓN
    @PutMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody NotificacionRequestDTO dto
    ) {
        return ResponseEntity.ok(notificacionService.actualizar(id, dto));
    }

    // ELIMINAR NOTIFICACIÓN
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        notificacionService.eliminar(id);
        return ResponseEntity.ok().build();
    }

    // 🔥 REFACTORIZADO Y PARCHADO: Sincronizado con idCarga para el flujo asíncrono
    @PostMapping("/enviar-alerta")
    public ResponseEntity<String> recibirAlerta(
            @Valid @RequestBody NotificacionRequestDTO dto
    ) {
        // ✅ SOLUCIONADO: Cambiado dto.getCargaId() por dto.getIdCarga()
        log.info("📢 [ALERTA] JSON recibido exitosamente. Carga ID: {}, Destinatario: {}", dto.getIdCarga(), dto.getDestinatario());

        // ✅ SOLUCIONADO: Pasando la variable unificada idCarga al hilo en segundo plano
        notificacionService.enviarEmailAsincrono(dto.getIdCarga(), dto.getDestinatario(), dto.getMensaje());

        // Retorna inmediatamente un 200 OK liberando al cliente
        return ResponseEntity.ok("Notificación recibida en formato JSON correctamente y procesándose en segundo plano.");
    }
}