package com.aduanas.comex.riesgo_ms.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiesgoResponseDTO {

    private Long idRiesgo; // ✅ CORREGIDO: Cambiado de 'id' a 'idRiesgo' para calzar con la entidad

    private String descripcion;

    private String tipoCarga;

    private String origen;

    private LocalDateTime fechaRegistro;

    private Long idCarga; // ✅ CORREGIDO: Cambiado de 'cargaId' a 'idCarga' para mantener el estándar

    private Integer puntajeRiesgo;

    private String canalAsignado;

    private String motivoAlerta;

    private LocalDateTime fechaEvaluacion;
}