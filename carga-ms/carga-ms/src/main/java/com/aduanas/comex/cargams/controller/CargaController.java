package com.aduanas.comex.cargams.controller;

import com.aduanas.comex.cargams.dto.CrearCargaRequestDTO;
import com.aduanas.comex.cargams.dto.CargaResponseDTO;
import com.aduanas.comex.cargams.enums.EstadoCarga;
import com.aduanas.comex.cargams.service.CargaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cargas")
@RequiredArgsConstructor
public class CargaController {

    private final CargaService cargaService;

    @PostMapping
    public ResponseEntity<CargaResponseDTO> crear(
            @Valid @RequestBody CrearCargaRequestDTO dto) {
        return ResponseEntity.ok(cargaService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<CargaResponseDTO>> listar() {
        return ResponseEntity.ok(cargaService.listar());
    }

    @GetMapping("/{idCarga}")
    public ResponseEntity<CargaResponseDTO> obtenerPorId(@PathVariable Long idCarga) {
        return ResponseEntity.ok(cargaService.obtenerPorId(idCarga));
    }

    @PutMapping("/{idCarga}/asignar-impuesto")
    public ResponseEntity<Void> asignarImpuestoYEstado(
            @PathVariable Long idCarga,
            @RequestParam BigDecimal impuesto,
            @RequestParam EstadoCarga estado) {

        cargaService.actualizarImpuestoYEstado(idCarga, impuesto, estado);
        return ResponseEntity.ok().build();
    }
}