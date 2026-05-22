package com.aduanas.com.pagosms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ Agregado: Mantiene el estándar de DTOs con capacidad de Builder
public class NotificacionBancoDto {

    @NotNull(message = "El ID del pago es obligatorio")
    private Long idPago; // ✅ CORREGIDO: Calza con .getIdPago() del Service

    @NotNull(message = "El ID de la carga es obligatorio")
    private Long idCarga; // ✅ CORREGIDO: Calza con .getIdCarga() del Service

    @NotBlank(message = "El código de váucher no puede estar vacío")
    private String idTransaccionExterna; // ✅ CORREGIDO: Calza con .getIdTransaccionExterna() del Service

    @NotNull(message = "El estado de la transacción es obligatorio")
    private Boolean transaccionExitosa; // true o false (Se mantiene igual)
}