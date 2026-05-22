package com.aduanas.comex.clasificacionms.service;

import com.aduanas.comex.clasificacionms.client.*;
import com.aduanas.comex.clasificacionms.dto.ClasificacionResponseDTO;
import com.aduanas.comex.clasificacionms.dto.EvaluarClasificacionRequestDTO;
import com.aduanas.comex.clasificacionms.entity.Clasificacion;
import com.aduanas.comex.clasificacionms.enums.TipoClasificacion;
import com.aduanas.comex.clasificacionms.repository.ClasificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClasificacionService {

    private final ClasificacionRepository clasificacionRepository;
    private final CargaClient cargaClient;
    private final DocumentoClient documentoClient;
    private final PagoClient pagoClient;
    private final NotificacionClient notificacionClient;
    private final RiesgoClient riesgoClient;

    @Transactional(rollbackFor = Exception.class)
    public ClasificacionResponseDTO evaluar(EvaluarClasificacionRequestDTO dto) {
        log.info("Iniciando auditoría tributaria y análisis de riesgo para Carga ID: {}", dto.getCargaId());

        // =========================================================================
        // ANÁLISIS DE RIESGO ADUANERO 🕵️‍♂️
        // =========================================================================
        String rutEvaluar = "12345678-9";
        boolean tieneRiesgo = false;

        try {
            log.info("Consultando matriz de riesgos para RUT: {} y Origen: {}", dto.getImportadorRut(), dto.getPaisOrigen());

            // ✅ CONECTADO: Riesgo ahora evalúa con el RUT real del importador que creó la carga
            tieneRiesgo = riesgoClient.evaluarRiesgoCarga(dto.getImportadorRut(), dto.getPaisOrigen());
        } catch (Exception e) {
            log.error("No se pudo conectar con riesgo-ms: {}", e.getMessage());
        }

        if (tieneRiesgo) {
            log.warn("ALERTA: Carga ID {} RECHAZADA por la matriz de riesgo aduanero.", dto.getCargaId());

            String obsRechazo = "Carga rechazada por el Departamento de Gestión de Riesgos. País de origen o importador bajo observación.";

            Clasificacion clasificacionRechazada = Clasificacion.builder()
                    .idCarga(dto.getCargaId())
                    .tipoClasificacion(TipoClasificacion.PROHIBIDA)
                    .permitido(false)
                    .montoImpuesto(BigDecimal.ZERO)
                    .observaciones(obsRechazo)
                    .fechaEvaluacion(LocalDateTime.now())
                    .build();

            clasificacionRechazada = clasificacionRepository.save(clasificacionRechazada);

            try {
                // ✅ CORREGIDO: Mandamos el estado como String o el Enum propio del MS, evitando acoplamiento cruzado de Enums
                cargaClient.actualizarImpuestoYEstado(dto.getCargaId(), BigDecimal.ZERO, "RECHAZADA");
            } catch (Exception e) {
                log.error("Error al actualizar estado de rechazo en carga-ms: {}", e.getMessage());
            }

            return mapToResponse(clasificacionRechazada);
        }

        // =========================================================================
        // CÁLCULO DE ADUANA CHILE COMPLETO (Si no hay riesgo) 🇨🇱
        // =========================================================================
        BigDecimal valorCarga = dto.getValorDeclarado();

        BigDecimal arancel = valorCarga.multiply(new BigDecimal("0.06"));
        BigDecimal baseIva = valorCarga.add(arancel);
        BigDecimal iva = baseIva.multiply(new BigDecimal("0.19"));
        BigDecimal impuestoTotal = arancel.add(iva);

        String observaciones = String.format(
                "Aprobado. Desglose: Arancel (6%%): $%s | IVA (19%%): $%s. Carga lista para recaudación.",
                arancel.setScale(2, RoundingMode.HALF_UP),
                iva.setScale(2, RoundingMode.HALF_UP)
        );

        Clasificacion clasificacion = Clasificacion.builder()
                .idCarga(dto.getCargaId())
                .tipoClasificacion(TipoClasificacion.IMPORTACION)
                .permitido(true)
                .montoImpuesto(impuestoTotal)
                .observaciones(observaciones)
                .fechaEvaluacion(LocalDateTime.now())
                .build();

        clasificacion = clasificacionRepository.save(clasificacion);
        log.info("Clasificación guardada de forma local con ID: {}", clasificacion.getIdClasificacion()); // ✅ CORREGIDO: idClasificacion

        // =========================================================================
// ORQUESTACIÓN SÍNCRONA EN CASCADA
        // =========================================================================
        try {
            log.info("1. Notificando cambio de estado a carga-ms como PENDIENTE_PAGO");
            cargaClient.actualizarImpuestoYEstado(dto.getCargaId(), impuestoTotal, "PENDIENTE_PAGO");

            log.info("2. Enviando orden de recaudación a pagos-ms para Carga ID: {}", dto.getCargaId());
            // ✅ ESTA ES LA LLAMADA CRUCIAL: Asegúrate que PagoClient use @RequestParam("idCarga")
            pagoClient.crearOrdenDePago(dto.getCargaId(), impuestoTotal);

            log.info("3. Generando documento DIN en documentos-ms");
            documentoClient.generarDeclaracionIngreso(dto.getCargaId(), impuestoTotal, dto.getImportadorRut());

            log.info("4. Enviando alerta de éxito a notificacion-ms");
            notificacionClient.enviarNotificacionClasificacion(
                    "importaciones@comex.cl",
                    "Tu Carga N° " + dto.getCargaId() + " fue clasificada con éxito. Total aduana a pagar: $" + impuestoTotal
            );

        } catch (Exception e) {
            // Si algo falla, el log nos dirá exactamente qué servicio fue
            log.error("⚠️ Error en la cadena de servicios: {}", e.getMessage());
        }

        ClasificacionResponseDTO response = mapToResponse(clasificacion);
        response.setPeso(new BigDecimal("1500.00"));
        response.setValorDeclarado(valorCarga);

        return response;
    }

    public List<ClasificacionResponseDTO> listar() {
        return clasificacionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList(); // ✅ Mapeo simplificado de Java 16+
    }

    public ClasificacionResponseDTO obtenerPorId(Long idClasificacion) { // ✅ CORREGIDO: Variable renombrada
        Clasificacion clasificacion = clasificacionRepository.findById(idClasificacion)
                .orElseThrow(() -> new RuntimeException("Clasificación no encontrada con ID: " + idClasificacion));
        return mapToResponse(clasificacion);
    }

    public ClasificacionResponseDTO obtenerPorCargaId(Long cargaId) {
        // ✅ CORREGIDO: Método adaptado al nombre idClasificacion en el Repository
        Clasificacion clasificacion = clasificacionRepository.findFirstByCargaIdOrderByIdClasificacionDesc(cargaId)
                .orElseThrow(() -> new RuntimeException("Clasificación no encontrada para la carga: " + cargaId));

        ClasificacionResponseDTO response = mapToResponse(clasificacion);

        response.setPeso(new BigDecimal("1500.00"));
        response.setValorDeclarado(new BigDecimal("50000.00"));

        return response;
    }

    private ClasificacionResponseDTO mapToResponse(Clasificacion clasificacion) {
        // ✅ CORREGIDO: El estado se maneja como String puro para desacoplar los servicios
        String estadoCalculado = Boolean.TRUE.equals(clasificacion.getPermitido())
                ? "PENDIENTE_PAGO"
                : "RECHAZADA";

        return ClasificacionResponseDTO.builder()
                .idClasificacion(clasificacion.getIdClasificacion()) // ✅ CORREGIDO: idClasificacion explícito
                .cargaId(clasificacion.getIdCarga())
                .tipoClasificacion(clasificacion.getTipoClasificacion().name())
                .permitido(clasificacion.getPermitido())
                .montoImpuesto(clasificacion.getMontoImpuesto())
                .observaciones(clasificacion.getObservaciones())
                .estado(estadoCalculado) // ✅ Asignado como String
                .build();
    }
}