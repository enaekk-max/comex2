package com.aduanas.comex.clasificacionms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notificacion-ms", url = "http://localhost:8087")
public interface NotificacionClient {

    @PostMapping("/api/v1/notificaciones/enviar-clasificacion") // ◄ Alineado con el método del Service
    void enviarNotificacionClasificacion(
            @RequestParam("correo") String correo, // ◄ ✅ CORREGIDO: De 'destino' a 'correo' para que ensamble con el MS destinatario
            @RequestParam("mensaje") String mensaje
    );
}