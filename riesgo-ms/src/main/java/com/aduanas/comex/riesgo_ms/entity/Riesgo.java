package com.aduanas.comex.riesgo_ms.entity;

import com.aduanas.comex.riesgo_ms.enums.CanalRiesgo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ AGREGADO: Crucial para el mapeo y creación de análisis de riesgo
@Table(name = "riesgos") // Cambiado a plural por estándar de tablas
public class Riesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_riesgo")
    private Long idRiesgo; // ✅ CORREGIDO: Estándar unificado idAlInicio

    @Column(name = "id_carga", nullable = false)
    private Long idCarga; // ✅ CORREGIDO: Estándar unificado idAlInicio

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "tipo_carga", nullable = false)
    private String tipoCarga;

    @Column(name = "origen", nullable = false)
    private String origen;

    @Builder.Default // ✅ AGREGADO: Evita que el Builder de Lombok pise la fecha dejándola en null
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "puntaje_riesgo")
    private Integer puntajeRiesgo;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_asignado")
    private CanalRiesgo canalAsignado; // VERDE, NARANJA, ROJO

    @Column(name = "motivo_alerta")
    private String motivoAlerta;

    @Builder.Default // ✅ AGREGADO: Asegura la estampa de tiempo al evaluar el riesgo
    @Column(name = "fecha_evaluacion")
    private LocalDateTime fechaEvaluacion = LocalDateTime.now();
}