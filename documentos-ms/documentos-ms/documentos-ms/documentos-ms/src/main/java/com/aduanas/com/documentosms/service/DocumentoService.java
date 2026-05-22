package com.aduanas.com.documentosms.service;

import com.aduanas.com.documentosms.client.ClasificacionFeignClient;
import com.aduanas.com.documentosms.client.NotificacionFeignClient;
import com.aduanas.com.documentosms.dto.*;
import com.aduanas.com.documentosms.entity.Documento;
import com.aduanas.com.documentosms.Enum.EstadoValidacion;         // ✅ CORREGIDO: Paquete 'enums' en minúscula
import com.aduanas.com.documentosms.Enum.EstadoValidacionArchivo;  // ✅ CORREGIDO: Paquete 'enums' en minúscula
import com.aduanas.com.documentosms.repository.DocumentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final ClasificacionFeignClient clasificacionFeignClient;
    private final NotificacionFeignClient notificacionFeignClient;

    @Transactional(rollbackFor = Exception.class)
    public DocumentoResponseUsuarioExDTO clienteSubeDocumento(DocumentoRequestUsuarioExDTO dto) {
        // ✅ CORREGIDO: Usando el estándar absoluto idCarga
        log.info("Gatillando Feign para verificar si existe la Carga ID: {}", dto.getCargaId());

        // 1. Validamos mediante Feign con Clasificación si la carga existe.
        clasificacionFeignClient.obtenerCargaPorId(dto.getCargaId());

        // 2. Si existía, creamos el documento amarrado a esa carga
        Documento documento = Documento.builder()
                .idCarga(dto.getCargaId()) // ✅ CORREGIDO: Cambiado de cargaId a idCarga
                .tipoDocumento(dto.getTipoDocumento())
                .rutaArchivo("/sistema/almacenamiento/aduana/" + dto.getTipoDocumento().toLowerCase() + "_simulado.pdf")
                .observacionManual("Comentario Cliente: " + dto.getObservacionManual())
                .estadoValidacion(EstadoValidacion.PENDIENTE)
                .fechaDocumento(LocalDateTime.now())
                .build();

        return mapToUsuarioExDTO(documentoRepository.save(documento));
    }

    @Transactional(rollbackFor = Exception.class)
    public DocumentoResponseAnalistaDTO analistaRevisaDocumento(Long idDocumento, DocumentoRequestAnalistaDTO dto) {
        Documento existente = documentoRepository.findById(idDocumento)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado con el ID: " + idDocumento));

        existente.setResultadoRevision(dto.getResultadoRevision());

        if (dto.getResultadoRevision() == EstadoValidacionArchivo.PDF_VALIDO) {
            existente.setEstadoValidacion(EstadoValidacion.VALIDADO);
            existente.setObservacionManual("Revisión Manual Exitosa: " + dto.getObservaciones());

        } else if (dto.getResultadoRevision() == EstadoValidacionArchivo.MALWARE_DETECTADO) {
            existente.setEstadoValidacion(EstadoValidacion.RECHAZADO);
            existente.setObservacionManual("[ALERTA DE SEGURIDAD] " + dto.getObservaciones());

        } else if (dto.getResultadoRevision() == EstadoValidacionArchivo.FORMATO_INCORRECTO) {
            existente.setEstadoValidacion(EstadoValidacion.RECHAZADO);
            existente.setObservacionManual("Rechazado por formato: " + dto.getObservaciones());
        }

        existente.setFechaDocumento(LocalDateTime.now());
        Documento guardado = documentoRepository.save(existente);

        // 🔥 SOLUCIONADO: Ahora armamos un único argumento (Map) estructurado como JSON
        try {
            log.info("Enviando cuerpo unificado a notificacion-ms para el documento {}", guardado.getIdDocumento());

            String mensajeAlerta = "Documento ID: " + guardado.getIdDocumento() +
                    " cambió a Estado: " + guardado.getEstadoValidacion().name() +
                    ". Obs: " + guardado.getObservacionManual();

            // Creamos el mapa con la estructura exacta que espera el DTO de Notificación
            Map<String, Object> payloadNotificacion = Map.of(
                    "idCarga", guardado.getIdCarga(), // ✅ Conectado con el estándar de auditoría
                    "destinatario", "analista.aduana@comex.cl",
                    "mensaje", mensajeAlerta,
                    "tipo", "EMAIL",
                    "estado", "ENVIADA"
            );

            // ✅ SE ENVÍA SOLO 1 ARGUMENTO: El mapa que Feign serializará automáticamente a JSON
            notificacionFeignClient.enviarNotificacionDocumento(payloadNotificacion);

        } catch (Exception e) {
            log.error("No se pudo enviar la notificación pero el documento quedó guardado igual: {}", e.getMessage());
        }

        return mapToAnalistaDTO(guardado);
    }

    public DocumentoResponseAnalistaDTO buscarPorId(Long idDocumento) {
        Documento documento = documentoRepository.findById(idDocumento)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado con el ID: " + idDocumento));
        return mapToAnalistaDTO(documento);
    }

    public List<DocumentoResponseAnalistaDTO> listar() {
        return documentoRepository.findAll().stream()
                .map(this::mapToAnalistaDTO)
                .toList();
    }

    // 🛠️ MAPEADORES BLINDADOS CON BUILDER
    private DocumentoResponseUsuarioExDTO mapToUsuarioExDTO(Documento m) {
        String estadoStr = m.getEstadoValidacion() != null ? m.getEstadoValidacion().name() : null;
        return DocumentoResponseUsuarioExDTO.builder()
                .idDocumento(m.getIdDocumento())
                .tipoDocumento(m.getTipoDocumento())
                .estadoValidacion(estadoStr)
                .fechaCreacion(m.getFechaDocumento())
                .build();
    }

    private DocumentoResponseAnalistaDTO mapToAnalistaDTO(Documento m) {
        String estadoStr = m.getEstadoValidacion() != null ? m.getEstadoValidacion().name() : null;
        return DocumentoResponseAnalistaDTO.builder()
                .idDocumento(m.getIdDocumento())
                .estadoValidacion(estadoStr)
                .datosExtraidos(m.getObservacionManual())
                .fechaRevision(m.getFechaDocumento())
                .build();
    }
}