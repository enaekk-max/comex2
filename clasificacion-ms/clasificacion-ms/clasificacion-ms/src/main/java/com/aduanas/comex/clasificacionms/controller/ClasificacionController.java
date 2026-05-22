package com.aduanas.comex.clasificacionms.controller;

import com.aduanas.comex.clasificacionms.dto.ClasificacionResponseDTO;
import com.aduanas.comex.clasificacionms.dto.EvaluarClasificacionRequestDTO;
import com.aduanas.comex.clasificacionms.service.ClasificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clasificaciones")
@RequiredArgsConstructor
public class ClasificacionController {

    private final ClasificacionService clasificacionService;

    // 1. EVALUAR UNA CARGA (✅ SOLUCIONADO: Cambiado a idCarga para hacer match con el Feign de Cargas)
    @PostMapping("/evaluar/{idCarga}")
    public ResponseEntity<ClasificacionResponseDTO> evaluar(
            @PathVariable Long idCarga,
            @Valid @RequestBody EvaluarClasificacionRequestDTO dto) {

        // Forzamos que el ID de la URL sea el mismo del DTO por consistencia
        dto.setCargaId(idCarga);
        return ResponseEntity.ok(clasificacionService.evaluar(dto));
    }

    // 2. LISTAR TODAS LAS EVALUACIONES
    @GetMapping
    public ResponseEntity<List<ClasificacionResponseDTO>> listar() {
        return ResponseEntity.ok(clasificacionService.listar());
    }

    // 3. OBTENER POR ID DE CLASIFICACIÓN
    @GetMapping("/{idClasificacion}")
    public ResponseEntity<ClasificacionResponseDTO> obtenerPorId(@PathVariable Long idClasificacion) {
        return ResponseEntity.ok(clasificacionService.obtenerPorId(idClasificacion));
    }

    // 4. OBTENER LA ÚLTIMA CLASIFICACIÓN DE UNA CARGA ESPECÍFICA (✅ SOLUCIONADO: idCarga unificado)
    @GetMapping("/cargas/{idCarga}")
    public ResponseEntity<ClasificacionResponseDTO> obtenerPorCargaId(@PathVariable Long idCarga) {
        return ResponseEntity.ok(clasificacionService.obtenerPorCargaId(idCarga));
    }
}