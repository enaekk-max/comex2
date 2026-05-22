package com.aduanas.comex.clasificacionms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;

@FeignClient(name = "carga-ms", url = "http://localhost:8081") // O la URL de tu carga-ms
public interface CargaClient {

    @PutMapping("/api/v1/cargas/{idCarga}/asignar-impuesto")
    void actualizarImpuestoYEstado(
            @PathVariable("carga_id") Long idCarga,
            @RequestParam("impuesto") BigDecimal impuesto,
            @RequestParam("estado") String estado // ◄ ✅ CORREGIDO: Cambiado de EstadoCarga a String
    );
}