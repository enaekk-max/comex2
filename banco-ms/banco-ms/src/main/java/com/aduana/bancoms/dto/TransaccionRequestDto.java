package com.aduana.bancoms.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransaccionRequestDto {

    private Long idPago;           // ✅ ALINEADO: De 'pagoId' a 'idPago'
    private Long idCarga;          // ✅ ALINEADO: De 'cargaId' a 'idCarga'
    private BigDecimal monto;
    private String numeroTarjeta;
    private String nombreTitular;
}