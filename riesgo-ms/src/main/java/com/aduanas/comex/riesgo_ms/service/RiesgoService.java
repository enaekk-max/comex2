package com.aduanas.comex.riesgo_ms.service;

import com.aduanas.comex.riesgo_ms.client.CargaClient;
import com.aduanas.comex.riesgo_ms.dto.RiesgoRequestDTO;
import com.aduanas.comex.riesgo_ms.dto.RiesgoResponseDTO;
import com.aduanas.comex.riesgo_ms.entity.Riesgo;
import com.aduanas.comex.riesgo_ms.enums.CanalRiesgo;
import com.aduanas.comex.riesgo_ms.exception.RiesgoException;
import com.aduanas.comex.riesgo_ms.repository.RiesgoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class RiesgoService {

    private final RiesgoRepository repository;
    private final CargaClient cargaClient;

    @Transactional(readOnly = true)
    public boolean evaluarRiesgoPolitico(String rut, String pais) {
        log.info("Evaluando matriz de riesgo político para RUT: {} y País: {}", rut, pais);
        if ("AFGANISTAN".equalsIgnoreCase(pais) || "IRAN".equalsIgnoreCase(pais) || rut.startsWith("999")) {
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public RiesgoResponseDTO crear(RiesgoRequestDTO dto) {
        Riesgo riesgo = Riesgo.builder()
                .descripcion(dto.getDescripcion())
                .tipoCarga(dto.getTipoCarga())
                .origen(dto.getOrigen())
                .idCarga(dto.getIdCarga())
                .puntajeRiesgo(dto.getPuntajeRiesgo())
                .motivoAlerta(dto.getMotivoAlerta())
                .canalAsignado(CanalRiesgo.valueOf(dto.getCanalAsignado().toUpperCase()))
                .fechaRegistro(LocalDateTime.now())
                .fechaEvaluacion(LocalDateTime.now())
                .build();

        Riesgo guardado = repository.save(riesgo);
        return convertirResponseDTO(guardado);
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public RiesgoResponseDTO evaluarCarga(Long idCarga) {
        int puntaje = calcularPuntaje(idCarga);
        String descripcionCarga = "Evaluación automática de riesgo";
        String paisOrigen = "Desconocido";

        try {
            Object respuestaCarga = cargaClient.obtenerCargaPorId(idCarga);
            if (respuestaCarga instanceof Map<?, ?> mapaGenerico) {
                // ✅ SOLUCIONADO: Casteo seguro a tipos conocidos para evitar el error 'capture<?>'
                Map<String, Object> datosCarga = (Map<String, Object>) mapaGenerico;
                descripcionCarga = (String) datosCarga.getOrDefault("descripcion", descripcionCarga);
                paisOrigen = (String) datosCarga.getOrDefault("paisOrigen", paisOrigen);
            }
        } catch (Exception e) {
            log.error("⚠️ No se pudo conectar con carga-ms. Usando genéricos: {}", e.getMessage());
        }

        Riesgo riesgo = Riesgo.builder()
                .idCarga(idCarga)
                .puntajeRiesgo(puntaje)
                .descripcion(descripcionCarga)
                .tipoCarga("Carga General")
                .origen(paisOrigen)
                .fechaRegistro(LocalDateTime.now())
                .fechaEvaluacion(LocalDateTime.now())
                .build();

        if (puntaje < 40) {
            riesgo.setCanalAsignado(CanalRiesgo.VERDE);
            riesgo.setMotivoAlerta("Carga confiable. Canal Verde automático.");
        } else if (puntaje < 70) {
            riesgo.setCanalAsignado(CanalRiesgo.NARANJA);
            riesgo.setMotivoAlerta("Riesgo moderado. Revisión documental.");
        } else {
            riesgo.setCanalAsignado(CanalRiesgo.ROJO);
            riesgo.setMotivoAlerta("Alto riesgo. Requiere inspección física.");
        }

        Riesgo guardado = repository.save(riesgo);
        return convertirResponseDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<RiesgoResponseDTO> listar() {
        return repository.findAll().stream().map(this::convertirResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public RiesgoResponseDTO buscarPorId(Long idRiesgo) {
        Riesgo riesgo = repository.findById(idRiesgo)
                .orElseThrow(() -> new RiesgoException("Riesgo no encontrado"));
        return convertirResponseDTO(riesgo);
    }

    @Transactional(readOnly = true)
    public List<RiesgoResponseDTO> buscarPorCanal(String canal) {
        CanalRiesgo canalEnum = CanalRiesgo.valueOf(canal.toUpperCase());
        return repository.findByCanalAsignado(canalEnum).stream().map(this::convertirResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<RiesgoResponseDTO> buscarPorCarga(Long idCarga) {
        return repository.findByIdCarga(idCarga).stream().map(this::convertirResponseDTO).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public RiesgoResponseDTO actualizar(Long idRiesgo, RiesgoRequestDTO dto) {
        Riesgo riesgo = repository.findById(idRiesgo)
                .orElseThrow(() -> new RiesgoException("Riesgo no encontrado"));

        riesgo.setDescripcion(dto.getDescripcion());
        riesgo.setTipoCarga(dto.getTipoCarga());
        riesgo.setOrigen(dto.getOrigen());
        riesgo.setIdCarga(dto.getIdCarga());
        riesgo.setPuntajeRiesgo(dto.getPuntajeRiesgo());
        riesgo.setMotivoAlerta(dto.getMotivoAlerta());
        riesgo.setCanalAsignado(CanalRiesgo.valueOf(dto.getCanalAsignado().toUpperCase()));
        riesgo.setFechaEvaluacion(LocalDateTime.now());

        Riesgo actualizado = repository.save(riesgo);
        return convertirResponseDTO(actualizado);
    }

    @Transactional(rollbackFor = Exception.class)
    public void eliminar(Long idRiesgo) {
        Riesgo riesgo = repository.findById(idRiesgo)
                .orElseThrow(() -> new RiesgoException("Riesgo no encontrado"));
        repository.delete(riesgo);
    }

    private int calcularPuntaje(Long idCarga) {
        return (int) (Math.random() * 100);
    }

    private RiesgoResponseDTO convertirResponseDTO(Riesgo riesgo) {
        return RiesgoResponseDTO.builder()
                .idRiesgo(riesgo.getIdRiesgo())
                .descripcion(riesgo.getDescripcion())
                .tipoCarga(riesgo.getTipoCarga())
                .origen(riesgo.getOrigen())
                .fechaRegistro(riesgo.getFechaRegistro())
                .idCarga(riesgo.getIdCarga())
                .puntajeRiesgo(riesgo.getPuntajeRiesgo())
                .canalAsignado(riesgo.getCanalAsignado() != null ? riesgo.getCanalAsignado().name() : null)
                .motivoAlerta(riesgo.getMotivoAlerta())
                .fechaEvaluacion(riesgo.getFechaEvaluacion())
                .build();
    }
}