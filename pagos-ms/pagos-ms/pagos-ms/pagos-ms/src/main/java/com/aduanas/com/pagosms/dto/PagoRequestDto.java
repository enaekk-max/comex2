package com.aduanas.com.pagosms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoRequestDto {
    private Long idCarga; // ✅ Cambiado para que calce con .getIdCarga()
    private BigDecimal monto;
    private String moneda;
    private String idTransaccionExterna;
}