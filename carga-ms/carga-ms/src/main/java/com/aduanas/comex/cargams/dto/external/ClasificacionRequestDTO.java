package com.aduanas.comex.cargams.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasificacionRequestDTO {
    private Long cargaId;
    private String descripcionMercancia;
    private String paisOrigen;
    private BigDecimal valorDeclarado;
    private String importadorRut;
}