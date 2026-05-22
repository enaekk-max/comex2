package com.aduanas.comex.notificacion_ms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionResponseDTO {

    // ✅ Sincronizado con el nombre de la entidad
    private Long idNotificacion;

    // ✅ SOLUCIONADO: Cambiado de cargaId a idCarga
    private Long idCarga;

    private String mensaje;

    private String tipo;

    private String destinatario;

    private String estado;

    // ✅ Sincronizado con el nombre de la entidad
    private LocalDateTime fechaNotificacion;
}