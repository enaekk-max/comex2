package com.aduanas.comex.notificacion_ms.entity;

import com.aduanas.comex.notificacion_ms.enums.EstadoNotificacion;
import com.aduanas.comex.notificacion_ms.enums.TipoNotificacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ Listo para usar con el Service
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotificacion;

    // ✅ CORREGIDO: Ahora se llama idCarga e id_carga en la BD. ¡Consistencia total!
    @Column(name = "id_carga", nullable = false)
    private Long idCarga;

    @Column(name = "mensaje", nullable = false, length = 255)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacion", nullable = false) // ✅ En minúsculas y estándar
    private TipoNotificacion tipo;

    @Column(name = "destinatario", nullable = false, length = 100)
    private String destinatario;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_notificacion", nullable = false)
    private EstadoNotificacion estado;

    @Column(name = "fecha_notificacion", nullable = false)
    private LocalDateTime fechaNotificacion;
}