package com.aduanas.comex.riesgo_ms.controller;

import com.aduanas.comex.riesgo_ms.dto.RiesgoRequestDTO;
import com.aduanas.comex.riesgo_ms.dto.RiesgoResponseDTO;
import com.aduanas.comex.riesgo_ms.service.RiesgoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/riesgos")
public class RiesgoController {

    private final RiesgoService service;

    // ✅ CONECTADO: Mapeo exacto de parámetros URL para el cliente Feign de clasificación
    @GetMapping("/evaluar")
    public ResponseEntity<Boolean> evaluarRiesgoPolitico(
            @RequestParam String rut,
            @RequestParam String pais
    ) {
        boolean tieneRiesgo = service.evaluarRiesgoPolitico(rut, pais);
        return ResponseEntity.ok(tieneRiesgo);
    }

    @PostMapping
    public ResponseEntity<RiesgoResponseDTO> crear(@Valid @RequestBody RiesgoRequestDTO dto) {
        RiesgoResponseDTO respuesta = service.crear(dto);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @PostMapping("/evaluar/{idCarga}")
    public ResponseEntity<RiesgoResponseDTO> evaluarCarga(@PathVariable Long idCarga) {
        RiesgoResponseDTO respuesta = service.evaluarCarga(idCarga);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    public ResponseEntity<List<RiesgoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{idRiesgo}")
    public ResponseEntity<RiesgoResponseDTO> buscarPorId(@PathVariable Long idRiesgo) {
        return ResponseEntity.ok(service.buscarPorId(idRiesgo));
    }

    @GetMapping("/canal/{canal}")
    public ResponseEntity<List<RiesgoResponseDTO>> buscarPorCanal(@PathVariable String canal) {
        return ResponseEntity.ok(service.buscarPorCanal(canal));
    }

    @GetMapping("/carga/{idCarga}")
    public ResponseEntity<List<RiesgoResponseDTO>> buscarPorCarga(@PathVariable Long idCarga) {
        return ResponseEntity.ok(service.buscarPorCarga(idCarga));
    }

    @PutMapping("/{idRiesgo}")
    public ResponseEntity<RiesgoResponseDTO> actualizar(@PathVariable Long idRiesgo, @Valid @RequestBody RiesgoRequestDTO dto) {
        return ResponseEntity.ok(service.actualizar(idRiesgo, dto));
    }

    @DeleteMapping("/{idRiesgo}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idRiesgo) {
        service.eliminar(idRiesgo);
        return ResponseEntity.noContent().build();
    }
}