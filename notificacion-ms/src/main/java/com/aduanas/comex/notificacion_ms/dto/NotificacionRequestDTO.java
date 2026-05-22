package com.aduanas.comex.notificacion_ms.dto;

// VALIDACIONES
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// LOMBOK
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionRequestDTO {

    // ✅ SOLUCIONADO: Cambiado a idCarga para mantener la consistencia total
    @NotNull(message = "El ID de la carga es obligatorio")
    private Long idCarga;

    @NotBlank(message = "El destinatario es obligatorio")
    private String destinatario;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}