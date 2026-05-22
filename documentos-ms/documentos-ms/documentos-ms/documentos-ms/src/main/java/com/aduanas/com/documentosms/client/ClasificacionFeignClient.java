package com.aduanas.com.documentosms.client;

import com.aduanas.com.documentosms.dto.CargaExternaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// ✅ CORREGIDO: El nombre lógico es 'carga-ms' y el puerto es el 8081, que es donde vive la tabla 'cargas' de Flyway
@FeignClient(name = "carga-ms", url = "http://localhost:8081")
public interface ClasificacionFeignClient {

    // ✅ CORREGIDO: Ruta idéntica a CargaController y parámetro unificado bajo el estándar 'idCarga'
    @GetMapping("/api/v1/cargas/{idCarga}")
    CargaExternaDto obtenerCargaPorId(@PathVariable("idCarga") Long idCarga);
}