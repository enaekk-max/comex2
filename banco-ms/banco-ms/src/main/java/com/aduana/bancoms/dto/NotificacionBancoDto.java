package com.aduana.bancoms.dto;

import lombok.Data;

@Data
public class NotificacionBancoDto {
    private Long idPago;               // ✅ ALINEADO: De 'pagoId' a 'idPago'
    private Long idCarga;              // ✅ ALINEADO: De 'cargaId' a 'idCarga'
    private String idTransaccionExterna; // Número de váucher único
    private Boolean transaccionExitosa;   // true = aprobada, false = rechazada
}