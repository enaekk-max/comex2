package com.aduanas.comex.clasificacionms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "riesgo-ms", url = "http://localhost:8085")
public interface RiesgoClient {

    // ✅ COORDINADO: "rut" y "pais" son las etiquetas que espera el puerto 8085 en la URL
    @GetMapping("/evaluar")
    boolean evaluarRiesgoCarga(
            @RequestParam("rut") String importadorRut, // ◄ Mapea el RUT del importador
            @RequestParam("pais") String paisOrigen   // ◄ Mapea el país de origen de la carga
    );
}