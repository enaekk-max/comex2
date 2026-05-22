package com.aduanas.comex.cargams.dto.external;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ClasificacionResponseDTO {
    private Long idClasificacion; // ✅ CORREGIDO: Especificado
    private Long cargaId;
    private String tipoClasificacion;
    private Boolean permitido;
    private BigDecimal montoImpuesto;
    private String observaciones;
}