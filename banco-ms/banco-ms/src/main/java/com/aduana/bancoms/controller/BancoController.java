package com.aduana.bancoms.controller;

import com.aduana.bancoms.dto.TransaccionRequestDto;
import com.aduana.bancoms.service.BancoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/banco")
@RequiredArgsConstructor
public class BancoController {

    private final BancoService bancoService;

    // ✅ CORREGIDO: URL real mapeada según los RequestMapping del proyecto
    // POST: http://localhost:8086/api/v1/banco/procesar
    @PostMapping("/procesar")
    public ResponseEntity<Map<String, String>> pagarCargaAduanera(@RequestBody TransaccionRequestDto dto) {

        // Llama a la lógica de negocio que valida la tarjeta y gatilla el Feign de vuelta
        String resultado = bancoService.procesarTransaccionBancaria(dto);

        // Retorna un JSON limpio con el mensaje de éxito y el estado del procesamiento
        return ResponseEntity.ok(Map.of(
                "status", "PROCESADO",
                "detalle", resultado
        ));
    }
}