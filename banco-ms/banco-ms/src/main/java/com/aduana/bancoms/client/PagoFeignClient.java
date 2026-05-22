package com.aduana.bancoms.client;

import com.aduana.bancoms.dto.NotificacionBancoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Conectamos con el microservicio de pagos (puerto 8083)
@FeignClient(name = "pagos-ms", url = "http://localhost:8083")
public interface PagoFeignClient {

    // CORREGIDO: Ahora le pega a la ruta oficial con /api/v1/pagos
    // Revisa si en tu PagoController el endpoint de recibir la notificación se llama /notificar o /notificacion-banco
    @PostMapping("/api/v1/pagos/notificacion-banco")
    void enviarConfirmacionAlMicroservicioPagos(@RequestBody NotificacionBancoDto dto);
}