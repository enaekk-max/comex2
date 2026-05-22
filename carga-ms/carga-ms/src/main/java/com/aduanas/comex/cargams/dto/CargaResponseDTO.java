package com.aduanas.comex.cargams.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargaResponseDTO {

    private Long idCarga; // ✅ CORREGIDO: Mapea directo con idCarga de la Entidad
    private String numeroDeclaracion;
    private String descripcion;
    private String paisOrigen;
    private BigDecimal valorDeclarado;
    private BigDecimal peso; // ✅ AGREGADO: Faltaba este dato de la carga
    private String importadorRut;
    private String estado;
    private LocalDateTime fechaCreacion;

    // Datos cruzados agregados dinámicamente desde clasificacion-ms (Sin @Transient)
    private Boolean permitido;
    private BigDecimal montoImpuesto;
    private String observaciones;
}