package com.aduanas.comex.clasificacionms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasificacionResponseDTO {

    private Long idClasificacion; // ✅ CORREGIDO: Matamos el "id" genérico
    private Long cargaId;
    private String tipoClasificacion;
    private Boolean permitido;
    private String observaciones;

    // 🍏 CAMPOS COORDENADOS PARA PAGOS-MS:
    private BigDecimal montoImpuesto;
    private String estado; // ✅ CORREGIDO: Cambiado a String para evitar acoplamiento fuerte de clases entre microservicios
    private BigDecimal peso;
    private BigDecimal valorDeclarado;
}