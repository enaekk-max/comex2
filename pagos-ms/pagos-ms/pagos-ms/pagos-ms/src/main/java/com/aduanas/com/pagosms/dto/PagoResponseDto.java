package com.aduanas.com.pagosms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponseDto {
    private Long idPago;
    private Long idCarga;
    private String tramo;
    private String cargoFijo;
    private BigDecimal montoImpuesto;
    private BigDecimal montoTotal;
    private String moneda;
    private String estadoPago;
    private String idTransaccionExterna;
    private LocalDateTime fechaPago;
}