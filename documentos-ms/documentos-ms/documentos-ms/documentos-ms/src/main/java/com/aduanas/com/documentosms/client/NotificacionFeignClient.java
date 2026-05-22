package com.aduanas.com.documentosms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

// ✅ CORREGIDO: URL con la ruta base real del controlador (/api/v1/notificaciones) y puerto correcto
@FeignClient(name = "notificacion-ms", url = "http://localhost:8087/api/v1/notificaciones")
public interface NotificacionFeignClient {

    // ✅ CORREGIDO: Ahora usa @RequestBody y envía un mapa estructurado que simula perfectamente el NotificacionRequestDTO
    @PostMapping("/enviar-alerta")
    void enviarNotificacionDocumento(@RequestBody Map<String, Object> notificacionBody);
}