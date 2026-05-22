package com.aduanas.comex.clasificacionms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;

@FeignClient(name = "documento-ms", url = "http://localhost:8083") // ◄ Quitamos el path de la URL para manejarlo limpio en el mapeo
public interface DocumentoClient {

    @PostMapping("/api/v1/documentos/generar-din") // ◄ RUTA ABSOLUTA CORREGIDA: Evita confusiones de concatenación
    void generarDeclaracionIngreso(
            @RequestParam("carga_id") Long idCarga,
            @RequestParam("montoImpuesto") BigDecimal montoImpuesto,
            @RequestParam("rutImportador") String rutImportador
    );
}