package com.aduanas.comex.clasificacionms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;

@FeignClient(name = "pago-ms", url = "http://localhost:8083")
public interface PagoClient {

    // Ruta unificada y nombres de parámetros idénticos al controlador
    @PostMapping("/api/v1/pagos/crear-orden")
    void crearOrdenDePago(
            @RequestParam("idCarga") Long idCarga, // Asegúrate que diga "idCarga"
            @RequestParam("monto") BigDecimal monto
    );
}