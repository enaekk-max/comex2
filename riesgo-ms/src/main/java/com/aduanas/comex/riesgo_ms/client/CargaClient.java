package com.aduanas.comex.riesgo_ms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "carga-ms", url = "http://localhost:8081")
public interface CargaClient {


    @GetMapping("/api/v1/cargas/{id}") // <-- CORREGIDO: Ahora sí calza con tu controlador
    Object obtenerCargaPorId(@PathVariable("id") Long id);
}