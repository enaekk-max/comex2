package com.aduanas.comex.notificacion_ms.service;

import com.aduanas.comex.notificacion_ms.dto.NotificacionRequestDTO;
import com.aduanas.comex.notificacion_ms.dto.NotificacionResponseDTO;
import com.aduanas.comex.notificacion_ms.entity.Notificacion;
import com.aduanas.comex.notificacion_ms.enums.EstadoNotificacion;
import com.aduanas.comex.notificacion_ms.enums.TipoNotificacion;
import com.aduanas.comex.notificacion_ms.repository.NotificacionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificacionService {

    private final NotificacionRepository repository;

    // CREAR NOTIFICACIÓN (SÍNCRONO)
    public NotificacionResponseDTO crear(NotificacionRequestDTO dto) {
        // ✅ SOLUCIONADO: Refactorizado usando el Builder de la Entidad e idCarga
        Notificacion notificacion = Notificacion.builder()
                .idCarga(dto.getIdCarga())
                .mensaje(dto.getMensaje())
                .destinatario(dto.getDestinatario())
                .tipo(TipoNotificacion.valueOf(dto.getTipo()))
                .estado(EstadoNotificacion.valueOf(dto.getEstado()))
                .fechaNotificacion(LocalDateTime.now())
                .build();

        Notificacion guardada = repository.save(notificacion);
        return convertirResponseDTO(guardada);
    }

    // LISTAR TODAS LAS NOTIFICACIONES
    public List<NotificacionResponseDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::convertirResponseDTO)
                .toList();
    }

    // BUSCAR NOTIFICACIÓN POR ID
    public NotificacionResponseDTO buscarPorId(Long id) {
        Notificacion entity = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Notificación no encontrada"));

        return convertirResponseDTO(entity);
    }

    // BUSCAR POR ESTADO
    public List<NotificacionResponseDTO> buscarPorEstado(String estado) {
        List<Notificacion> lista = repository.findByEstado(EstadoNotificacion.valueOf(estado));

        return lista.stream()
                .map(this::convertirResponseDTO)
                .toList();
    }

    // ACTUALIZAR NOTIFICACIÓN
    public NotificacionResponseDTO actualizar(Long id, NotificacionRequestDTO dto) {
        Notificacion entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        // ✅ SOLUCIONADO: Sincronizado con idCarga
        entity.setIdCarga(dto.getIdCarga());
        entity.setMensaje(dto.getMensaje());
        entity.setDestinatario(dto.getDestinatario());
        entity.setTipo(TipoNotificacion.valueOf(dto.getTipo()));
        entity.setEstado(EstadoNotificacion.valueOf(dto.getEstado()));

        Notificacion actualizada = repository.save(entity);
        return convertirResponseDTO(actualizada);
    }

    // ELIMINAR NOTIFICACIÓN
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada");
        }
        repository.deleteById(id);
    }

    // CONVERTIR ENTITY -> RESPONSE DTO
    private NotificacionResponseDTO convertirResponseDTO(Notificacion entity) {
        return NotificacionResponseDTO.builder()
                .idNotificacion(entity.getIdNotificacion())
                // ✅ SOLUCIONADO: Retorna idCarga de forma consistente
                .idCarga(entity.getIdCarga())
                .mensaje(entity.getMensaje())
                .tipo(entity.getTipo().name())
                .destinatario(entity.getDestinatario())
                .estado(entity.getEstado().name())
                .fechaNotificacion(entity.getFechaNotificacion())
                .build();
    }

    // 🚀 MÉTODO ASÍNCRONO COMPLETAMENTE SINCRONIZADO
    @Transactional
    @Async
    public void enviarEmailAsincrono(Long idCarga, String email, String mensaje) {
        try {
            System.out.println("==== HILO EN SEGUNDO PLANO ENTRANTE ====");
            System.out.println("Preparando entorno de correo para: " + email);

            // Simulamos la lentitud del servidor de correos demorando 3 segundos
            Thread.sleep(3000);

            // ✅ SOLUCIONADO: Cambiado a Builder e idCarga unificado
            Notificacion entity = Notificacion.builder()
                    .idCarga(idCarga)
                    .destinatario(email)
                    .mensaje(mensaje)
                    .tipo(TipoNotificacion.EMAIL)
                    .estado(EstadoNotificacion.ENVIADA)
                    .fechaNotificacion(LocalDateTime.now())
                    .build();

            repository.save(entity);

            System.out.println("Notificación enviada y guardada en BD con éxito para la carga: " + idCarga);
            System.out.println("========================================");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("❌ Error de interrupción en el hilo secundario: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ ERROR CRÍTICO AL GUARDAR EN BD DESDE EL HILO ASÍNCRONO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}